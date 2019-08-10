package stories.mapwebinfo;

import constants.Constants;
import entities.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import start.MainApplication;
import stories.mapsedition.MapsEditionController;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class MapWebInfoController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapWebInfoController.class);
    private final MapWebInfoFacade facade;
    private Long idWorkShop;
    private String mapName;
    private String strUrlMapImage;

    @FXML private WebView mapInfoWebView;
    @FXML private Label mapNameLabel;
    @FXML private Button addMap;
    @FXML private Label alreadyInLauncher;

    public MapWebInfoController() {
        super();
        facade = new MapWebInfoFacadeImpl();
        idWorkShop = null;
        mapName = null;
        strUrlMapImage = null;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        WebEngine webEngine = mapInfoWebView.getEngine();
        if (Session.getInstance().getMap() != null) {
            mapNameLabel.setText(Session.getInstance().getMap().getKey());
            webEngine.load(Session.getInstance().getMap().getUrlInfo());
            if (!Session.getInstance().getMap().getOfficial()) {
                pageLoadListener(webEngine);
            }
        } else {
            mapNameLabel.setText("");
            webEngine.load("https://steamcommunity.com/app/232090/workshop/");
            pageLoadListener(webEngine);
        }
    }

    private void pageLoadListener(WebEngine webEngine) {
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> ov, Document oldDoc, Document doc) {
                if (doc != null) {
                    try {
                        NodeList titleList = doc.getElementsByTagName("title");
                        String urlWorkShop = doc.getDocumentURI();
                        String[] array = urlWorkShop.split("=");
                        idWorkShop = null;
                        mapName = null;
                        strUrlMapImage = null;
                        if (array != null && array.length > 1) {
                            String[] arrayTwo = array[1].split("&");
                            idWorkShop = Long.parseLong(arrayTwo[0]);
                        }
                        if (titleList != null && titleList.getLength() > 0) {
                            Node title = titleList.item(0);
                            if (title.getTextContent().toUpperCase().startsWith("STEAM WORKSHOP")) {
                                mapName = title.getTextContent().replace("Steam Workshop ::", "");
                                NodeList linkList = doc.getElementsByTagName("link");
                                for (int i=0; i < linkList.getLength(); i++) {
                                    Node link = linkList.item(i);
                                    if (link.hasAttributes()) {
                                        NamedNodeMap linkAttrList = link.getAttributes();
                                        if ("image_src".equalsIgnoreCase(linkAttrList.getNamedItem("rel").getTextContent())) {
                                            strUrlMapImage = linkAttrList.getNamedItem("href").getTextContent();
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        mapNameLabel.setText(mapName);
                        if (idWorkShop != null & facade.isMapInDataBase(idWorkShop)) {
                            addMap.setVisible(false);
                            alreadyInLauncher.setVisible(true);
                        } else {
                            addMap.setVisible(StringUtils.isNotBlank(urlWorkShop) && StringUtils.isNotBlank(mapName));
                            alreadyInLauncher.setVisible(false);
                        }
                    } catch (Exception e) {
                        logger.error("Error in loading process of new page from Steam's WorkShop\nSee stacktrace for more details", e);
                        mapNameLabel.setText(null);
                        addMap.setVisible(false);
                        alreadyInLauncher.setVisible(false);
                    }
                }
            }
        });
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

    @FXML
    private void addMapOnAction() {
        try {
            String installationFolder = facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER);
            if (!facade.isCorrectInstallationFolder(installationFolder)) {
                Utils.warningDialog("No maps can be added!", "The installation folder is not correct.\nSet it up in Install / Update section.");
                return;
            }
            if (Session.getInstance().isRunningProcess()) {
                Utils.warningDialog("No maps can be added!", "At least one instance of the server is running. Close them.");
                return;
            }
            Map customMap = facade.createNewCustomMapFromWorkshop(idWorkShop, mapName, strUrlMapImage,  installationFolder);
            if (customMap != null) {
                addMap.setVisible(false);
                alreadyInLauncher.setVisible(true);
                Utils.infoDialog("The map was successfully added to the launcher", "Map name: " + mapName + "\nURL/Id WorkShop: " + idWorkShop);
            } else {
                Utils.errorDialog("Error adding map to the launcher", "Map name: " + mapName + "\nURL/Id WorkShop: " + idWorkShop, null);
            }
        } catch (Exception e) {
            Utils.errorDialog("Error adding map to the launcher", "See stacktrace for more details", e);
        }
    }
}
