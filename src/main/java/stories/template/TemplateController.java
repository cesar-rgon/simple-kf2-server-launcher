package stories.template;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class TemplateController implements Initializable {

    private static final Logger logger = LogManager.getLogger(TemplateController.class);
    private final PropertyService propertyService;
    private String languageCode;

    @FXML private Menu mainPage;
    @FXML private Menu installUpdateServer;
    @FXML private Menu webAdmin;
    @FXML private Menu mapsMenu;

    public TemplateController() {
        super();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");

            String mainPageTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.mainPage");
            mainPage.setGraphic(getLabelWithHandler(mainPageTitle, "/views/mainContent.fxml"));

            String installUpdateTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.installUpdateServer");
            installUpdateServer.setGraphic(getLabelWithHandler(installUpdateTitle, "/views/installUpdateServer.fxml"));

            String webAdminTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.webAdmin");
            webAdmin.setGraphic(getLabelWithHandler(webAdminTitle, "/views/webAdmin.fxml"));

            String mapsTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.maps");
            mapsMenu.setGraphic(getLabelWithHandler(mapsTitle, "/views/mapsEdition.fxml"));
        } catch (Exception e) {
            String message = "Error setting menu titles";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    private Label getLabelWithHandler(String title, String fxmlFilePath) {
        Label menuLabel = new Label(title);
        menuLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                loadNewContent(title, fxmlFilePath);
            }
        });
        return menuLabel;
    }

    private void loadNewContent(String title, String fxmlFilePath) {
        try {
            GridPane templateContent = (GridPane)MainApplication.getTemplate().getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource(fxmlFilePath));
            content.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            content.load();

            String mainPageTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.mainPage");
            mainPage.setDisable(mainPageTitle.equals(title));

            String installUpdateTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.installUpdateServer");
            installUpdateServer.setDisable(installUpdateTitle.equals(title));

            String webAdminTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.webAdmin");
            webAdmin.setDisable(webAdminTitle.equals(title));

            String mapsTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.maps");
            mapsMenu.setDisable(mapsTitle.equals(title));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void profilesMenuOnAction() {
        try {
            String profilesTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.configuration.profiles");
            loadNewContent(profilesTitle, "/views/profilesEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void gameTypesMenuOnAction() {
        try {
            String gameTypesTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.configuration.gameTypes");
            loadNewContent(gameTypesTitle, "/views/gameTypesEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void difficultiesMenuOnAction() {
        try {
            String difficultiesTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.configuration.difficulties");
            loadNewContent(difficultiesTitle, "/views/difficultiesEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void lengthMenuOnAction() {
        try {
            String lengthTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.configuration.length");
            loadNewContent(lengthTitle, "/views/lengthEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void maxPlayersMenuOnAction() {
        try {
            String maxPlayersTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.menu.configuration.maxPlayers");
            loadNewContent(maxPlayersTitle, "/views/maxPlayersEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void aboutMenuOnAction() {
        try {
            String applicationVersion = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationVersion");
            Utils.infoDialog("Developed by cesar-rgon", "Version: " + applicationVersion);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void documentationMenuOnAction() {
        try {
            String readmeUrl = propertyService.getPropertyValue("properties/config.properties", "prop.config.helpReadmeUrl");
            Desktop.getDesktop().browse(new URI(readmeUrl));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void githubMenuOnAction() {
        try {
            String githubUrl = propertyService.getPropertyValue("properties/config.properties", "prop.config.helpGithubUrl");
            Desktop.getDesktop().browse(new URI(githubUrl));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
