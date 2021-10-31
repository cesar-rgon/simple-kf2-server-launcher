package stories.mapedition;

import dtos.AbstractMapDto;
import dtos.CustomMapModDto;
import dtos.ProfileDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import pojos.session.Session;
import start.MainApplication;
import utils.Utils;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

public class MapEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MapEditionController.class);

    private final MapEditionFacade facade;
    private String installationFolder;
    private int mapIndex;

    @FXML private WebView mapPreviewWebView;
    @FXML private Text mapNameValue;
    @FXML private TextField mapPreviewUrlTextField;
    @FXML private Text officialValue;
    @FXML private Text downloadedValue;
    @FXML private Text idWorkShopValue;
    @FXML private Text importationDateValue;
    @FXML private TextField releaseDateTextField;
    @FXML private TextField infoUrlTextField;
    @FXML private Button previousMapButton;
    @FXML private Button nextMapButton;
    @FXML private Button backButton;


    public MapEditionController() {
        super();
        facade = new MapEditionFacadeImpl();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mapPreviewWebView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
            @Override
            public void changed(ObservableValue<? extends Document> observable, Document oldDoc, Document doc) {
                if (doc != null) {
                    NodeList imgList = doc.getElementsByTagName("img");
                    if (imgList != null && imgList.getLength() > 0) {
                        Element img = (Element) imgList.item(0);
                        img.setAttribute("width", "512");
                        img.setAttribute("height", "256");
                    }

                }
            }
        });

        mapPreviewUrlTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                                /*
                                String profileName = profileSelect.getValue() != null ? profileSelect.getValue().getName(): null;
                                if (!facade.updateProfileSetUrlImageServer(profileName, urlImageServer.getText())) {
                                    logger.warn("The image server link value could not be saved!" + urlImageServer.getText());
                                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                            "prop.message.profileNotUpdated");
                                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageSelect.getValue().getKey() + ".properties",
                                            "prop.message.imageServerLinkNotSaved");
                                    Utils.warningDialog(headerText, contentText);
                                }
                                 */
                        if (StringUtils.isNotEmpty(mapPreviewUrlTextField.getText())) {
                            mapPreviewWebView.getEngine().load(mapPreviewUrlTextField.getText());
                        } else {
                            mapPreviewWebView.getEngine().load("file:" + getClass().getResource("/images/photo-borders.png").getPath());
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        try {
            installationFolder = facade.findConfigPropertyValue("prop.config.installationFolder");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        mapIndex = 0;
        loadMapData(mapIndex);
    }

    private void loadMapData(int mapIndex) {
        try {
            ProfileDto actualProfile = Session.getInstance().getActualProfile();

            WebEngine webEngine = mapPreviewWebView.getEngine();

            if (!Session.getInstance().getMapList().isEmpty()) {
                AbstractMapDto mapDto = Session.getInstance().getMapList().get(mapIndex);

                mapNameValue.setText(mapDto.getKey());
                officialValue.setText(mapDto.isOfficial() ? "Yes" : "No");
                downloadedValue.setText(mapDto.isOfficial() ? "Yes" : ((CustomMapModDto) mapDto).isDownloaded() ? "Yes" : "No");
                idWorkShopValue.setText(mapDto.isOfficial() ? StringUtils.EMPTY : String.valueOf(((CustomMapModDto) mapDto).getIdWorkShop()));
                importationDateValue.setText(
                        StringUtils.isNotBlank(mapDto.getImportedDate(actualProfile.getName())) ? mapDto.getImportedDate(actualProfile.getName()) : "Unknown"
                );
                releaseDateTextField.setText(
                        StringUtils.isNotBlank(mapDto.getReleaseDate()) ? mapDto.getReleaseDate() : "Unknown"
                );
                infoUrlTextField.setText(
                        StringUtils.isNotBlank(mapDto.getUrlInfo()) ? mapDto.getUrlInfo() : StringUtils.EMPTY
                );

                if (StringUtils.isNotBlank(mapDto.getUrlPhoto())) {
                    webEngine.load("file:" + installationFolder + mapDto.getUrlPhoto());
                    mapPreviewUrlTextField.setText("file:" + installationFolder + mapDto.getUrlPhoto());
                } else {
                    webEngine.load("file:" + getClass().getResource("/images/no-photo.png").getPath());
                    mapPreviewUrlTextField.setText(StringUtils.EMPTY);
                }

                // Put gray color for background of the browser's page
                Field f = webEngine.getClass().getDeclaredField("page");
                f.setAccessible(true);
                com.sun.webkit.WebPage page = (com.sun.webkit.WebPage) f.get(webEngine);
                page.setBackgroundColor((new java.awt.Color(0.5019608f, 0.5019608f, 0.5019608f, 0.5f)).getRGB());
            } else {
                webEngine.load("file:" + getClass().getResource("/images/no-photo.png").getPath());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
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
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void previousMapButtonOnAction(){
        if (mapIndex > 0) {
            mapIndex--;
        } else {
            mapIndex = Session.getInstance().getMapList().size() - 1;
        }
        loadMapData(mapIndex);
    }

    @FXML
    private void nextMapButtonOnAction(){
        if (mapIndex < Session.getInstance().getMapList().size() - 1) {
            mapIndex++;
        } else {
            mapIndex = 0;
        }
        loadMapData(mapIndex);
    }

}
