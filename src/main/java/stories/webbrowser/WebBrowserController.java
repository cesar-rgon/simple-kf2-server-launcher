package stories.webbrowser;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import pojos.session.Session;
import stories.mapwebinfo.MapWebInfoController;
import utils.Utils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

public class WebBrowserController implements Initializable {

    private static final Logger logger = LogManager.getLogger(WebBrowserController.class);

    @FXML private WebView webView;
    @FXML private ProgressIndicator progressIndicator;

    public WebBrowserController() {
        super();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            webView.getEngine().load(Session.getInstance().getUrl());

            // Put black color for background of the browser's page
            Field f = webView.getEngine().getClass().getDeclaredField("page");
            f.setAccessible(true);
            com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(webView.getEngine());
            page.setBackgroundColor((new java.awt.Color(0.0f, 0.0f, 0.0f, 1f)).getRGB());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        webView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> ov, Document oldDoc, Document doc) {
                if (doc == null) {
                    return;
                }
                progressIndicator.setVisible(false);
            }
        });
    }

}
