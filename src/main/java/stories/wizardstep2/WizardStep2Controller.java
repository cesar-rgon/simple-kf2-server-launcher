package stories.wizardstep2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.MainApplication;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class WizardStep2Controller implements Initializable {

    private static final Logger logger = LogManager.getLogger(WizardStep2Controller.class);

    @FXML private Button switchSteamGamesServer;
    @FXML private Button switchEpicGamesServer;
    @FXML private Button previousStep;
    @FXML private Button nextStep;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void switchSteamServerOnAction() {
        try {
            FXMLLoader wizardStepTemplate = MainApplication.getTemplate();
            GridPane templateContent = (GridPane) wizardStepTemplate.getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource("/views/installUpdateSteamServer.fxml"));
            content.setRoot(wizardStepTemplate.getNamespace().get("content"));
            content.load();
            switchEpicGamesServer.setDisable(false);
            switchSteamGamesServer.setDisable(true);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void switchEpicGamesServerOnAction() {
        try {
            FXMLLoader wizardStepTemplate = MainApplication.getTemplate();
            GridPane templateContent = (GridPane) wizardStepTemplate.getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource("/views/installUpdateEpicServer.fxml"));
            content.setRoot(wizardStepTemplate.getNamespace().get("content"));
            content.load();
            switchSteamGamesServer.setDisable(false);
            switchEpicGamesServer.setDisable(true);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void previousStepOnAction() {
        try {
            FXMLLoader wizardStepTemplate = new FXMLLoader(getClass().getResource("/views/wizard-step1.fxml"));
            Scene scene = new Scene(wizardStepTemplate.load());
            MainApplication.getPrimaryStage().setScene(scene);
            MainApplication.setTemplate(wizardStepTemplate);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void nextStepOnAction() {
        try {
            FXMLLoader wizardStepTemplate = new FXMLLoader(getClass().getResource("/views/wizard-step3.fxml"));
            Scene scene = new Scene(wizardStepTemplate.load());
            MainApplication.getPrimaryStage().setScene(scene);
            GridPane templateContent = (GridPane) wizardStepTemplate.getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource("/views/profilesEdition.fxml"));
            content.setRoot(wizardStepTemplate.getNamespace().get("content"));
            content.load();
            MainApplication.setTemplate(wizardStepTemplate);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
