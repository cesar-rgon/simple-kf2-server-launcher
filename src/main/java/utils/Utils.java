package utils;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import pojos.AddMapsToProfile;
import pojos.ImportMapResultToDisplay;
import pojos.MapToDisplay;
import pojos.ProfileToDisplay;
import services.PropertyService;
import services.PropertyServiceImpl;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Utils {

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
        alert.showAndWait();
    }


    public static List<AddMapsToProfile> defineCustomMapsToAddPerProfile(String headerText, ObservableList<ProfileDto> profileList) {
        Dialog<TableView<AddMapsToProfile>> dialog = new Dialog<TableView<AddMapsToProfile>>();
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

        TableView<AddMapsToProfile> tableView = new TableView<AddMapsToProfile>();
        // First Column
        TableColumn<AddMapsToProfile, String> profileNameColumn = new TableColumn<AddMapsToProfile, String>();
        profileNameColumn.setText(profileNameText);
        profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().profileNameProperty());
        profileNameColumn.setSortable(false);
        profileNameColumn.setEditable(false);
        profileNameColumn.setMinWidth(150);

        // Second Column
        TableColumn<AddMapsToProfile, String> customMapsColumn = new TableColumn<AddMapsToProfile, String>();
        customMapsColumn.setText(defineMapsToAddText);
        customMapsColumn.setMinWidth(500);
        customMapsColumn.setSortable(false);
        customMapsColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        customMapsColumn.setCellValueFactory(cellData -> cellData.getValue().mapListProperty());
        customMapsColumn.setEditable(true);

        tableView.getColumns().add(profileNameColumn);
        tableView.getColumns().add(customMapsColumn);

        if (profileList != null && !profileList.isEmpty()) {
            for (ProfileDto profile: profileList) {
                tableView.getItems().add(new AddMapsToProfile(profile.getName()));
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
        dialog.setResultConverter(new Callback<ButtonType, TableView<AddMapsToProfile>>() {
            @Override
            public TableView call(ButtonType b) {
                if (b == finalButtonTypeOk) {
                    return tableView;
                }
                return null;
            }
        });

        Optional<TableView<AddMapsToProfile>> result = dialog.showAndWait();
        if (result.isPresent() && result.get() != null && result.get().getItems() != null && !result.get().getItems().isEmpty()) {
            return result.get().getItems();
        }
        return new ArrayList<AddMapsToProfile>();
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
                                                        setGraphic(item);
                                                        if (!empty){
                                                            item.setOnAction(e -> {
                                                                try {
                                                                    Desktop.getDesktop().browse(new URI(item.getText()));
                                                                } catch (Exception ex) {
                                                                    ex.printStackTrace();
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
        TableColumn<MapToDisplay, String> mapNameColumn = new TableColumn<MapToDisplay, String>();
        mapNameColumn.setText(commentaryText);
        mapNameColumn.setCellValueFactory(cellData -> cellData.getValue().commentaryProperty());
        mapNameColumn.setSortable(false);
        mapNameColumn.setEditable(false);
        mapNameColumn.setMinWidth(270);

        tableView.getColumns().add(selectColumn);
        tableView.getColumns().add(idWorkShopColumn);
        tableView.getColumns().add(mapNameColumn);
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

    public static Optional<ProfileToDisplay> selectProfileDialog(String headerText, List<ProfileToDisplay> profileList) {
        Dialog<TableView<ProfileToDisplay>> dialog = new Dialog<TableView<ProfileToDisplay>>();
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

        TableView<ProfileToDisplay> tableView = new TableView<ProfileToDisplay>();

        // First Column
        TableColumn<ProfileToDisplay, String> profileNameColumn = new TableColumn<ProfileToDisplay, String>();
        profileNameColumn.setText(profileNameText);
        profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().profileNameProperty());
        profileNameColumn.setSortable(false);
        profileNameColumn.setMinWidth(150);

        // Second Column
        TableColumn<ProfileToDisplay, String> gameTypeColumn = new TableColumn<ProfileToDisplay, String>();
        gameTypeColumn.setText(gameTypeText);
        gameTypeColumn.setCellValueFactory(cellData -> cellData.getValue().gameTypeDescriptionProperty());
        gameTypeColumn.setSortable(false);
        gameTypeColumn.setMinWidth(150);

        // Third Column
        TableColumn<ProfileToDisplay, String> mapNameColumn = new TableColumn<ProfileToDisplay, String>();
        mapNameColumn.setText(mapNameText);
        mapNameColumn.setCellValueFactory(cellData -> cellData.getValue().mapNameProperty());
        mapNameColumn.setSortable(false);
        mapNameColumn.setMinWidth(150);

        // Fourth Column
        TableColumn<ProfileToDisplay, String> difficultyColumn = new TableColumn<ProfileToDisplay, String>();
        difficultyColumn.setText(difficultyText);
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyDescriptionProperty());
        difficultyColumn.setSortable(false);
        difficultyColumn.setMinWidth(150);

        // Fifth Column
        TableColumn<ProfileToDisplay, String> lengthColumn = new TableColumn<ProfileToDisplay, String>();
        lengthColumn.setText(lengthText);
        lengthColumn.setCellValueFactory(cellData -> cellData.getValue().lengthDescriptionProperty());
        lengthColumn.setSortable(false);
        lengthColumn.setMinWidth(150);

        tableView.getColumns().add(profileNameColumn);
        tableView.getColumns().add(gameTypeColumn);
        tableView.getColumns().add(mapNameColumn);
        tableView.getColumns().add(difficultyColumn);
        tableView.getColumns().add(lengthColumn);
        tableView.setItems(FXCollections.observableArrayList(profileList));
        tableView.setEditable(false);
        Optional<ProfileToDisplay> selectedProfile = profileList.stream().filter(p -> p.isSelected()).findFirst();
        if (selectedProfile.isPresent()) {
            tableView.getSelectionModel().select(selectedProfile.get());
            tableView.scrollTo(selectedProfile.get());
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
        dialog.setResultConverter(new Callback<ButtonType, TableView<ProfileToDisplay>>() {
            @Override
            public TableView call(ButtonType b) {
                if (b == finalButtonTypeOk) {
                    return tableView;
                }
                return null;
            }
        });

        Optional<TableView<ProfileToDisplay>> result = dialog.showAndWait();
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
        dialog.setTitle("Simple Killing Floor 2 Server Launcher");
        dialog.setHeaderText("Import maps from server to launcher");

        Label profileNameLabel = new Label("Profile " + profileName.toUpperCase());
        profileNameLabel.setAlignment(Pos.CENTER);
        profileNameLabel.setMaxWidth(Double.MAX_VALUE);
        profileNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px");

        Label successImportationLabel = new Label("Success importation");
        successImportationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");

        TableView<ImportMapResultToDisplay> successImportationTable = new TableView<ImportMapResultToDisplay>();
        successImportationTable.setPrefHeight(300);

        TableColumn<ImportMapResultToDisplay, String> successCategory = new TableColumn<ImportMapResultToDisplay, String>();
        successCategory.setText("Category");
        successCategory.setCellValueFactory(cellData -> cellData.getValue().isOfficialProperty());
        successCategory.setSortable(true);
        successCategory.setStyle("-fx-alignment: CENTER;");
        successCategory.setMinWidth(150);

        TableColumn<ImportMapResultToDisplay, String> successMapName = new TableColumn<ImportMapResultToDisplay, String>();
        successMapName.setText("Map name");
        successMapName.setCellValueFactory(cellData -> cellData.getValue().mapNameProperty());
        successMapName.setSortable(true);
        successMapName.setMinWidth(450);

        TableColumn<ImportMapResultToDisplay, String> successImportedDate = new TableColumn<ImportMapResultToDisplay, String>();
        successImportedDate.setText("Imported date");
        successImportedDate.setCellValueFactory(cellData -> cellData.getValue().importedDateProperty());
        successImportedDate.setSortable(true);
        successImportedDate.setStyle("-fx-alignment: CENTER;");
        successImportedDate.setMinWidth(200);

        successImportationTable.getColumns().add(successCategory);
        successImportationTable.getColumns().add(successMapName);
        successImportationTable.getColumns().add(successImportedDate);
        successImportationTable.setItems(FXCollections.observableArrayList(
                importMapResultToDisplayList.stream().
                        filter(imr -> imr.getProfileName().equals(profileName)).
                        filter(imr -> imr.getImportedDate() != null).
                        collect(Collectors.toList())
        ));
        successImportationTable.setEditable(false);

        Label failedImportationLabel = new Label("Failed importation");
        failedImportationLabel.setPadding(new Insets(20,0,0,0));
        failedImportationLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: red;");

        TableView<ImportMapResultToDisplay> failedImportationTable = new TableView<ImportMapResultToDisplay>();
        failedImportationTable.setPrefHeight(200);

        TableColumn<ImportMapResultToDisplay, String> failedCategory = new TableColumn<ImportMapResultToDisplay, String>();
        failedCategory.setText("Category");
        failedCategory.setCellValueFactory(cellData -> cellData.getValue().isOfficialProperty());
        failedCategory.setSortable(true);
        failedCategory.setMinWidth(100);

        TableColumn<ImportMapResultToDisplay, String> failedMapName = new TableColumn<ImportMapResultToDisplay, String>();
        failedMapName.setText("Map name");
        failedMapName.setCellValueFactory(cellData -> cellData.getValue().mapNameProperty());
        failedMapName.setSortable(true);
        failedMapName.setMinWidth(300);

        TableColumn<ImportMapResultToDisplay, String> failedErrorMessage = new TableColumn<ImportMapResultToDisplay, String>();
        failedErrorMessage.setText("Error message");
        failedErrorMessage.setCellValueFactory(cellData -> cellData.getValue().errorMessageProperty());
        failedErrorMessage.setSortable(true);
        failedErrorMessage.setMinWidth(400);

        failedImportationTable.getColumns().add(failedCategory);
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

        ButtonType previousButton = new ButtonType("Previous Profile", ButtonBar.ButtonData.BACK_PREVIOUS);
        ButtonType nextButton = new ButtonType("Next Profile", ButtonBar.ButtonData.NEXT_FORWARD);
        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

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

        return dialog.showAndWait();
    }

}
