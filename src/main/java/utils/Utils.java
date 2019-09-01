package utils;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.Key;
import java.util.ArrayList;
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

    public static List<ProfileDto> selectProfilesDialog(String headerText, ObservableList<ProfileDto> profiles, List<ProfileDto> selectedProfiles) {
        Dialog<GridPane> dialog = new Dialog<GridPane>();
        PropertyService propertyService = new PropertyServiceImpl();
        try {
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }
        dialog.setHeaderText(headerText);

        GridPane gridpane = new GridPane();
        if (profiles != null && !profiles.isEmpty()) {
            int rowIndex = 1;
            List<String> selectedProfileNameList = selectedProfiles.stream().map(p -> p.getName()).collect(Collectors.toList());
            for (ProfileDto profile: profiles) {
                CheckBox checkbox = new CheckBox(profile.getName());
                checkbox.setStyle("-fx-padding: 0 0 10px 0; -fx-font-weight: bold;");
                if (selectedProfileNameList != null && !selectedProfileNameList.isEmpty() && selectedProfileNameList.contains(profile.getName())) {
                    checkbox.setSelected(true);
                }
                gridpane.add(checkbox, 1, rowIndex);
                Label label = new Label(profile.getGametype() + ", " + profile.getMap() + ", " + profile.getDifficulty() + ", " + profile.getLength());
                label.setStyle("-fx-padding: -10px 0 0 10px; -fx-font-weight: bold; -fx-text-fill: grey;");
                gridpane.add(label, 2, rowIndex);
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
        List<ProfileDto> selectedProfileList = new ArrayList<ProfileDto>();
        if (result.isPresent() && result.get() != null) {
            int index = 0;
            while (index < gridpane.getChildren().size()) {
                CheckBox checkbox = (CheckBox)result.get().getChildren().get(index);
                if (checkbox.isSelected()) {
                    Optional<ProfileDto> profileOpt = profiles.stream().filter(p -> p.getName().equalsIgnoreCase(checkbox.getText())).findFirst();
                    if (profileOpt.isPresent()) {
                        selectedProfileList.add(profileOpt.get());
                    }
                }
                index+=2;
            }
        }
        return selectedProfileList;
    }

    public static ProfileDto selectProfileDialog(ObservableList<ProfileDto> profiles) {
        Dialog<GridPane> dialog = new Dialog<GridPane>();
        PropertyService propertyService = new PropertyServiceImpl();
        String portLabelText = "";
        try {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
            portLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.port");
            dialog.setTitle(applicationTitle);
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.joinServer");
            dialog.setHeaderText(headerText + ":");
        } catch (Exception ex) {
            dialog.setTitle("");
            dialog.setHeaderText("Join only to one server. Select a profile:");
        }

        GridPane gridpane = new GridPane();
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
        return new BASE64Encoder().encode(encrypted);
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
        byte[] decrypted = new BASE64Decoder().decodeBuffer(encryptedPassword);
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
}
