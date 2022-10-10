package stories.webadmin;

import dtos.ProfileDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

public class WebAdminController implements Initializable {

    private static final Logger logger = LogManager.getLogger(WebAdminController.class);
    private final WebAdminFacade facade;

    @FXML private WebView webAdmin;
    @FXML private Label message;
    @FXML private ProgressIndicator progressIndicator;

    public WebAdminController() {
        super();
        this.facade = new WebAdminFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String messageText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.webAdminMessage");;
            message.setText(messageText);
            WebEngine webEngine = webAdmin.getEngine();
            webEngine.documentProperty().addListener(new ChangeListener<Document>() {
                @Override
                public void changed(ObservableValue<? extends Document> ov, Document oldDoc, Document doc) {
                    if (doc != null && !webAdmin.isVisible()) {
                        webAdmin.setVisible(true);
                        progressIndicator.setVisible(false);
                        Element usernameInput = doc.getElementById("username");
                        if (usernameInput != null) {
                            usernameInput.setAttribute("value", "admin");
                            Element passwordInput = doc.getElementById("password");
                            if (passwordInput != null) {
                                try {
                                    ProfileDto actualProfile = facade.findProfileDtoByName(Session.getInstance().getActualProfileName());
                                    if (actualProfile != null) {
                                        String decryptedPassword = Utils.decryptAES(actualProfile.getWebPassword());
                                        if (StringUtils.isNotEmpty(decryptedPassword)) {
                                            passwordInput.setAttribute("value", decryptedPassword);
                                        } else {
                                            passwordInput.setAttribute("value", "admin");
                                        }
                                    } else {
                                        passwordInput.setAttribute("value", "admin");
                                    }
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                    Utils.errorDialog(e.getMessage(), e);
                                }
                            }
                        }
                        webEngine.executeScript("document.forms[0].submit();");
                    }
                }
            });

            // Put black color for background of the browser's page
            Field f = webEngine.getClass().getDeclaredField("page");
            f.setAccessible(true);
            com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(webEngine);
            page.setBackgroundColor((new java.awt.Color(0.0f, 0.0f, 0.0f, 1f)).getRGB());

            if (Session.getInstance().isRunningProcess() && facade.findProfileDtoByName(Session.getInstance().getActualProfileName()) != null) {
                ProfileDto databaseProfile = facade.findProfileDtoByName(Session.getInstance().getActualProfileName());
                if (databaseProfile.getWebPage() != null && databaseProfile.getWebPage()) {
                    message.setVisible(false);
                    progressIndicator.setVisible(true);
                    webEngine.load("http://127.0.0.1:" + databaseProfile.getWebPort() + "/ServerAdmin");
                }
            }
        } catch (Exception e) {
            String message = "The WebAdmin page can not be loaded!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }
}
