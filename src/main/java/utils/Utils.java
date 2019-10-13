package utils;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import pojos.AddMapsToProfile;
import pojos.ProfileToDisplay;
import pojos.RadioButtonTableCell;
import services.PropertyService;
import services.PropertyServiceImpl;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

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
        try {
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }
        dialog.setHeaderText(headerText);

        TableView<AddMapsToProfile> tableView = new TableView<AddMapsToProfile>();
        // First Column
        TableColumn<AddMapsToProfile, String> profileNameColumn = new TableColumn<AddMapsToProfile, String>();
        profileNameColumn.setText("Profile name");
        profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().profileNameProperty());
        profileNameColumn.setSortable(false);
        profileNameColumn.setEditable(false);
        profileNameColumn.setMinWidth(150);

        // Second Column
        TableColumn<AddMapsToProfile, String> customMapsColumn = new TableColumn<AddMapsToProfile, String>();
        customMapsColumn.setText("Custom URL/Id from WorkShop for maps/mods separated by comma");
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
        try {
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }
        dialog.setHeaderText(headerText);

        TableView<ProfileToDisplay> tableView = new TableView<ProfileToDisplay>();

        // First Column
        TableColumn<ProfileToDisplay, Boolean> selectColumn = new TableColumn<ProfileToDisplay, Boolean>();
        selectColumn.setText("Select");
        selectColumn.setCellFactory(col -> new CheckBoxTableCell<>());
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setSortable(false);
        selectColumn.setEditable(true);
        selectColumn.setMinWidth(50);

        // Second Column
        TableColumn<ProfileToDisplay, String> profileNameColumn = new TableColumn<ProfileToDisplay, String>();
        profileNameColumn.setText("Profile name");
        profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().profileNameProperty());
        profileNameColumn.setSortable(false);
        profileNameColumn.setEditable(false);
        profileNameColumn.setMinWidth(150);

        // Third Column
        TableColumn<ProfileToDisplay, String> gameTypeColumn = new TableColumn<ProfileToDisplay, String>();
        gameTypeColumn.setText("Game type");
        gameTypeColumn.setCellValueFactory(cellData -> cellData.getValue().gameTypeDescriptionProperty());
        gameTypeColumn.setSortable(false);
        gameTypeColumn.setEditable(false);
        gameTypeColumn.setMinWidth(150);

        // Fourth Column
        TableColumn<ProfileToDisplay, String> mapNameColumn = new TableColumn<ProfileToDisplay, String>();
        mapNameColumn.setText("Map name");
        mapNameColumn.setCellValueFactory(cellData -> cellData.getValue().mapNameProperty());
        mapNameColumn.setSortable(false);
        mapNameColumn.setEditable(false);
        mapNameColumn.setMinWidth(150);

        // Fifth Column
        TableColumn<ProfileToDisplay, String> difficultyColumn = new TableColumn<ProfileToDisplay, String>();
        difficultyColumn.setText("Difficuty");
        difficultyColumn.setCellValueFactory(cellData -> cellData.getValue().difficultyDescriptionProperty());
        difficultyColumn.setSortable(false);
        difficultyColumn.setEditable(false);
        difficultyColumn.setMinWidth(150);

        // Sixth Column
        TableColumn<ProfileToDisplay, String> lengthColumn = new TableColumn<ProfileToDisplay, String>();
        lengthColumn.setText("Length");
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

    public static ProfileToDisplay selectProfileDialog(String headerText, List<ProfileToDisplay> profileList) {
        Dialog<TableView<ProfileToDisplay>> dialog = new Dialog<TableView<ProfileToDisplay>>();
        PropertyService propertyService = new PropertyServiceImpl();
        String portLabelText = "";
        try {
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            portLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.port");
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }
        dialog.setHeaderText(headerText);

        TableView<ProfileToDisplay> tableView = new TableView<ProfileToDisplay>();

        // First Column
        TableColumn<ProfileToDisplay, Boolean> selectColumn = new TableColumn<ProfileToDisplay, Boolean>();
        selectColumn.setText("Select");
        ToggleGroup toggleGroup = new ToggleGroup();
        selectColumn.setCellFactory(factory -> new RadioButtonTableCell(toggleGroup));
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setSortable(false);
        selectColumn.setEditable(true);
        selectColumn.setMinWidth(50);

        tableView.getColumns().add(selectColumn);
        tableView.setItems(FXCollections.observableArrayList(profileList));
        tableView.setEditable(true);
        Optional<ProfileToDisplay> selectedProfile = profileList.stream().filter(p -> p.isSelected()).findFirst();
        if (selectedProfile.isPresent()) {
            tableView.getSelectionModel().select(selectedProfile.get());
        }

        dialog.getDialogPane().setContent(tableView);
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(800);
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

        System.out.println(tableView.getSelectionModel().getSelectedItem().getProfileName());
        // ...
        return null;

        /* CON GRID
        if (profiles != null && !profiles.isEmpty()) {
            int rowIndex = 1;
            ToggleGroup group = new ToggleGroup();
            for (ProfileDto profile: profiles) {
                RadioButton radioButton = new RadioButton(profile.getName());
                radioButton.setToggleGroup(group);
                radioButton.setStyle("-fx-padding: 0 0 10px 0; -fx-font-weight: bold;");
                if (Session.getInstance().getActualProfile() != null && profile.getName().equals(Session.getInstance().getActualProfile().getName())) {
                    radioButton.setSelected(true);
                }
                gridpane.add(radioButton, 1, rowIndex);
                Label labelGamePort = null;
                try {
                    labelGamePort = new Label(portLabelText + "=" + profile.getGamePort());
                } catch (Exception e) {
                    labelGamePort = new Label("Port=" + profile.getGamePort());
                }

                labelGamePort.setStyle("-fx-padding: -10px 0 0 10px; -fx-text-fill: red;");
                gridpane.add(labelGamePort, 2, rowIndex);
                Label label = new Label(profile.getGametype() + ", " + profile.getMap() + ", " + profile.getDifficulty() + ", " + profile.getLength());
                label.setStyle("-fx-padding: -10px 0 0 10px; -fx-font-weight: bold; -fx-text-fill: grey;");
                gridpane.add(label, 3, rowIndex);
                rowIndex++;
            }
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridpane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        dialog.getDialogPane().setContent(scrollPane);
        dialog.setResizable(true);
        dialog.getDialogPane().setMinWidth(400);
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
        dialog.setResultConverter(new Callback<ButtonType, GridPane>() {
            @Override
            public GridPane call(ButtonType b) {
                if (b == finalButtonTypeOk) {
                    return gridpane;
                }
                return null;
            }
        });

        Optional<GridPane> result = dialog.showAndWait();
        if (result.isPresent() && result.get() != null) {
            int index = 0;
            while (index < gridpane.getChildren().size()) {
                RadioButton radioButton = (RadioButton)result.get().getChildren().get(index);
                if (radioButton.isSelected()) {
                    Optional<ProfileDto> profileOpt = profiles.stream().filter(p -> p.getName().equalsIgnoreCase(radioButton.getText())).findFirst();
                    if (profileOpt.isPresent()) {
                        return profileOpt.get();
                    }
                }
                index+=3;
            }
        }
        return null;
        */
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
}
