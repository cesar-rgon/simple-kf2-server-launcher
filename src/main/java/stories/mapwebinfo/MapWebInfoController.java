package stories.mapwebinfo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import pojos.session.Session;
import start.MainApplication;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class MapWebInfoController implements Initializable {

    @FXML private WebView mapInfoWebView;
    @FXML private Label mapNameLabel;
    @FXML private Button backButton;

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapNameLabel.setText(Session.getInstance().getMap().getValue());
        WebEngine webEngine = mapInfoWebView.getEngine();
        webEngine.load(Session.getInstance().getMap().getUrlInfo());
    }

    @FXML
    private void backButtonOnAction(){
        loadNewContent("/views/mapsEdition.fxml");
    }

    private void loadNewContent(String fxmlFilePath) {
        try {
            GridPane templateContent = (GridPane) MainApplication.getTemplate().getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource(fxmlFilePath));
            content.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            content.load();
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }
}
