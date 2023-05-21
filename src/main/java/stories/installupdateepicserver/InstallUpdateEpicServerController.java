package stories.installupdateepicserver;

import entities.EpicPlatform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Epic;
import pojos.kf2factory.Kf2Factory;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import stories.getplatforminstallationfolder.GetPlatformInstallationFolderFacadeResult;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class InstallUpdateEpicServerController implements Initializable {

    private static final Logger logger = LogManager.getLogger(InstallUpdateEpicServerController.class);
    private final InstallUpdateEpicServerManagerFacade facade;

    protected String languageCode;

    @FXML private Label titleConfigLabel;
    @FXML private TextField installationFolder;
    @FXML private Label installationFolderLabel;
    @FXML private ImageView installationFolderImg;
    @FXML private Button exploreFolder;
    @FXML private Button installUpdate;
    @FXML private ProgressIndicator progressIndicator;

    public InstallUpdateEpicServerController() {
        super();
        facade = new InstallUpdateEpicServerManagerFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = facade.findPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");

            String titleConfigLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.titleInstallEpicServer");
            titleConfigLabel.setText(titleConfigLabelText);

            String installationFolderLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.installationFolder");
            installationFolderLabel.setText(installationFolderLabelText + "*");
            GetPlatformInstallationFolderFacadeResult result = facade.execute();
            installationFolder.setText(result.getPlatformInstallationFolder());
            loadTooltip("prop.tooltip.installationFolder", installationFolderImg, installationFolderLabel, installationFolder);

            Double tooltipDuration = Double.parseDouble(
                    facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );

            String exploreFolderText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.exploreFolder");
            exploreFolder.setText(exploreFolderText);
            Tooltip exploreFolderTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.exploreFolder"));
            exploreFolderTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            exploreFolder.setTooltip(exploreFolderTooltip);

            String installUpdateText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.installUpdate");
            installUpdate.setText(installUpdateText);
            Tooltip installUpdateTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.installUpdate"));
            installUpdateTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            installUpdate.setTooltip(installUpdateTooltip);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }

        installationFolder.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        if (!facade.updatePlatformInstallationFolder(installationFolder.getText())) {
                            logger.warn("The installation folder value could not be saved!:" + installationFolder.getText());
                            String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                            String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installDirNotSaved");
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
            String message = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.browseFolder");
            directoryChooser.setTitle(message);
            File selectedDirectory = directoryChooser.showDialog(MainApplication.getPrimaryStage());
            if (selectedDirectory != null) {
                installationFolder.setText(selectedDirectory.getAbsolutePath());
                if (!facade.updatePlatformInstallationFolder(installationFolder.getText())) {
                    logger.warn("The installation folder value could not be saved!: " + installationFolder.getText());
                    String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                    String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installDirNotSaved");
                    Utils.warningDialog(headerText, contentText);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void installUpdateServer() {
        progressIndicator.setVisible(true);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                facade.updatePlatformInstallationFolder(installationFolder.getText());
                return null;
            }
        };

        task.setOnSucceeded(wse -> {
            try {
                facade.installUpdateServer();
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
            progressIndicator.setVisible(false);
        });
        task.setOnFailed(wse -> {
            progressIndicator.setVisible(false);
        });

        Thread thread = new Thread(task);
        thread.start();
    }

    private void loadTooltip(String propKey, javafx.scene.image.ImageView img, Label label, TextField textField) throws Exception {
        Double tooltipDuration = Double.parseDouble(
                facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        textField.setTooltip(tooltip);
    }
}
