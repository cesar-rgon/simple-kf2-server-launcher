package stories.mapedition;

import dtos.AbstractMapDto;
import dtos.CustomMapModDto;
import dtos.ProfileDto;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pojos.session.Session;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

public class MapEditionController implements Initializable {

    private final MapEditionFacade facade;
    private String installationFolder;

    @FXML private WebView mapPreviewWebView;
    @FXML private Text mapNameValue;
    @FXML private TextField mapPreviewUrlTextField;
    @FXML private Text officialValue;
    @FXML private Text downloadedValue;
    @FXML private Text idWorkShopValue;
    @FXML private Text importationDateValue;
    @FXML private TextField releaseDateTextField;
    @FXML private TextField infoUrlTextField;

    public MapEditionController() {
        super();
        facade = new MapEditionFacadeImpl();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            installationFolder = facade.findConfigPropertyValue("prop.config.installationFolder");
            ProfileDto actualProfile = Session.getInstance().getActualProfile();

            WebEngine webEngine = mapPreviewWebView.getEngine();

            if (!Session.getInstance().getMapList().isEmpty()) {
                AbstractMapDto mapDto = Session.getInstance().getMapList().get(0);

                webEngine.documentProperty().addListener(new ChangeListener<Document>() {
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

                mapNameValue.setText(mapDto.getKey());
                officialValue.setText(mapDto.isOfficial()? "Yes": "No");
                downloadedValue.setText(mapDto.isOfficial()? "Yes": ((CustomMapModDto)mapDto).isDownloaded()? "Yes": "No");
                idWorkShopValue.setText(mapDto.isOfficial()? StringUtils.EMPTY: String.valueOf(((CustomMapModDto)mapDto).getIdWorkShop()));
                importationDateValue.setText(
                        StringUtils.isNotBlank(mapDto.getImportedDate(actualProfile.getName())) ? mapDto.getImportedDate(actualProfile.getName()): "Unknown"
                );
                releaseDateTextField.setText(
                        StringUtils.isNotBlank(mapDto.getReleaseDate()) ? mapDto.getReleaseDate(): "Unknown"
                );
                infoUrlTextField.setText(
                        StringUtils.isNotBlank(mapDto.getUrlInfo()) ? mapDto.getUrlInfo(): StringUtils.EMPTY
                );

                if (StringUtils.isNotBlank(mapDto.getUrlPhoto())) {
                    webEngine.load("file:" + installationFolder + mapDto.getUrlPhoto());
                    mapPreviewUrlTextField.setText(mapDto.getUrlPhoto());
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

        }
    }

}
