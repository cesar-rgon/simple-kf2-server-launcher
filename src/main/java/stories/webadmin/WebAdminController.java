package stories.webadmin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLFormElement;
import pojos.session.Session;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class WebAdminController implements Initializable {

    @FXML private WebView webAdmin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = webAdmin.getEngine();
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
                                                  @Override
                                                  public void changed(ObservableValue<? extends Document> ov, Document oldDoc, Document doc) {
                                                      if (doc != null) {
                                                          Element usernameInput = doc.getElementById("username");
                                                          usernameInput.setAttribute("value", "admin");
                                                          Element passwordInput = doc.getElementById("password");
                                                          try {
                                                              passwordInput.setAttribute("value", Utils.decryptAES(Session.getInstance().getActualProfile().getWebPassword()));
                                                          } catch (Exception e) {
                                                              Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                                                          }
                                                          HTMLFormElement form = (HTMLFormElement)doc.getElementById("loginform");
                                                          form.submit();
                                                      }
                                                  }
                                              });
        if (Session.getInstance().isShowWebAdmin()) {
            webEngine.load("http://127.0.0.1:" + Session.getInstance().getWebPort() + "/ServerAdmin");
            webAdmin.setVisible(true);
        }
    }
}
