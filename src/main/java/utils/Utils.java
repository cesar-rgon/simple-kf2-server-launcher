package utils;

import constants.Constants;
import dtos.SelectDto;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.Key;
import java.util.Optional;

public class Utils {

    public static void errorDialog(String headerText, String contentText, Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_APPLICATION_TITLE);
            alert.setTitle(applicationTitle);
        } catch (Exception ex) {
            alert.setTitle("");
        }
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

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
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_APPLICATION_TITLE);
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
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_APPLICATION_TITLE);
            dialog.setTitle(applicationTitle);
        } catch (Exception ex) {
            dialog.setTitle("");
        }
        dialog.setHeaderText("Add a new item");
        dialog.setResizable(false);
        Label code = new Label("Item code: ");
        Label description = new Label("Item description: ");
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
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        dialog.setResultConverter(new Callback<ButtonType, SelectDto>() {
            @Override
            public SelectDto call(ButtonType b) {
                if (b == buttonTypeOk) {
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
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_APPLICATION_TITLE);
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
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_APPLICATION_TITLE);
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
            String applicationTitle = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_APPLICATION_TITLE);
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

    public static String encryptAES(String password) throws Exception {
        if (StringUtils.isBlank(password)) {
            return "";
        }
        PropertyService propertyService = new PropertyServiceImpl();
        String aesEncryptionKey = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_AES_ENCRIPTION_KEY);
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
        String aesEncryptionKey = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_AES_ENCRIPTION_KEY);
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
