package stories.template;

import constants.Constants;
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
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import stories.difficultiesedition.DifficultiesEditionController;
import utils.Utils;

import java.awt.*;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

public class TemplateController implements Initializable {

    private static final Logger logger = LogManager.getLogger(TemplateController.class);
    private final PropertyService propertyService;

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
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            String mainPageTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_MAIN_PAGE);
            mainPage.setGraphic(getLabelWithHandler(mainPageTitle, "/views/mainContent.fxml"));

            String installUpdateTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_INSTALL_UPDATE_SERVER);
            installUpdateServer.setGraphic(getLabelWithHandler(installUpdateTitle, "/views/installUpdateServer.fxml"));

            String webAdminTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_WEB_ADMIN);
            webAdmin.setGraphic(getLabelWithHandler(webAdminTitle, "/views/webAdmin.fxml"));

            String mapsTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_MAPS_EDITION);
            mapsMenu.setGraphic(getLabelWithHandler(mapsTitle, "/views/mapsEdition.fxml"));
        } catch (Exception e) {
            String message = "Error setting menu titles";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
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

            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            String mainPageTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_MAIN_PAGE);
            mainPage.setDisable(mainPageTitle.equals(title));

            String installUpdateTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_INSTALL_UPDATE_SERVER);
            installUpdateServer.setDisable(installUpdateTitle.equals(title));

            String webAdminTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_WEB_ADMIN);
            webAdmin.setDisable(webAdminTitle.equals(title));

            String mapsTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_MAPS_EDITION);
            mapsMenu.setDisable(mapsTitle.equals(title));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void profilesMenuOnAction() {
        try {
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            String profilesTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_PROFILES_EDITION);
            loadNewContent(profilesTitle, "/views/profilesEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void gameTypesMenuOnAction() {
        try {
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            String gameTypesTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_GAMETYPES_EDITION);
            loadNewContent(gameTypesTitle, "/views/gameTypesEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void difficultiesMenuOnAction() {
        try {
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            String difficultiesTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_DIFFICULTIES_EDITION);
            loadNewContent(difficultiesTitle, "/views/difficultiesEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void lengthMenuOnAction() {
        try {
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            String lengthTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_LENGTH_EDITION);
            loadNewContent(lengthTitle, "/views/lengthEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void maxPlayersMenuOnAction() {
        try {
            String languageCode = Session.getInstance().getActualProfile() != null ?
                    Session.getInstance().getActualProfile().getLanguage().getKey():
                    propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_DEFAULT_LANGUAGE_CODE);

            String maxPlayersTitle = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", Constants.LANG_MENU_MAXPLAYERS_EDITION);
            loadNewContent(maxPlayersTitle, "/views/maxPlayersEdition.fxml");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void aboutMenuOnAction() {
        try {
            String applicationVersion = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_APPLICATION_VERSION);
            Utils.infoDialog("Developed by cesar-rgon", "Version: " + applicationVersion);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void documentationMenuOnAction() {
        try {
            String readmeUrl = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_HELP_README_URL);
            Desktop.getDesktop().browse(new URI(readmeUrl));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void githubMenuOnAction() {
        try {
            String githubUrl = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_HELP_GITHUB_PROJECT_URL);
            Desktop.getDesktop().browse(new URI(githubUrl));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }
}
