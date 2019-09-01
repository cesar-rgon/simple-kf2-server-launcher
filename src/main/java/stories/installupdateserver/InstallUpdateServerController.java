package stories.installupdateserver;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class InstallUpdateServerController implements Initializable {

    private static final Logger logger = LogManager.getLogger(InstallUpdateServerController.class);
    private final InstallUpdateServerFacade facade;
    private final PropertyService propertyService;
    protected String languageCode;

    @FXML private TextField installationFolder;
    @FXML private CheckBox validateFiles;
    @FXML private CheckBox isBeta;
    @FXML private TextField betaBrunch;
    @FXML private Label installationFolderLabel;
    @FXML private Label validateFilesLabel;
    @FXML private Label updateBetaLabel;
    @FXML private Label betaBrunchLabel;
    @FXML private Button exploreFolder;
    @FXML private Button installUpdate;

    public InstallUpdateServerController() {
        super();
        facade = new InstallUpdateServerFacadeImpl();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            installationFolder.setText(facade.findPropertyValue("prop.config.installationFolder"));
            isBeta.setSelected(Boolean.parseBoolean(facade.findPropertyValue("prop.config.isBeta")));
            betaBrunch.setText(facade.findPropertyValue("prop.config.betaBrunch"));
            String installationFolderLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.installationFolder");
            installationFolderLabel.setText(installationFolderLabelText + "*");
            String validateFilesLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.validateFiles");
            validateFilesLabel.setText(validateFilesLabelText);
            String updateBetaLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.updateBeta");
            updateBetaLabel.setText(updateBetaLabelText);
            String betaBrunchLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.betaBrunch");
            betaBrunchLabel.setText(betaBrunchLabelText);
            String exploreFolderText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.exploreFolder");
            exploreFolder.setText(exploreFolderText);
            String installUpdateText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.installUpdate");
            installUpdate.setText(installUpdateText);
            String validateFilesText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.validateFilesCheck");
            validateFiles.setText(validateFilesText);
            String isBetaText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.isBetaCheck");
            isBeta.setText(isBetaText);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        installationFolder.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        if (!facade.saveOrUpdateProperty("prop.config.installationFolder", installationFolder.getText())) {
                            logger.warn("The installation folder value could not be saved!:" + installationFolder.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installDirNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });

        betaBrunch.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        if (!facade.saveOrUpdateProperty("prop.config.betaBrunch", betaBrunch.getText())) {
                            logger.warn("The beta brunch value could not be saved!: " + betaBrunch.getText());
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.betaBrunchNotSaved");
                            Utils.warningDialog(headerText, contentText);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            }
        });
    }

    @FXML
    private void exploreFolderOnAction() {
        try {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.browseFolder");
            directoryChooser.setTitle(message);
            File selectedDirectory = directoryChooser.showDialog(MainApplication.getPrimaryStage());
            if (selectedDirectory != null) {
                installationFolder.setText(selectedDirectory.getAbsolutePath());
                if (!facade.saveOrUpdateProperty("prop.config.installationFolder", installationFolder.getText())) {
                    logger.warn("The installation folder value could not be saved!: " + installationFolder.getText());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installDirNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void isBetaOnAction() {
        try {
            if (!facade.saveOrUpdateProperty("prop.config.isBeta", String.valueOf(isBeta.isSelected()))) {
                logger.warn("The is-beta value could not be saved!: " + isBeta.isSelected());
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.isBetaNotSaved");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }


    @FXML
    private void validateFilesOnAction() {
        try {
            if (!facade.saveOrUpdateProperty("prop.config.validateFiles", String.valueOf(validateFiles.isSelected()))) {
                logger.warn("The validate files value could not be saved!: " + validateFiles.isSelected());
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.validateFilesNotSaved");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }


    @FXML
    private void installUpdateServer() {
        Kf2Common kf2Common = Kf2Factory.getInstance();
        kf2Common.installOrUpdateServer(installationFolder.getText(), validateFiles.isSelected(), isBeta.isSelected(), betaBrunch.getText());
    }
}
