package stories.wizardstep1;

import dtos.SelectDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.MainApplication;
import stories.listlanguageswizardstep1.ListLanguagesWizardStep1FacadeResult;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;

public class WizardStep1Controller implements Initializable {

    private static final Logger logger = LogManager.getLogger(WizardStep1Controller.class);

    private WizardStep1ManagerFacade facade;

    @FXML private ComboBox<SelectDto> languageSelect;
    @FXML private Label languageLabel;
    @FXML private Label step1TitleLabel;
    @FXML private Label step2TitleLabel;
    @FXML private Label step3TitleLabel;
    @FXML private Label step4TitleLabel;
    @FXML private Label step5TitleLabel;
    @FXML private Label step1Description;

    public WizardStep1Controller() {
        facade = new WizardStep1ManagerFacadeImpl();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ListLanguagesWizardStep1FacadeResult result = facade.execute();
            languageSelect.setItems(result.getLanguageDtoList());
            languageSelect.getSelectionModel().select(0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void languageOnAction(ActionEvent event) {
        try {
            loadLanguageTexts(languageSelect.getValue() != null ? languageSelect.getValue().getKey() : "en");
        } catch (Exception e) {
            String headerText = "The language value could not be saved!";
            logger.error(headerText, e);
            Utils.errorDialog(headerText, e);
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

        String step1DescriptionText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.wizard.step1Description");
        step1Description.setText(step1DescriptionText);

    }


    @FXML
    private void nextStepOnAction() {
        try {
            FXMLLoader wizardStepTemplate = new FXMLLoader(getClass().getResource("/views/wizard-step2.fxml"));
            Scene scene = new Scene(wizardStepTemplate.load());
            MainApplication.getPrimaryStage().setScene(scene);
            GridPane templateContent = (GridPane) wizardStepTemplate.getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource("/views/installUpdateSteamServer.fxml"));
            content.setRoot(wizardStepTemplate.getNamespace().get("content"));
            content.load();
            MainApplication.setTemplate(wizardStepTemplate);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
