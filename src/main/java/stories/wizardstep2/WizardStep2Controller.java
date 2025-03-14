package stories.wizardstep2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import start.MainApplication;
import stories.wizardsteps.WizardStepsManagerFacade;
import stories.wizardsteps.WizardStepsManagerFacadeImpl;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class WizardStep2Controller implements Initializable {

    private static final Logger logger = LogManager.getLogger(WizardStep2Controller.class);
    private WizardStepsManagerFacade facade;

    @FXML private Label step1TitleLabel;
    @FXML private Label step2TitleLabel;
    @FXML private Label step3TitleLabel;
    @FXML private Label step4TitleLabel;
    @FXML private Label step5TitleLabel;
    @FXML private Button switchSteamGamesServer;
    @FXML private Button switchEpicGamesServer;
    @FXML private Button previousStep;
    @FXML private Button nextStep;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        facade = new WizardStepsManagerFacadeImpl();
        try {
            loadLanguageTexts(Session.getInstance().getWizardLanguage().name());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    private void loadLanguageTexts(String languageCode) throws Exception {
        String step1TitleLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.step1Label");
        step1TitleLabel.setText(step1TitleLabelText);

        String step2TitleLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.step2Label");
        step2TitleLabel.setText(step2TitleLabelText);

        String step3TitleLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.step3Label");
        step3TitleLabel.setText(step3TitleLabelText);

        String step4TitleLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.step4Label");
        step4TitleLabel.setText(step4TitleLabelText);

        String step5TitleLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.step5Label");
        step5TitleLabel.setText(step5TitleLabelText);

        String switchSteamGamesServerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.switchSteamServer");
        switchSteamGamesServer.setText(switchSteamGamesServerText);

        String switchEpicGamesServerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.switchEpicServer");
        switchEpicGamesServer.setText(switchEpicGamesServerText);

        String previousStepText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.previousStep");
        previousStep.setText(previousStepText);

        String nextStepText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.nextStep");
        nextStep.setText(nextStepText);
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
