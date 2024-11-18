package stories.wizardstep5;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class WizardStep5Controller implements Initializable {

    private static final Logger logger = LogManager.getLogger(WizardStep5Controller.class);

    @FXML private Button previousStep;
    @FXML private Button finish;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void previousStepOnAction() {
        try {
            FXMLLoader wizardStepTemplate = new FXMLLoader(getClass().getResource("/views/wizard-step4.fxml"));
            Scene scene = new Scene(wizardStepTemplate.load());
            MainApplication.getPrimaryStage().setScene(scene);
            GridPane templateContent = (GridPane) wizardStepTemplate.getNamespace().get("content");
            templateContent.getColumnConstraints().clear();
            templateContent.getRowConstraints().clear();
            templateContent.getChildren().clear();
            FXMLLoader content = new FXMLLoader(getClass().getResource("/views/maps.fxml"));
            content.setRoot(wizardStepTemplate.getNamespace().get("content"));
            content.load();
            MainApplication.setTemplate(wizardStepTemplate);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void finishOnAction() {
        try {
            PropertyService propertyService = new PropertyServiceImpl();
            propertyService.setProperty("properties/config.properties", "prop.config.createDatabase", "false");
            String[] resolution = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationResolution").split("x");
            MainApplication.getPrimaryStage().setWidth(Double.parseDouble(resolution[0]));
            MainApplication.getPrimaryStage().setHeight(Double.parseDouble(resolution[1]));
            Boolean applicationMaximized = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationMaximized"));
            MainApplication.getPrimaryStage().setMaximized(applicationMaximized != null? applicationMaximized: false);
            MainApplication.setTemplate(new FXMLLoader(getClass().getResource("/views/template.fxml")));
            Scene scene = new Scene(MainApplication.getTemplate().load());
            FXMLLoader mainContent = new FXMLLoader(getClass().getResource("/views/mainContent.fxml"));
            mainContent.setRoot(MainApplication.getTemplate().getNamespace().get("content"));
            mainContent.load();
            MainApplication.getPrimaryStage().setScene(scene);
            MainApplication.getPrimaryStage().show();
            Session.getInstance().setWizardMode(false);

            MainApplication.getPrimaryStage().maximizedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                    String isMaximized = String.valueOf(t1.booleanValue());
                    try {
                        propertyService.setProperty("properties/config.properties", "prop.config.applicationMaximized", isMaximized);
                    } catch (Exception e) {
                        String message = "Error setting maximized value in config.properties file";
                        logger.error(message, e);
                        Utils.errorDialog(message, e);
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
