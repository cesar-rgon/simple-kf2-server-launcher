package stories.template;

import constants.MenuTitle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import start.MainApplication;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class TemplateController implements Initializable {

    @FXML private Menu mainPage;
    @FXML private Menu installUpdateServer;
    @FXML private Menu webAdmin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainPage.setGraphic(getLabelWithHandler(MenuTitle.MAIN_PAGE, "/views/mainContent.fxml"));
        installUpdateServer.setGraphic(getLabelWithHandler(MenuTitle.INSTALL_UPDATE_SERVER, "/views/installUpdateServer.fxml"));
        webAdmin.setGraphic(getLabelWithHandler(MenuTitle.WEB_ADMIN, "/views/webAdmin.fxml"));
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
            mainPage.setDisable(MenuTitle.MAIN_PAGE.equals(title));
            installUpdateServer.setDisable(MenuTitle.INSTALL_UPDATE_SERVER.equals(title));
            webAdmin.setDisable(MenuTitle.WEB_ADMIN.equals(title));
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void profilesMenuOnAction() {
        loadNewContent(MenuTitle.PROFILES_EDITION, "/views/profilesEdition.fxml");
    }
}
