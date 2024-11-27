package utils;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.Duration;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import pojos.*;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2SteamLinuxImpl;
import pojos.kf2factory.Kf2SteamWindowsImpl;
import services.PropertyService;
import services.PropertyServiceImpl;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final Logger logger = LogManager.getLogger(Utils.class);

    public static void errorDialog(String headerText, Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            alert.setTitle(applicationTitle);
        } catch (Exception ex) {
            alert.setTitle("");
        }
        alert.setHeaderText(headerText);

        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();
            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setContent(textArea);
            alert.getDialogPane().setMinWidth(600);
            alert.getDialogPane().setMinHeight(400);
        }

        alert.setWidth(400);
        alert.setHeight(500);
        alert.setResizable(true);
        alert.showAndWait();
    }


    public static Optional<String> OneTextInputDialog(String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
            dialog.getDialogPane().setMinWidth(600);
            dialog.setResizable(true);
        } catch (Exception ex) {
            dialog.setTitle("");
        }

        dialog.setWidth(400);
        dialog.setHeight(500);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait();
    }

    public static Optional<SelectDto> TwoTextInputsDialog() {
        Dialog<SelectDto> dialog = new Dialog<SelectDto>();
        Label code = null;
        Label description = null;
        String languageCode = null;
        PropertyService propertyService = new PropertyServiceImpl();

        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.addItem");
            dialog.setHeaderText(headerText);

            String codeLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.itemCode");
            String descriptionLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.itemDescription");
            code = new Label(codeLabelText + ": ");
            description = new Label(descriptionLabelText + ": ");

        } catch (Exception ex) {
            dialog.setTitle("");
            dialog.setHeaderText("Add a new Item");
            code = new Label("Item code: ");
            description = new Label("Item description: ");
        }

        dialog.setResizable(false);
        TextField codeText = new TextField();
        TextField descriptionText = new TextField();
        Label separator = new Label("");
        GridPane grid = new GridPane();
        grid.add(code, 1, 1);
        grid.add(codeText, 2, 1);
        grid.add(separator, 1, 2);
        grid.add(description, 1, 3);
        grid.add(descriptionText, 2, 3);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = null;
        ButtonType buttonTypeCancel = null;
        try {
            String okText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.ok");
            buttonTypeOk = new ButtonType(okText, ButtonBar.ButtonData.OK_DONE);
            String cancelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.cancel");
            buttonTypeCancel = new ButtonType(cancelText, ButtonBar.ButtonData.CANCEL_CLOSE);
        } catch (Exception e) {
            buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        }

        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        ButtonType finalButtonTypeOk = buttonTypeOk;
        dialog.setResultConverter(new Callback<ButtonType, SelectDto>() {
            @Override
            public SelectDto call(ButtonType b) {
                if (b == finalButtonTypeOk) {
                    return new SelectDto(codeText.getText(), descriptionText.getText());
                }
                return null;
            }
        });

        dialog.setWidth(400);
        dialog.setHeight(500);
        return dialog.showAndWait();
    }

    public static Optional<ButtonType> questionDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            alert.setTitle(applicationTitle);
        } catch (Exception ex) {
            alert.setTitle("");
        }
        alert.setHeaderText(header);

        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setEditable(false);
        alert.getDialogPane().setContent(area);
        alert.setResizable(true);

        alert.setWidth(400);
        alert.setHeight(300);
        return alert.showAndWait();
    }

    public static void warningDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            alert.setTitle(applicationTitle);
        } catch (Exception ex) {
            alert.setTitle("");
        }
        alert.setHeaderText(header);

        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setEditable(false);
        alert.getDialogPane().setContent(area);
        alert.setResizable(true);

        alert.setWidth(400);
        alert.setHeight(300);
        alert.showAndWait();
    }

    public static void infoDialog(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            alert.setTitle(applicationTitle);
        } catch (Exception ex) {
            alert.setTitle("");
        }
        alert.setHeaderText(header);

        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setEditable(false);
        alert.getDialogPane().setContent(area);
        alert.setResizable(true);

        alert.setWidth(400);
        alert.setHeight(300);
        alert.showAndWait();
    }

    public static Optional<UpdateLauncher> updateDialog(String header, String content) {
        Dialog<UpdateLauncher> dialog = new Dialog<UpdateLauncher>();
        PropertyService propertyService = new PropertyServiceImpl();
        Boolean checkForUpgrades = null;
        String languageCode = StringUtils.EMPTY;
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            checkForUpgrades = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.checkForUpgrades"));
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }

        Text headerText = new Text(header + "\n");
        headerText.setStyle("-fx-font-weight: bold");

        TextArea area = new TextArea(content);
        area.setWrapText(true);
        area.setPrefHeight(150);
        area.setEditable(false);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(headerText, area);

        CheckBox dontShowAtStartupCheckBox = new CheckBox();
        Boolean finalCheckForUpgrades = checkForUpgrades;
        String finalLanguageCode = languageCode;
        dialog.setDialogPane(new DialogPane() {
            @Override
            protected Node createDetailsButton() {
                try {
                    dontShowAtStartupCheckBox.setText(propertyService.getPropertyValue("properties/languages/" + finalLanguageCode + ".properties", "prop.message.notShowAtStartup"));
                } catch (Exception e) {
                    dontShowAtStartupCheckBox.setText("Do not show at startup");
                }
                if (finalCheckForUpgrades) {
                    dontShowAtStartupCheckBox.setSelected(false);
                } else {
                    dontShowAtStartupCheckBox.setSelected(true);
                }
                return dontShowAtStartupCheckBox;
            }
        });

        dialog.getDialogPane().setExpandableContent(new Group());
        dialog.getDialogPane().setExpanded(false);
        dialog.getDialogPane().setContent(vBox);;

        String updateStr = StringUtils.EMPTY;
        String cancelStr = StringUtils.EMPTY;
        try {
            updateStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.updateButton");
            cancelStr = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.cancel");
        } catch (Exception e) {
            updateStr = "Update";
            cancelStr = "Cancel";
        }

        ButtonType updateButton = new ButtonType(updateStr, ButtonBar.ButtonData.OK_DONE);
        ButtonType closeButton = new ButtonType(cancelStr, ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(updateButton, closeButton);
        dialog.setResizable(true);

        dialog.setResultConverter(new Callback<ButtonType, UpdateLauncher>() {
            @Override
            public UpdateLauncher call(ButtonType b) {
                return new UpdateLauncher(
                    b.equals(updateButton),
                    dontShowAtStartupCheckBox.isSelected()
                );
            }
        });

        dialog.setWidth(400);
        dialog.setHeight(300);
        return dialog.showAndWait();
    }

    public static void infoDialog(String header, Node content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            alert.setTitle(applicationTitle);
            alert.setHeaderText(header);
            alert.getDialogPane().setContent(content);
            alert.setResizable(true);

            alert.setWidth(400);
            alert.setHeight(300);
            alert.showAndWait();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static List<AddMapsToPlatformProfile> defineCustomMapsToAddPerProfile(String headerText, ObservableList<ProfileDto> profileList) {
        Dialog<TableView<AddMapsToPlatformProfile>> dialog = new Dialog<TableView<AddMapsToPlatformProfile>>();
        PropertyService propertyService = new PropertyServiceImpl();
        String profileNameText = "";
        String defineMapsToAddText = "";

        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            profileNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileName");
            defineMapsToAddText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.defineMapsToAdd");
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }
        dialog.setHeaderText(headerText);

        TableView<AddMapsToPlatformProfile> tableView = new TableView<AddMapsToPlatformProfile>();

        // First Column
        TableColumn<AddMapsToPlatformProfile, String> profileNameColumn = new TableColumn<AddMapsToPlatformProfile, String>();
        profileNameColumn.setText(profileNameText);
        profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().profileNameProperty());
        profileNameColumn.setSortable(false);
        profileNameColumn.setEditable(false);
        profileNameColumn.setMinWidth(150);

        // Second Column
        ObservableList<String> comboBoxOptions = FXCollections.observableArrayList(
                Arrays.stream(EnumPlatform.values()).
                        map(EnumPlatform::getDescripcion).
                        collect(Collectors.toList())
        );

        TableColumn<AddMapsToPlatformProfile, StringProperty> platformColumn = new TableColumn<AddMapsToPlatformProfile, StringProperty>();
        platformColumn.setText("Platform");
        platformColumn.setMinWidth(120);
        platformColumn.setSortable(false);

        platformColumn.setCellFactory(col -> {
            TableCell<AddMapsToPlatformProfile, StringProperty> c = new TableCell<>();
            final ComboBox<String> comboBox = new ComboBox<>(comboBoxOptions);
            c.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if (newValue != null) {
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
            return c;
        });

        platformColumn.setCellValueFactory(cellData -> {
            return Bindings.createObjectBinding(() -> cellData.getValue().platformNameProperty());
        });
        platformColumn.setEditable(true);

        // Third Column
        TableColumn<AddMapsToPlatformProfile, String> customMapsColumn = new TableColumn<AddMapsToPlatformProfile, String>();
        customMapsColumn.setText(defineMapsToAddText);
        customMapsColumn.setMinWidth(500);
        customMapsColumn.setSortable(false);
        // Commit value when lost focus
        customMapsColumn.setCellFactory(cell -> new TableCell<AddMapsToPlatformProfile, String>() {
            private TextField textField = new TextField();
            {
                textField.setOnAction(e -> {
                    commitEdit(textField.getText());
                });
                textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (!newValue) {
                        System.out.println("Commiting " + textField.getText());
                        commitEdit(textField.getText());
                    }
                });
            }

            @Override
            public void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty) {
                    setText(null);
                    setGraphic(null);
                } else if (isEditing()) {
                    textField.setText(value);
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(value);
                    setGraphic(null);
                }
            }

            @Override
            public void startEdit() {
                super.startEdit();
                textField.setText(getItem());
                setText(null);
                setGraphic(textField);
                textField.selectAll();
                textField.requestFocus();
            }

            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem());
                setGraphic(null);
            }

            @Override
            public void commitEdit(String value) {
                super.commitEdit(value);
                setText(textField.getText());
                setGraphic(null);
            }
        });

        customMapsColumn.setCellValueFactory(cellData -> cellData.getValue().mapListProperty());
        customMapsColumn.setEditable(true);

        tableView.getColumns().add(profileNameColumn);
        tableView.getColumns().add(platformColumn);
        tableView.getColumns().add(customMapsColumn);

        if (profileList != null && !profileList.isEmpty()) {
            for (ProfileDto profile: profileList) {
                tableView.getItems().add(new AddMapsToPlatformProfile(profile.getName()));
            }
        }
        tableView.setEditable(true);

        dialog.getDialogPane().setContent(tableView);
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(650);
        dialog.getDialogPane().setMinHeight(400);

        ButtonType buttonTypeOk = null;
        ButtonType buttonTypeCancel = null;
        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String okText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.ok");
            buttonTypeOk = new ButtonType(okText, ButtonBar.ButtonData.OK_DONE);
            String cancelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.cancel");
            buttonTypeCancel = new ButtonType(cancelText, ButtonBar.ButtonData.CANCEL_CLOSE);
        } catch (Exception e) {
            buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        }

        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        ButtonType finalButtonTypeOk = buttonTypeOk;
        dialog.setResultConverter(new Callback<ButtonType, TableView<AddMapsToPlatformProfile>>() {
            @Override
            public TableView call(ButtonType b) {
                if (b == finalButtonTypeOk) {
                    return tableView;
                }
                return null;
            }
        });

        dialog.setWidth(500);
        dialog.setHeight(500);
        Optional<TableView<AddMapsToPlatformProfile>> result = dialog.showAndWait();
        if (result.isPresent() && result.get() != null && result.get().getItems() != null && !result.get().getItems().isEmpty()) {
            return result.get().getItems();
        }
        return new ArrayList<AddMapsToPlatformProfile>();
    }

    public static List<ProfileToDisplay> selectProfilesDialog(String headerText, List<ProfileToDisplay> profileList) {
        Dialog<TableView<ProfileToDisplay>> dialog = new Dialog<TableView<ProfileToDisplay>>();
        PropertyService propertyService = new PropertyServiceImpl();
        String selectText = "";
        String profileNameText = "";
        String gameTypeText = "";
        String mapNameText = "";
        String difficultyText = "";
        String lengthText = "";

        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            selectText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.select");
            profileNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileName");
            gameTypeText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.gametype");
            mapNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
            difficultyText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.difficultyLowercase");
            lengthText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthLowercase");
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }
        dialog.setHeaderText(headerText);

        TableView<ProfileToDisplay> tableView = new TableView<ProfileToDisplay>();

        // First Column
        TableColumn<ProfileToDisplay, Boolean> selectColumn = new TableColumn<ProfileToDisplay, Boolean>();
        selectColumn.setText(selectText);
        selectColumn.setCellFactory(col -> new CheckBoxTableCell<>());
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setSortable(false);
        selectColumn.setEditable(true);
        selectColumn.setMinWidth(50);

        // Second Column
        TableColumn<ProfileToDisplay, String> profileNameColumn = new TableColumn<ProfileToDisplay, String>();
        profileNameColumn.setText(profileNameText);
        profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().profileNameProperty());
        profileNameColumn.setSortable(false);
        profileNameColumn.setEditable(false);
        profileNameColumn.setMinWidth(150);

        // Third Column
        TableColumn<ProfileToDisplay, String> gameTypeColumn = new TableColumn<ProfileToDisplay, String>();
        gameTypeColumn.setText(gameTypeText);
        gameTypeColumn.setCellValueFactory(cellData -> cellData.getValue().gameTypeDescriptionProperty());
        gameTypeColumn.setSortable(false);
        gameTypeColumn.setEditable(false);
        gameTypeColumn.setMinWidth(150);

        // Fourth Column
        TableColumn<ProfileToDisplay, String> mapNameColumn = new TableColumn<ProfileToDisplay, String>();
        mapNameColumn.setText(mapNameText);
        mapNameColumn.setCellValueFactory(cellData -> cellData.getValue().mapNameProperty());
        mapNameColumn.setSortable(false);
        mapNameColumn.setEditable(false);
        mapNameColumn.setMinWidth(150);

        // Fifth Column
        TableColumn<ProfileToDisplay, String> difficultyColumn = new TableColumn<ProfileToDisplay, String>();
        difficultyColumn.setText(difficultyText);
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyDescriptionProperty());
        difficultyColumn.setSortable(false);
        difficultyColumn.setEditable(false);
        difficultyColumn.setMinWidth(150);

        // Sixth Column
        TableColumn<ProfileToDisplay, String> lengthColumn = new TableColumn<ProfileToDisplay, String>();
        lengthColumn.setText(lengthText);
        lengthColumn.setCellValueFactory(cellData -> cellData.getValue().lengthDescriptionProperty());
        lengthColumn.setSortable(false);
        lengthColumn.setEditable(false);
        lengthColumn.setMinWidth(150);

        tableView.getColumns().add(selectColumn);
        tableView.getColumns().add(profileNameColumn);
        tableView.getColumns().add(gameTypeColumn);
        tableView.getColumns().add(mapNameColumn);
        tableView.getColumns().add(difficultyColumn);
        tableView.getColumns().add(lengthColumn);
        tableView.setItems(FXCollections.observableArrayList(profileList));
        tableView.setEditable(true);

        dialog.getDialogPane().setContent(tableView);
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(800);
        dialog.getDialogPane().setMinHeight(400);

        ButtonType buttonTypeOk = null;
        ButtonType buttonTypeCancel = null;
        ButtonType selectAll = null;
        ButtonType selectNone = null;
        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String okText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.ok");
            buttonTypeOk = new ButtonType(okText, ButtonBar.ButtonData.OK_DONE);
            String cancelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.cancel");
            buttonTypeCancel = new ButtonType(cancelText, ButtonBar.ButtonData.CANCEL_CLOSE);
            String selectAllText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.selectMaps");
            selectAll = new ButtonType(selectAllText, ButtonBar.ButtonData.LEFT);
            String unselectAllText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unselectMaps");
            selectNone = new ButtonType(unselectAllText, ButtonBar.ButtonData.LEFT);
        } catch (Exception e) {
            buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        }

        dialog.getDialogPane().getButtonTypes().addAll(selectAll, selectNone, buttonTypeOk, buttonTypeCancel);
        ButtonType finalButtonTypeOk = buttonTypeOk;
        ButtonType finalUnselectAll = selectNone;
        ButtonType finalSelectAll = selectAll;
        dialog.setResultConverter(b -> {
            if (b.equals(finalButtonTypeOk) ) {
                return tableView;
            }
            if (b.equals(finalUnselectAll)) {
                profileList.stream().forEach(p -> p.setSelected(false));
                dialog.showAndWait();
            }
            if (b.equals(finalSelectAll)) {
                profileList.stream().forEach(p -> p.setSelected(true));
                dialog.showAndWait();
            }
            return null;
        });

        dialog.setWidth(500);
        dialog.setHeight(500);
        Optional<TableView<ProfileToDisplay>> result = dialog.showAndWait();
        if (result.isPresent() && result.get() != null && result.get().getItems() != null && !result.get().getItems().isEmpty()) {
            List<ProfileToDisplay> selectedProfiles = new ArrayList<ProfileToDisplay>();
            for (ProfileToDisplay profileToDisplay : profileList) {
                Boolean isSelected = selectColumn.getCellData(profileToDisplay);
                if (isSelected != null && isSelected) {
                    selectedProfiles.add(profileToDisplay);
                }
            }
            return selectedProfiles;
        }
        return new ArrayList<ProfileToDisplay>();
    }

    public static List<PlatformProfileToDisplay> selectPlatformProfilesDialog(String headerText, List<PlatformProfileToDisplay> platformProfileToDisplayList, List<String> selectedProfileNameList) {
        Dialog<TableView<PlatformProfileToDisplay>> dialog = new Dialog<TableView<PlatformProfileToDisplay>>();
        PropertyService propertyService = new PropertyServiceImpl();
        String selectText = "";
        String profileNameText = "";
        String gameTypeText = "";
        String mapNameText = "";
        String difficultyText = "";
        String lengthText = "";

        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            selectText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.select");
            profileNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileName");
            gameTypeText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.gametype");
            mapNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
            difficultyText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.difficultyLowercase");
            lengthText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthLowercase");
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }
        dialog.setHeaderText(headerText);

        TableView<PlatformProfileToDisplay> tableView = new TableView<PlatformProfileToDisplay>();

        // First Column
        TableColumn<PlatformProfileToDisplay, Boolean> selectColumn = new TableColumn<PlatformProfileToDisplay, Boolean>();
        selectColumn.setText(selectText);
        selectColumn.setCellFactory(col -> new CheckBoxTableCell<>());
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setSortable(false);
        selectColumn.setEditable(true);
        selectColumn.setMinWidth(50);

        // Second Column
        ObservableList<String> comboBoxOptions = FXCollections.observableArrayList(
                Arrays.stream(EnumPlatform.values()).
                        map(EnumPlatform::getDescripcion).
                        collect(Collectors.toList())
        );

        TableColumn<PlatformProfileToDisplay, StringProperty> platformColumn = new TableColumn<PlatformProfileToDisplay, StringProperty>();
        platformColumn.setText("Platform");
        platformColumn.setMinWidth(120);
        platformColumn.setSortable(false);

        platformColumn.setCellFactory(col -> {
            TableCell<PlatformProfileToDisplay, StringProperty> c = new TableCell<>();
            final ComboBox<String> comboBox = new ComboBox<>(comboBoxOptions);
            c.itemProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue != null) {
                    comboBox.valueProperty().unbindBidirectional(oldValue);
                }
                if (newValue != null) {
                    comboBox.valueProperty().bindBidirectional(newValue);
                }
            });
            c.graphicProperty().bind(Bindings.when(c.emptyProperty()).then((Node) null).otherwise(comboBox));
            return c;
        });

        platformColumn.setCellValueFactory(cellData -> {
            return Bindings.createObjectBinding(() -> cellData.getValue().platformNameProperty());
        });
        platformColumn.setEditable(true);

        // Third Column
        TableColumn<PlatformProfileToDisplay, String> profileNameColumn = new TableColumn<PlatformProfileToDisplay, String>();
        profileNameColumn.setText(profileNameText);
        profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().profileNameProperty());
        profileNameColumn.setSortable(false);
        profileNameColumn.setEditable(false);
        profileNameColumn.setMinWidth(150);
        profileNameColumn.setStyle("-fx-alignment: CENTER;");

        // Fourth Column
        TableColumn<PlatformProfileToDisplay, String> gameTypeColumn = new TableColumn<PlatformProfileToDisplay, String>();
        gameTypeColumn.setText(gameTypeText);
        gameTypeColumn.setCellValueFactory(cellData -> cellData.getValue().gameTypeDescriptionProperty());
        gameTypeColumn.setSortable(false);
        gameTypeColumn.setEditable(false);
        gameTypeColumn.setMinWidth(150);
        gameTypeColumn.setStyle("-fx-alignment: CENTER;");

        // Fifth Column
        TableColumn<PlatformProfileToDisplay, String> mapNameColumn = new TableColumn<PlatformProfileToDisplay, String>();
        mapNameColumn.setText(mapNameText);
        mapNameColumn.setCellValueFactory(cellData -> cellData.getValue().mapNameProperty());
        mapNameColumn.setSortable(false);
        mapNameColumn.setEditable(false);
        mapNameColumn.setMinWidth(150);
        mapNameColumn.setStyle("-fx-alignment: CENTER;");

        // Sixth Column
        TableColumn<PlatformProfileToDisplay, String> difficultyColumn = new TableColumn<PlatformProfileToDisplay, String>();
        difficultyColumn.setText(difficultyText);
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyDescriptionProperty());
        difficultyColumn.setSortable(false);
        difficultyColumn.setEditable(false);
        difficultyColumn.setMinWidth(150);
        difficultyColumn.setStyle("-fx-alignment: CENTER;");

        // Seventh Column
        TableColumn<PlatformProfileToDisplay, String> lengthColumn = new TableColumn<PlatformProfileToDisplay, String>();
        lengthColumn.setText(lengthText);
        lengthColumn.setCellValueFactory(cellData -> cellData.getValue().lengthDescriptionProperty());
        lengthColumn.setSortable(false);
        lengthColumn.setEditable(false);
        lengthColumn.setMinWidth(150);
        lengthColumn.setStyle("-fx-alignment: CENTER;");

        tableView.getColumns().add(selectColumn);
        tableView.getColumns().add(profileNameColumn);
        tableView.getColumns().add(platformColumn);
        tableView.getColumns().add(gameTypeColumn);
        tableView.getColumns().add(mapNameColumn);
        tableView.getColumns().add(difficultyColumn);
        tableView.getColumns().add(lengthColumn);

        tableView.setItems(FXCollections.observableArrayList(platformProfileToDisplayList));
        boolean firstProfile = true;
        for (String selectedProfileName: selectedProfileNameList) {
            Optional<PlatformProfileToDisplay> selectedProfileOptional = platformProfileToDisplayList.stream().filter(p -> p.getProfileName().equals(selectedProfileName)).findFirst();
            if (selectedProfileOptional.isPresent()) {
                selectedProfileOptional.get().setSelected(true);
                selectColumn.getCellData(selectedProfileOptional.get());
                if (firstProfile) {
                    tableView.scrollTo(selectedProfileOptional.get());
                    firstProfile = false;
                }
            }
        }

        tableView.setEditable(true);

        dialog.getDialogPane().setContent(tableView);
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(800);
        dialog.getDialogPane().setMinHeight(400);

        ButtonType buttonTypeOk = null;
        ButtonType buttonTypeCancel = null;
        ButtonType selectAll = null;
        ButtonType selectNone = null;
        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String okText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.ok");
            buttonTypeOk = new ButtonType(okText, ButtonBar.ButtonData.OK_DONE);
            String cancelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.cancel");
            buttonTypeCancel = new ButtonType(cancelText, ButtonBar.ButtonData.CANCEL_CLOSE);
            String selectAllText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.selectMaps");
            selectAll = new ButtonType(selectAllText, ButtonBar.ButtonData.LEFT);
            String unselectAllText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unselectMaps");
            selectNone = new ButtonType(unselectAllText, ButtonBar.ButtonData.LEFT);
        } catch (Exception e) {
            buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        }

        dialog.getDialogPane().getButtonTypes().addAll(selectAll, selectNone, buttonTypeOk, buttonTypeCancel);
        ButtonType finalButtonTypeOk = buttonTypeOk;
        ButtonType finalUnselectAll = selectNone;
        ButtonType finalSelectAll = selectAll;
        dialog.setResultConverter(b -> {
            if (b.equals(finalButtonTypeOk) ) {
                return tableView;
            }
            if (b.equals(finalUnselectAll)) {
                platformProfileToDisplayList.stream().forEach(p -> p.setSelected(false));
                dialog.showAndWait();
            }
            if (b.equals(finalSelectAll)) {
                platformProfileToDisplayList.stream().forEach(p -> p.setSelected(true));
                dialog.showAndWait();
            }
            return null;
        });

        dialog.setWidth(500);
        dialog.setHeight(500);
        Optional<TableView<PlatformProfileToDisplay>> result = dialog.showAndWait();
        if (result.isPresent() && result.get() != null && result.get().getItems() != null && !result.get().getItems().isEmpty()) {
            List<PlatformProfileToDisplay> selectedProfiles = new ArrayList<PlatformProfileToDisplay>();
            for (PlatformProfileToDisplay platformProfileToDisplay : platformProfileToDisplayList) {
                Boolean isSelected = selectColumn.getCellData(platformProfileToDisplay);
                if (isSelected != null && isSelected) {
                    selectedProfiles.add(platformProfileToDisplay);
                }
            }
            return selectedProfiles;
        }
        return new ArrayList<PlatformProfileToDisplay>();
    }


    public static List<MapToDisplay> selectMapsDialog(String headerText, List<MapToDisplay> mapList) {
        if (mapList == null || mapList.isEmpty()) {
            return new ArrayList<MapToDisplay>();
        }

        Dialog<TableView<MapToDisplay>> dialog = new Dialog<TableView<MapToDisplay>>();
        PropertyService propertyService = new PropertyServiceImpl();
        String selectText = "";
        String workShopPageText = "";
        String commentaryText = "";

        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            selectText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.select");
            workShopPageText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.workShopPage");
            commentaryText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.commentary");
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
        } catch (Exception e) {
            dialog.setTitle("");
        }

        dialog.setHeaderText(headerText);
        TableView<MapToDisplay> tableView = new TableView<MapToDisplay>();

        // First Column
        TableColumn<MapToDisplay, Boolean> selectColumn = new TableColumn<MapToDisplay, Boolean>();
        selectColumn.setText(selectText);
        selectColumn.setCellFactory(col -> new CheckBoxTableCell<>());
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setSortable(false);
        selectColumn.setEditable(true);
        selectColumn.setMinWidth(50);

        // Second Column
        TableColumn<MapToDisplay, Hyperlink> idWorkShopColumn = new TableColumn<MapToDisplay, Hyperlink>();
        idWorkShopColumn.setText(workShopPageText);
        idWorkShopColumn.setCellFactory(new Callback<TableColumn<MapToDisplay, Hyperlink>, TableCell<MapToDisplay, Hyperlink>>() {
                                            @Override
                                            public TableCell<MapToDisplay, Hyperlink> call(TableColumn<MapToDisplay, Hyperlink> param) {
                                                TableCell<MapToDisplay, Hyperlink> cell = new TableCell<MapToDisplay, Hyperlink>() {
                                                    @Override
                                                    protected void updateItem(Hyperlink item, boolean empty) {
                                                        super.updateItem(item, empty);
                                                        setGraphic(empty ? null : item);
                                                        if (!empty){
                                                            item.setOnAction(e -> {
                                                                try {
                                                                    Desktop.getDesktop().browse(new URI(item.getText()));
                                                                } catch (Exception ex) {
                                                                    logger.error(ex.getMessage(), ex);
                                                                }
                                                            });
                                                        }
                                                    }
                                                };
                                                return cell;
                                            }
                                        }
        );
        idWorkShopColumn.setCellValueFactory(new PropertyValueFactory<>("workShopPage"));
        idWorkShopColumn.setSortable(false);
        idWorkShopColumn.setEditable(false);
        idWorkShopColumn.setMinWidth(480);

        // Third Column
        TableColumn<MapToDisplay, String> commentaryColumn = new TableColumn<MapToDisplay, String>();
        commentaryColumn.setText(commentaryText);
        commentaryColumn.setCellValueFactory(cellData -> cellData.getValue().commentaryProperty());
        commentaryColumn.setSortable(false);
        commentaryColumn.setEditable(false);
        commentaryColumn.setMinWidth(270);

        tableView.getColumns().add(selectColumn);
        tableView.getColumns().add(idWorkShopColumn);
        tableView.getColumns().add(commentaryColumn);
        tableView.setItems(FXCollections.observableArrayList(mapList));
        tableView.setEditable(true);

        dialog.getDialogPane().setContent(tableView);
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(800);
        dialog.getDialogPane().setMinHeight(400);

        ButtonType buttonTypeOk = null;
        ButtonType buttonTypeCancel = null;
        ButtonType selectNone = null;
        ButtonType selectAll = null;
        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String okText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.ok");
            buttonTypeOk = new ButtonType(okText, ButtonBar.ButtonData.OK_DONE);
            String cancelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.cancel");
            buttonTypeCancel = new ButtonType(cancelText, ButtonBar.ButtonData.CANCEL_CLOSE);
            String selectAllText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.selectMaps");
            selectAll = new ButtonType(selectAllText, ButtonBar.ButtonData.LEFT);
            String unselectAllText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.unselectMaps");
            selectNone = new ButtonType(unselectAllText, ButtonBar.ButtonData.LEFT);
        } catch (Exception e) {
            buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            selectAll = new ButtonType("Select all", ButtonBar.ButtonData.LEFT);
            selectNone = new ButtonType("Unselect all", ButtonBar.ButtonData.LEFT);
        }

        dialog.getDialogPane().getButtonTypes().addAll(selectAll, selectNone, buttonTypeOk, buttonTypeCancel);

        ButtonType finalButtonTypeOk = buttonTypeOk;
        ButtonType finalUnselectAll = selectNone;
        ButtonType finalSelectAll = selectAll;
        dialog.setResultConverter(b -> {
            if (b.equals(finalButtonTypeOk) ) {
                return tableView;
            }
            if (b.equals(finalUnselectAll)) {
                mapList.stream().forEach(m -> m.setSelected(false));
                dialog.showAndWait();
            }
            if (b.equals(finalSelectAll)) {
                mapList.stream().forEach(m -> m.setSelected(true));
                dialog.showAndWait();
            }
            return null;
        });

        dialog.setWidth(500);
        dialog.setHeight(500);
        Optional<TableView<MapToDisplay>> result = dialog.showAndWait();
        if (result.isPresent() && result.get() != null && result.get().getItems() != null && !result.get().getItems().isEmpty()) {
            List<MapToDisplay> selectedMaps = new ArrayList<MapToDisplay>();
            for (MapToDisplay mapToDisplay : mapList) {
                Boolean isSelected = selectColumn.getCellData(mapToDisplay);
                if (isSelected != null && isSelected) {
                    selectedMaps.add(mapToDisplay);
                }
            }
            return selectedMaps;
        }
        return new ArrayList<MapToDisplay>();
    }

    public static Optional<PlatformProfileToDisplay> selectProfileDialog(String headerText, List<PlatformProfileToDisplay> platformProfileToDisplayList, List<String> selectedProfileNameList) {
        Dialog<TableView<PlatformProfileToDisplay>> dialog = new Dialog<TableView<PlatformProfileToDisplay>>();
        PropertyService propertyService = new PropertyServiceImpl();
        String profileNameText = "";
        String gameTypeText = "";
        String mapNameText = "";
        String difficultyText = "";
        String lengthText = "";

        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            profileNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileName");
            gameTypeText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.gametype");
            mapNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
            difficultyText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.difficultyLowercase");
            lengthText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthLowercase");
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }
        dialog.setHeaderText(headerText);

        TableView<PlatformProfileToDisplay> tableView = new TableView<PlatformProfileToDisplay>();

        // First Column
        TableColumn<PlatformProfileToDisplay, String> profileNameColumn = new TableColumn<PlatformProfileToDisplay, String>();
        profileNameColumn.setText(profileNameText);
        profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().profileNameProperty());
        profileNameColumn.setSortable(false);
        profileNameColumn.setMinWidth(150);

        // Second Column
        TableColumn<PlatformProfileToDisplay, String> gameTypeColumn = new TableColumn<PlatformProfileToDisplay, String>();
        gameTypeColumn.setText(gameTypeText);
        gameTypeColumn.setCellValueFactory(cellData -> cellData.getValue().gameTypeDescriptionProperty());
        gameTypeColumn.setSortable(false);
        gameTypeColumn.setMinWidth(150);

        // Third Column
        TableColumn<PlatformProfileToDisplay, String> mapNameColumn = new TableColumn<PlatformProfileToDisplay, String>();
        mapNameColumn.setText(mapNameText);
        mapNameColumn.setCellValueFactory(cellData -> cellData.getValue().mapNameProperty());
        mapNameColumn.setSortable(false);
        mapNameColumn.setMinWidth(150);

        // Fourth Column
        TableColumn<PlatformProfileToDisplay, String> difficultyColumn = new TableColumn<PlatformProfileToDisplay, String>();
        difficultyColumn.setText(difficultyText);
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyDescriptionProperty());
        difficultyColumn.setSortable(false);
        difficultyColumn.setMinWidth(150);

        // Fifth Column
        TableColumn<PlatformProfileToDisplay, String> lengthColumn = new TableColumn<PlatformProfileToDisplay, String>();
        lengthColumn.setText(lengthText);
        lengthColumn.setCellValueFactory(cellData -> cellData.getValue().lengthDescriptionProperty());
        lengthColumn.setSortable(false);
        lengthColumn.setMinWidth(150);

        tableView.getColumns().add(profileNameColumn);
        tableView.getColumns().add(gameTypeColumn);
        tableView.getColumns().add(mapNameColumn);
        tableView.getColumns().add(difficultyColumn);
        tableView.getColumns().add(lengthColumn);

        tableView.setItems(FXCollections.observableArrayList(platformProfileToDisplayList));
        tableView.setEditable(false);

        boolean firstProfile = true;
        for (String selectedProfileName: selectedProfileNameList) {
            Optional<PlatformProfileToDisplay> selectedProfileOptional = platformProfileToDisplayList.stream().filter(p -> p.getProfileName().equals(selectedProfileName)).findFirst();
            if (selectedProfileOptional.isPresent()) {
                tableView.getSelectionModel().select(selectedProfileOptional.get());
                if (firstProfile) {
                    tableView.scrollTo(selectedProfileOptional.get());
                    firstProfile = false;
                }
            }
        }

        dialog.getDialogPane().setContent(tableView);
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(750);
        dialog.getDialogPane().setMinHeight(400);

        ButtonType buttonTypeOk = null;
        ButtonType buttonTypeCancel = null;
        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String okText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.ok");
            buttonTypeOk = new ButtonType(okText, ButtonBar.ButtonData.OK_DONE);
            String cancelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.cancel");
            buttonTypeCancel = new ButtonType(cancelText, ButtonBar.ButtonData.CANCEL_CLOSE);
        } catch (Exception e) {
            buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        }

        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        ButtonType finalButtonTypeOk = buttonTypeOk;
        dialog.setResultConverter(new Callback<ButtonType, TableView<PlatformProfileToDisplay>>() {
            @Override
            public TableView call(ButtonType b) {
                if (b == finalButtonTypeOk) {
                    return tableView;
                }
                return null;
            }
        });

        dialog.setWidth(500);
        dialog.setHeight(500);
        Optional<TableView<PlatformProfileToDisplay>> result = dialog.showAndWait();
        if (result.isPresent()) {
            return Optional.ofNullable(result.get().getSelectionModel().getSelectedItem());
        }
        return Optional.empty();
    }

    public static String getPublicIp() throws Exception {
        URL myIpCom = new URL("https://api.myip.com");;
        BufferedReader reader = new BufferedReader(new InputStreamReader(myIpCom.openStream()));
        String publicIP = "127.0.0.1";
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("ip")) {
                String[] array = line.split(",");
                String[] arrayTwo = array[0].split(":");
                publicIP = arrayTwo[1].replaceAll("\"", "");
                break;
            }
        }
        reader.close();
        return publicIP;
    }

    public static String encryptAES(String password) throws Exception {
        if (StringUtils.isBlank(password)) {
            return "";
        }
        PropertyService propertyService = new PropertyServiceImpl();
        String aesEncryptionKey = propertyService.getPropertyValue("properties/config.properties", "prop.config.aesEncryptionKey");
        Key aesKey = new SecretKeySpec(aesEncryptionKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(password.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decryptAES(String encryptedPassword) throws Exception {
        if (StringUtils.isBlank(encryptedPassword)) {
            return "";
        }
        PropertyService propertyService = new PropertyServiceImpl();
        String aesEncryptionKey = propertyService.getPropertyValue("properties/config.properties", "prop.config.aesEncryptionKey");
        Key aesKey = new SecretKeySpec(aesEncryptionKey.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decrypted = Base64.getDecoder().decode(encryptedPassword);
        byte[] decryptedValue = cipher.doFinal(decrypted);
        return new String(decryptedValue);
    }

    public static File downloadImageFromUrlToFile(String strUrlImage, String targetFolder, String fileName) throws IOException {
        URL urlImage = new URL(strUrlImage);
        URLConnection connection = urlImage.openConnection();
        String mimeType = connection.getContentType();
        String[] array = mimeType.split("/");
        String fileExtension = array[1];
        String localUrlMapImage = targetFolder + "/" + fileName + "." + fileExtension;
        File file = new File(localUrlMapImage);
        FileUtils.copyURLToFile(urlImage,file);
        return file;
    }

    public static File downloadImageFromUrlToFile(String strUrlImage, String pathToFile) throws IOException {
        URL urlImage = new URL(strUrlImage);
        File file = new File(pathToFile);
        FileUtils.copyURLToFile(urlImage,file);
        return file;
    }

    public static Optional<VBox> importMapsResultDialog(List<ImportMapResultToDisplay> importMapResultToDisplayList, String profileName) {

        Dialog<VBox> dialog = new Dialog<VBox>();

        String languageCode = StringUtils.EMPTY;
        String importMapsFromServerText = StringUtils.EMPTY;
        String platformText = StringUtils.EMPTY;
        String profileText = StringUtils.EMPTY;
        String importSuccessText = StringUtils.EMPTY;
        String categoryText = StringUtils.EMPTY;
        String mapNameText = StringUtils.EMPTY;
        String importedDateText = StringUtils.EMPTY;
        String failedImportText = StringUtils.EMPTY;
        String errorMessageText = StringUtils.EMPTY;
        String previousProfileText = StringUtils.EMPTY;
        String nextProfileText = StringUtils.EMPTY;
        String closeText = StringUtils.EMPTY;

        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            importMapsFromServerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importMaps");
            profileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profile");
            platformText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.platform");
            importSuccessText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importSuccess");
            categoryText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.category");
            mapNameText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
            importedDateText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importedDate");
            failedImportText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importFailed");
            errorMessageText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.errorMessage");
            previousProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.nextProfile");
            nextProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.previousProfile");
            closeText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.close");

            dialog.setTitle(applicationTitle);
            dialog.setHeaderText(importMapsFromServerText);
        } catch (Exception ex) {
            dialog.setTitle("");
        }

        Label profileNameLabel = new Label(profileText + " " + profileName.toUpperCase());
        profileNameLabel.setAlignment(Pos.CENTER);
        profileNameLabel.setMaxWidth(Double.MAX_VALUE);
        profileNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");

        Label successImportationLabel = new Label(importSuccessText);
        successImportationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");

        TableView<ImportMapResultToDisplay> successImportationTable = new TableView<ImportMapResultToDisplay>();
        successImportationTable.setPrefHeight(300);

        TableColumn<ImportMapResultToDisplay, String> successCategory = new TableColumn<ImportMapResultToDisplay, String>();
        successCategory.setText(categoryText);
        successCategory.setCellValueFactory(cellData -> cellData.getValue().isOfficialProperty());
        successCategory.setSortable(true);
        successCategory.setStyle("-fx-alignment: CENTER;");
        successCategory.setMinWidth(150);

        TableColumn<ImportMapResultToDisplay, String> successPlatform = new TableColumn<ImportMapResultToDisplay, String>();
        successPlatform.setText(platformText);
        successPlatform.setCellValueFactory(cellData -> cellData.getValue().platformNameProperty());
        successPlatform.setSortable(true);
        successPlatform.setStyle("-fx-alignment: CENTER;");
        successPlatform.setMinWidth(150);

        TableColumn<ImportMapResultToDisplay, String> successMapName = new TableColumn<ImportMapResultToDisplay, String>();
        successMapName.setText(mapNameText);
        successMapName.setCellValueFactory(cellData -> cellData.getValue().mapNameProperty());
        successMapName.setSortable(true);
        successMapName.setMinWidth(450);

        TableColumn<ImportMapResultToDisplay, String> successImportedDate = new TableColumn<ImportMapResultToDisplay, String>();
        successImportedDate.setText(importedDateText);
        successImportedDate.setCellValueFactory(cellData -> cellData.getValue().importedDateProperty());
        successImportedDate.setSortable(true);
        successImportedDate.setStyle("-fx-alignment: CENTER;");
        successImportedDate.setMinWidth(200);

        successImportationTable.getColumns().add(successCategory);
        successImportationTable.getColumns().add(successPlatform);
        successImportationTable.getColumns().add(successMapName);
        successImportationTable.getColumns().add(successImportedDate);
        successImportationTable.setItems(FXCollections.observableArrayList(
                importMapResultToDisplayList.stream().
                        filter(imr -> imr.getProfileName().equals(profileName)).
                        filter(imr -> imr.getImportedDate() != null).
                        collect(Collectors.toList())
        ));
        successImportationTable.setEditable(false);

        Label failedImportationLabel = new Label(failedImportText);
        failedImportationLabel.setPadding(new Insets(20,0,0,0));
        failedImportationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");

        TableView<ImportMapResultToDisplay> failedImportationTable = new TableView<ImportMapResultToDisplay>();
        failedImportationTable.setPrefHeight(200);

        TableColumn<ImportMapResultToDisplay, String> failedCategory = new TableColumn<ImportMapResultToDisplay, String>();
        failedCategory.setText(categoryText);
        failedCategory.setCellValueFactory(cellData -> cellData.getValue().isOfficialProperty());
        failedCategory.setSortable(true);
        failedCategory.setMinWidth(100);

        TableColumn<ImportMapResultToDisplay, String> failedPlatform = new TableColumn<ImportMapResultToDisplay, String>();
        failedPlatform.setText(platformText);
        failedPlatform.setCellValueFactory(cellData -> cellData.getValue().platformNameProperty());
        failedPlatform.setSortable(true);
        failedPlatform.setMinWidth(100);

        TableColumn<ImportMapResultToDisplay, String> failedMapName = new TableColumn<ImportMapResultToDisplay, String>();
        failedMapName.setText(mapNameText);
        failedMapName.setCellValueFactory(cellData -> cellData.getValue().mapNameProperty());
        failedMapName.setSortable(true);
        failedMapName.setMinWidth(300);

        TableColumn<ImportMapResultToDisplay, String> failedErrorMessage = new TableColumn<ImportMapResultToDisplay, String>();
        failedErrorMessage.setText(errorMessageText);
        failedErrorMessage.setCellValueFactory(cellData -> cellData.getValue().errorMessageProperty());
        failedErrorMessage.setSortable(true);
        failedErrorMessage.setMinWidth(450);

        failedImportationTable.getColumns().add(failedCategory);
        failedImportationTable.getColumns().add(failedPlatform);
        failedImportationTable.getColumns().add(failedMapName);
        failedImportationTable.getColumns().add(failedErrorMessage);
        failedImportationTable.setItems(FXCollections.observableArrayList(
                importMapResultToDisplayList.stream().
                        filter(imr -> imr.getProfileName().equals(profileName)).
                        filter(imr -> imr.getImportedDate() == null).
                        collect(Collectors.toList())
        ));
        failedImportationTable.setEditable(false);

        Text action = new Text(StringUtils.EMPTY);
        action.setVisible(false);

        VBox vbox = new VBox(profileNameLabel, successImportationLabel, successImportationTable, failedImportationLabel, failedImportationTable, action);

        dialog.getDialogPane().setContent(vbox);
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(800);
        dialog.getDialogPane().setMinHeight(500);

        ButtonType previousButton = new ButtonType(previousProfileText, ButtonBar.ButtonData.BACK_PREVIOUS);
        ButtonType nextButton = new ButtonType(nextProfileText, ButtonBar.ButtonData.NEXT_FORWARD);
        ButtonType closeButton = new ButtonType(closeText, ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(previousButton, nextButton, closeButton);

        dialog.setResultConverter(new Callback<ButtonType, VBox>() {
            @Override
            public VBox call(ButtonType b) {
                if (b.equals(closeButton)) {
                    return null;
                }
                if (b.equals(previousButton)) {
                    action.setText("PREVIOUS");
                    return vbox;
                }
                if (b.equals(nextButton)) {
                    action.setText("NEXT");
                    return vbox;
                }
                return null;
            }
        });

        dialog.setWidth(500);
        dialog.setHeight(500);
        return dialog.showAndWait();
    }

    public static boolean upgradeLauncher(String languageCode, boolean isInStartup) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationVersion = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationVersion");

            String lastPublishedVersion = getLastPublishedVersion();

            String newPublishedVersionText = StringUtils.EMPTY;
            if (applicationVersion.compareTo(lastPublishedVersion) < 0) {
                newPublishedVersionText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.message.newPublishedVersion");
            } else {
                if (isInStartup) {
                    return false;
                }
                newPublishedVersionText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.message.notNewPublishedVersion");;
            }

            String actualVersionText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.message.actualVersion");
            String newestPublishedVersionText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.message.publishedversion");
            String upgradeAppText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.message.upgradeApp");
            String updateButtonText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties","prop.message.updateButton");

            Optional<UpdateLauncher> checkForUpgradeOptional = Utils.updateDialog(newPublishedVersionText,
                    actualVersionText + ": " + applicationVersion +
                            "\n" + newestPublishedVersionText + ": " + lastPublishedVersion +
                            "\n\n" + upgradeAppText + ".\n\n" +
                            updateButtonText + "."
            );

            if (checkForUpgradeOptional.isPresent() && !checkForUpgradeOptional.get().isDontShowAtStartup()) {
                propertyService.setProperty("properties/config.properties", "prop.config.checkForUpgrades", "true");
            }
            if (checkForUpgradeOptional.isPresent() && checkForUpgradeOptional.get().isDontShowAtStartup()) {
                propertyService.setProperty("properties/config.properties", "prop.config.checkForUpgrades", "false");
            }

            return checkForUpgradeOptional.map(UpdateLauncher::isUpdate).orElse(false);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    private static String getLastPublishedVersion() throws Exception {

        PropertyService propertyService = new PropertyServiceImpl();
        String releasePageGithubUrl = propertyService.getPropertyValue("properties/config.properties", "prop.config.releasePageGithubUrl");
        String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");

        org.jsoup.nodes.Document doc = Jsoup.connect(releasePageGithubUrl).get();

        Elements linkList = doc.getElementsByTag("a");
        if (linkList != null && linkList.size() > 0) {
            for (int i = 0; i < linkList.size(); i++) {
                org.jsoup.nodes.Element link = linkList.get(i);
                if (link.hasText() && link.text().contains(applicationTitle + " v2.")) {
                    String nameAndVersion = link.text();
                    String[] versionArray = nameAndVersion.split("v2.");
                    return "2." + versionArray[1].replaceAll("_",  " ");
                }
            }
        }
        return StringUtils.EMPTY;
    }

    public static Optional<Integer> renderTipMarkDown(Integer actualTipNumber, Integer maxNumberOfTips, Boolean dontShowTipsOnStartup) {
        Dialog<Integer> dialog = new Dialog<Integer>();
        PropertyService propertyService = new PropertyServiceImpl();
        try {
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }

        InputStream markdownContentIS = null;
        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            markdownContentIS = new URL("https://raw.githubusercontent.com/cesar-rgon/simple-kf2-server-launcher/master/tips/" + languageCode + "/tip" + actualTipNumber + ".md").openStream();
            String markdownContent = IOUtils.toString(markdownContentIS, StandardCharsets.UTF_8);

            MutableDataSet options = new MutableDataSet();
            Parser parser = Parser.builder(options).build();
            HtmlRenderer renderer = HtmlRenderer.builder(options).build();
            com.vladsch.flexmark.util.ast.Node document = parser.parse(markdownContent);

            String htmlTemplate = "<html><head><title>Tip " + actualTipNumber + "</title></head><body style='background-color:black;color:white;'><img src='https://raw.githubusercontent.com/cesar-rgon/simple-kf2-server-launcher/master/doc/images/kf2banner.png' alt='Simple KF2 Server Launcher logo'><div style='font-size:18px;'>CONTENT</div></body></html>";
            String outputHtml = htmlTemplate.replace("CONTENT", renderer.render(document)).replace("<h1>", "<h1 style='color:gold'>").replace("<img ", "<img width='800' ").replace("<a ", "<a style='color:#f03830;' ");

            WebView webView = new WebView();
            webView.getEngine().loadContent(outputHtml);

            CheckBox showAtStartupCheckBox = new CheckBox();
            dialog.setDialogPane(new DialogPane() {
                @Override
                protected Node createDetailsButton() {
                    try {
                        showAtStartupCheckBox.setText(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notShowAtStartup"));
                    } catch (Exception e) {
                        showAtStartupCheckBox.setText("Do not show at startup");
                    }
                    if (dontShowTipsOnStartup) {
                        showAtStartupCheckBox.setSelected(true);
                    } else {
                        showAtStartupCheckBox.setSelected(false);
                    }
                    return showAtStartupCheckBox;
                }
            });

            dialog.getDialogPane().setExpandableContent(new Group());
            dialog.getDialogPane().setExpanded(false);

            dialog.getDialogPane().setContent(webView);;
            dialog.getDialogPane().setMinWidth(800);
            dialog.getDialogPane().setMinHeight(600);

            ButtonType previousButton = new ButtonType("Previous tip", ButtonBar.ButtonData.BACK_PREVIOUS);
            ButtonType nextButton = new ButtonType("Next tip", ButtonBar.ButtonData.NEXT_FORWARD);
            ButtonType closeButton = new ButtonType("Ok", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(previousButton, nextButton, closeButton);
            dialog.setResizable(true);

            dialog.setResultConverter(new Callback<ButtonType, Integer>() {
                @Override
                public Integer call(ButtonType b) {
                    if (b.equals(closeButton)) {
                        if (showAtStartupCheckBox.isSelected()) {
                            return 0;
                        }
                        return -1;
                    }
                    if (b.equals(previousButton)) {
                        if (actualTipNumber > 1) {
                            return actualTipNumber - 1;
                        } else {
                            return maxNumberOfTips;
                        }
                    }
                    if (b.equals(nextButton)) {
                        if (actualTipNumber < maxNumberOfTips) {
                            return actualTipNumber + 1;
                        } else {
                            return 1;
                        }
                    }
                    return null;
                }
            });

            dialog.setWidth(800);
            dialog.setHeight(600);
            return dialog.showAndWait();

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(markdownContentIS);
        }

        return Optional.empty();
    }

    public static void showTipsOnStasrtup() {
        PropertyService propertyService = new PropertyServiceImpl();
        InputStream maxNumberOfTipsIS = null;
        try {
            maxNumberOfTipsIS = new URL("https://raw.githubusercontent.com/cesar-rgon/simple-kf2-server-launcher/master/tips/lastTip.txt").openStream();
            Integer maxNumberOfTips = Integer.parseInt(IOUtils.toString(maxNumberOfTipsIS, StandardCharsets.UTF_8));

            Boolean dontShowTipsOnStartup = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.dontShowTipsOnStartup"));

            if (!dontShowTipsOnStartup && maxNumberOfTips > 0) {
                Optional<Integer> actualTipNumber = Optional.ofNullable(maxNumberOfTips);
                if (actualTipNumber.isPresent()) {
                    do {
                        actualTipNumber = renderTipMarkDown(actualTipNumber.get(), maxNumberOfTips, dontShowTipsOnStartup);
                    } while (actualTipNumber.isPresent() && actualTipNumber.get() > 0);
                }
                if (actualTipNumber.isPresent() && actualTipNumber.get() == 0) {
                    propertyService.setProperty("properties/config.properties", "prop.config.dontShowTipsOnStartup", "true");
                }
                if (actualTipNumber.isPresent() && actualTipNumber.get() == -1) {
                    propertyService.setProperty("properties/config.properties", "prop.config.dontShowTipsOnStartup", "false");
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(maxNumberOfTipsIS);
        }
    }

    public static void loadTooltip(String languageCode, String propKey, Button button) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        button.setTooltip(tooltip);
    }


    public static void loadTooltip(String languageCode, String propKey, ImageView img, Label label, ComboBox<?> combo) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        if (label != null) {
            label.setTooltip(tooltip);
        }
        combo.setTooltip(tooltip);
    }

    public static void loadTooltip(String languageCode, String propKey, ImageView img, Label label, TextField textField) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        textField.setTooltip(tooltip);
    }

    public static void loadTooltip(String languageCode, String propKey, ImageView img, Label label, TextField[] textFieldArray) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        for (int i=0; i<textFieldArray.length; i++) {
            textFieldArray[i].setTooltip(tooltip);
        }
    }

    public static void loadTooltip(String languageCode, String propKey, ImageView img, Label label, TextField textField1, TextField textField2) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        textField1.setTooltip(tooltip);
        textField2.setTooltip(tooltip);
    }

    public static void loadTooltip(String languageCode, String propKey, ImageView img, Label label, CheckBox checkBox, TextField textField) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        checkBox.setTooltip(tooltip);
        textField.setTooltip(tooltip);
    }

    public static void loadTooltip(String languageCode, String propKey, ImageView img, Label label, CheckBox checkBox) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        checkBox.setTooltip(tooltip);
    }

    public static void loadTooltip(String languageCode, String propKey, ImageView img, Label label, TextArea textArea) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        textArea.setTooltip(tooltip);
    }

    public static void loadTooltip(String languageCode, String propKey, Label label, TextArea textArea) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        Double tooltipDuration = Double.parseDouble(
                propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        label.setTooltip(tooltip);
        textArea.setTooltip(tooltip);
    }

    public static void convertPngToJpg(File sourcePngFile, File targetJpgFile) throws Exception {
        BufferedImage originalImage = ImageIO.read(sourcePngFile);
        BufferedImage newBufferedImage = new BufferedImage(
                originalImage.getWidth(),
                originalImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        newBufferedImage.createGraphics()
                .drawImage(originalImage,
                        0,
                        0,
                        Color.BLACK,
                        null);

        ImageIO.write(newBufferedImage, "jpg", targetJpgFile);
    }

    public static String normalizeMapName(String mapName) {
        if (StringUtils.isBlank(mapName)) {
            return StringUtils.EMPTY;
        }

        StringBuilder result = new StringBuilder();
        if (!mapName.toUpperCase().startsWith("KF")) {
            result.append("KF-");
        }
        result.append(mapName.replaceAll(" ", "_"));

        return result.toString();
    }

    public static File downloadZipFileFromGithub(String url) throws Exception {
        String tagStr = "https://github.com/cesar-rgon/simple-kf2-server-launcher/releases/tag/";
        String assetsStr = "https://github.com/cesar-rgon/simple-kf2-server-launcher/releases/expanded_assets/";

        org.jsoup.nodes.Document releasesDoc = Jsoup.connect(url).get();
        Elements releasesLinkList = releasesDoc.getElementsByTag("a");
        Optional<String> baseUriOptional = releasesLinkList.stream().filter(l -> l.baseUri().contains(tagStr)).map(l -> l.baseUri()).findFirst();
        String assetsUri = StringUtils.EMPTY;
        if (baseUriOptional.isPresent()) {
            String tagVersion = baseUriOptional.get().substring(tagStr.length());
            assetsUri = assetsStr + tagVersion;
        }

        String text = StringUtils.EMPTY;
        String uri = StringUtils.EMPTY;
        if (StringUtils.isNotBlank(assetsUri)) {
            org.jsoup.nodes.Document doc = Jsoup.connect(assetsUri).get();
            Elements linkList = doc.getElementsByTag("a");

            String os = System.getProperty("os.name");
            String osStr = StringUtils.EMPTY;
            if (StringUtils.isNotEmpty(os) && os.contains("Linux")) {
                osStr = "linux";
            } else {
                osStr = "windows";
            }

            String finalOsStr = osStr;
            Optional<org.jsoup.nodes.Element> linkOptional = linkList.stream().filter(l -> l.attributes().get("href").contains(finalOsStr + ".zip")).findFirst();
            if (linkOptional.isPresent()) {
                text = linkOptional.get().text();
                uri = "https://github.com" + linkOptional.get().attributes().get("href");
            }
        }

        if (StringUtils.isNotBlank(text) && StringUtils.isNotBlank(uri)) {
            String tempFolder = System.getProperty("java.io.tmpdir");
            File tempZipFile = new File(tempFolder + "/" + text);
            PropertyService propertyService = new PropertyServiceImpl();
            String downloadConnectionTimeOut = propertyService.getPropertyValue("properties/config.properties", "prop.config.downloadConnectionTimeout");
            String downloadReadTimeOut = propertyService.getPropertyValue("properties/config.properties", "prop.config.downloadReadTimeout");

            FileUtils.copyURLToFile(
                    new URL(uri),
                    tempZipFile,
                    Integer.parseInt(downloadConnectionTimeOut),
                    Integer.parseInt(downloadReadTimeOut)
            );
            return tempZipFile;
        }
        return null;
    }

    public static void uncompressZipFile(File file, File targetFolder) throws Exception {
        ZipFile zipFile = new ZipFile(file.getAbsolutePath());
        zipFile.extractAll(targetFolder.getAbsolutePath());
    }

    public static String removeSpacesAccents(String text) {
        return text.
            replaceAll(" ", "_").
            replaceAll("á", "a").
            replaceAll("é", "e").
            replaceAll("í", "i").
            replaceAll("ó", "o").
            replaceAll("ú", "u").
            replaceAll("ñ", "n").
            replaceAll("Á", "A").
            replaceAll("É", "E").
            replaceAll("Í", "I").
            replaceAll("Ó", "O").
            replaceAll("Ú", "U").
            replaceAll("Ñ", "N");
    }
}
