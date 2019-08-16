package stories.webadmin;

import dtos.ProfileDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLFormElement;
import pojos.session.Session;
import stories.difficultiesedition.DifficultiesEditionController;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WebAdminController implements Initializable {

    private static final Logger logger = LogManager.getLogger(WebAdminController.class);
    private final WebAdminFacade facade;

    @FXML private WebView webAdmin;

    public WebAdminController() {
        super();
        this.facade = new WebAdminFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            WebEngine webEngine = webAdmin.getEngine();
            webEngine.documentProperty().addListener(new ChangeListener<Document>() {
                @Override
                public void changed(ObservableValue<? extends Document> ov, Document oldDoc, Document doc) {
                    if (doc != null) {
                        Element usernameInput = doc.getElementById("username");
                        usernameInput.setAttribute("value", "admin");
                        Element passwordInput = doc.getElementById("password");
                        try {
                            if (Session.getInstance().getActualProfile() != null) {
                                String decryptedPassword = Utils.decryptAES(Session.getInstance().getActualProfile().getWebPassword());
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
                            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                        }
                        HTMLFormElement form = (HTMLFormElement) doc.getElementById("loginform");
                        form.submit();
                    }
                }
            });
            if (Session.getInstance().isRunningProcess() && Session.getInstance().getActualProfile() != null) {
                ProfileDto databaseProfile = facade.findProfileByName(Session.getInstance().getActualProfile().getName());
                if (databaseProfile.getWebPage() != null && databaseProfile.getWebPage()) {
                    webEngine.load("http://127.0.0.1:" + databaseProfile.getWebPort() + "/ServerAdmin");
                    webAdmin.setVisible(true);
                }
            }
        } catch (SQLException e) {
            String message = "The WebAdmin page can not be loaded!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }
}
