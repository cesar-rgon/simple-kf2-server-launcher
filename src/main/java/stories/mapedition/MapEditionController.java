package stories.mapedition;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

public class MapEditionController implements Initializable {

    private final MapEditionFacade facade;

    @FXML private WebView mapPreviewWebView;

    public MapEditionController() {
        super();
        facade = new MapEditionFacadeImpl();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            WebEngine webEngine = mapPreviewWebView.getEngine();
            webEngine.load("file:C:\\kf2server\\KFGame\\Web\\images\\maps\\custom\\KF1 West London.jpeg");
            //webEngine.load("https://math.hws.edu/graphicsbook/demos/c4/textures/NightEarth-512x256.jpg");

            // Use reflection to retrieve the WebEngine's private 'page' field.
            Field f = webEngine.getClass().getDeclaredField("page");
            f.setAccessible(true);
            com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(webEngine);
            page.setBackgroundColor((new java.awt.Color(0.5019608f, 0.5019608f, 0.5019608f, 0.5f)).getRGB());

        } catch (Exception e) {

        }
    }

}
