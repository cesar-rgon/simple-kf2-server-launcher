package stories.installupdateserver;

import constants.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import start.MainApplication;
import stories.gametypesedition.GameTypesEditionController;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class InstallUpdateServerController implements Initializable {

    private static final Logger logger = LogManager.getLogger(InstallUpdateServerController.class);
    private final InstallUpdateServerFacade facade;

    @FXML private TextField installationFolder;
    @FXML private CheckBox validateFiles;
    @FXML private CheckBox isBeta;
    @FXML private TextField betaBrunch;

    public InstallUpdateServerController() {
        super();
        facade = new InstallUpdateServerFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            installationFolder.setText(facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER));
            isBeta.setSelected(Boolean.parseBoolean(facade.findPropertyValue(Constants.CONFIG_IS_BETA)));
            betaBrunch.setText(facade.findPropertyValue(Constants.CONFIG_BETA_BRUNCH));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }

        installationFolder.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        if (!facade.saveOrUpdateProperty(Constants.CONFIG_INSTALLATION_FOLDER, installationFolder.getText())) {
                            String message = "The installation folder value could not be saved!";
                            logger.warn(message);
                            Utils.warningDialog("Error updating the property information", message);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });

        betaBrunch.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        if (!facade.saveOrUpdateProperty(Constants.CONFIG_BETA_BRUNCH, betaBrunch.getText())) {
                            String message = "The beta brunch value could not be saved!";
                            logger.warn(message);
                            Utils.warningDialog("Error updating the property information", message);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
                }
            }
        });
    }

    @FXML
    private void exploreFolderOnAction() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Browse to KF2 server folder");
        File selectedDirectory = directoryChooser.showDialog(MainApplication.getPrimaryStage());
        if (selectedDirectory != null) {
            installationFolder.setText(selectedDirectory.getAbsolutePath());
            try {
                if (!facade.saveOrUpdateProperty(Constants.CONFIG_INSTALLATION_FOLDER, installationFolder.getText())) {
                    String message = "The installation folder value could not be saved!";
                    logger.warn(message);
                    Utils.warningDialog("Error updating the property information", message);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
            }
        }
    }

    @FXML
    private void isBetaOnAction() {
        try {
            if (!facade.saveOrUpdateProperty(Constants.CONFIG_IS_BETA, String.valueOf(isBeta.isSelected()))) {
                String message = "The is beta value could not be saved!";
                logger.warn(message);
                Utils.warningDialog("Error updating the property information", message);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }


    @FXML
    private void validateFilesOnAction() {
        try {
            if (!facade.saveOrUpdateProperty(Constants.CONFIG_VALIDATE_FILES, String.valueOf(validateFiles.isSelected()))) {
                String message = "The validate files value could not be saved!";
                logger.warn(message);
                Utils.warningDialog("Error updating the property information", message);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }


    @FXML
    private void installUpdateServer() {
        Kf2Common kf2Common = Kf2Factory.getInstance();
        kf2Common.installOrUpdateServer(installationFolder.getText(), validateFiles.isSelected(), isBeta.isSelected(), betaBrunch.getText());
    }
}
