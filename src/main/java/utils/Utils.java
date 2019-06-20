package utils;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.Key;

public class Utils {

    private final static String AES_ENCRIPTION_KEY = "SimplKillingFloor2ServerLauncher";

    public static void errorDialog(String headerText, String contentText, Throwable e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Simple Killing Floor 2 Server Launcher");
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
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);
            GridPane expContent = new GridPane();
            expContent.add(textArea, 0, 1);
            alert.getDialogPane().setMinWidth(600);
            alert.getDialogPane().setMinHeight(400);
            alert.getDialogPane().setExpandableContent(expContent);
        }
        alert.showAndWait();
    }

    public static String encryptAES(String password) throws Exception {
        if (password == null) {
            return null;
        }
        Key aesKey = new SecretKeySpec(AES_ENCRIPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(password.getBytes());
        return new BASE64Encoder().encode(encrypted);
    }

    public static String decryptAES(String encryptedPassword) throws Exception {
        if (encryptedPassword == null) {
            return null;
        }
        Key aesKey = new SecretKeySpec(AES_ENCRIPTION_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decrypted = new BASE64Decoder().decodeBuffer(encryptedPassword);
        byte[] decryptedValue = cipher.doFinal(decrypted);
        return new String(decryptedValue);
    }
}
