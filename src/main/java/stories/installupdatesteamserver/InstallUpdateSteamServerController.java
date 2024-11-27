package stories.installupdatesteamserver;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import stories.getplatforminstallationfolder.GetPlatformInstallationFolderFacadeResult;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class InstallUpdateSteamServerController implements Initializable {

    private static final Logger logger = LogManager.getLogger(InstallUpdateSteamServerController.class);
    private final InstallUpdateSteamServerManagerFacade facade;
    protected String languageCode;

    @FXML private Label titleConfigLabel;

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
    @FXML private ImageView installationFolderImg;
    @FXML private ImageView validateFilesImg;
    @FXML private ImageView updateBetaImg;
    @FXML private ImageView betaBrunchImg;
    @FXML private ProgressIndicator progressIndicator;

    public InstallUpdateSteamServerController() {
        super();
        facade = new InstallUpdateSteamServerManagerFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            if (Session.getInstance().isWizardMode()) {
                languageCode = Session.getInstance().getWizardLanguage().name();
            } else {
                languageCode = facade.findPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            }

            String titleConfigLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.titleInstallSteamServer");
            titleConfigLabel.setText(titleConfigLabelText);

            String installationFolderLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.installationFolder");
            installationFolderLabel.setText(installationFolderLabelText + "*");
            GetPlatformInstallationFolderFacadeResult result = facade.execute();
            installationFolder.setText(result.getPlatformInstallationFolder());
            loadTooltip("prop.tooltip.installationFolder", installationFolderImg, installationFolderLabel, installationFolder);

            String validateFilesLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.validateFiles");
            validateFilesLabel.setText(validateFilesLabelText);
            String validateFilesText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.validateFilesCheck");
            validateFiles.setText(validateFilesText);
            loadTooltip("prop.tooltip.validateFiles", validateFilesImg, validateFilesLabel, validateFiles);

            isBeta.setSelected(Boolean.parseBoolean(facade.findPropertyValue("properties/config.properties", "prop.config.isBeta")));
            String isBetaText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.isBetaCheck");
            isBeta.setText(isBetaText);
            String updateBetaLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.updateBeta");
            updateBetaLabel.setText(updateBetaLabelText);
            loadTooltip("prop.tooltip.updateBeta", updateBetaImg, updateBetaLabel, isBeta);

            betaBrunch.setText(facade.findPropertyValue("properties/config.properties","prop.config.betaBrunch"));
            String betaBrunchLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.betaBrunch");
            betaBrunchLabel.setText(betaBrunchLabelText);
            loadTooltip("prop.tooltip.betaBrunch", betaBrunchImg, betaBrunchLabel, betaBrunch);

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

            Platform.runLater(() -> {
                try {
                    if (Session.getInstance().isWizardMode()) {
                        FXMLLoader wizardStepTemplate = MainApplication.getTemplate();
                        Button nextStep = (Button) wizardStepTemplate.getNamespace().get("nextStep");
                        if (StringUtils.isNotBlank(installationFolder.getText())) {
                            nextStep.setDisable(false);
                        } else {
                            nextStep.setDisable(true);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                }
            });

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }


        installationFolder.textProperty().addListener(new ChangeListener<String>() {
              @Override
              public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                    if (!newValue.equals(oldValue)) {
                        try {
                            if (facade.updatePlatformInstallationFolder(installationFolder.getText())) {
                                if (Session.getInstance().isWizardMode()) {
                                    FXMLLoader wizardStepTemplate = MainApplication.getTemplate();
                                    Button nextStep = (Button) wizardStepTemplate.getNamespace().get("nextStep");
                                    if (StringUtils.isNotBlank(installationFolder.getText())) {
                                        nextStep.setDisable(false);
                                    } else {
                                        nextStep.setDisable(true);
                                    }
                                }
                            } else {
                                logger.warn("The installation folder value could not be saved!:" + installationFolder.getText());
                                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installDirNotSaved");
                                Utils.warningDialog(headerText, contentText);
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            Utils.errorDialog(e.getMessage(), e);
                        }
                    }
              }
        });

        betaBrunch.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                try {
                    if (!newPropertyValue) {
                        if (!facade.saveOrUpdateProperty("properties/config.properties","prop.config.betaBrunch", betaBrunch.getText())) {
                            logger.warn("The beta brunch value could not be saved!: " + betaBrunch.getText());
                            String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                            String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.betaBrunchNotSaved");
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

    private void loadTooltip(String propKey, javafx.scene.image.ImageView img, Label label, CheckBox checkBox) throws Exception {
        Double tooltipDuration = Double.parseDouble(
                facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
        );
        Tooltip tooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties",propKey));
        tooltip.setShowDuration(Duration.seconds(tooltipDuration));
        Tooltip.install(img, tooltip);
        label.setTooltip(tooltip);
        checkBox.setTooltip(tooltip);
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
                if (facade.updatePlatformInstallationFolder(installationFolder.getText())) {
                    if (Session.getInstance().isWizardMode()) {
                        FXMLLoader wizardStepTemplate = MainApplication.getTemplate();
                        Button nextStep = (Button) wizardStepTemplate.getNamespace().get("nextStep");
                        if (StringUtils.isNotBlank(installationFolder.getText())) {
                            nextStep.setDisable(false);
                        } else {
                            nextStep.setDisable(true);
                        }
                    }
                } else {
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
    private void isBetaOnAction() {
        try {
            if (!facade.saveOrUpdateProperty("properties/config.properties","prop.config.isBeta", String.valueOf(isBeta.isSelected()))) {
                logger.warn("The is-beta value could not be saved!: " + isBeta.isSelected());
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.isBetaNotSaved");
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
            if (!facade.saveOrUpdateProperty("properties/config.properties","prop.config.validateFiles", String.valueOf(validateFiles.isSelected()))) {
                logger.warn("The validate files value could not be saved!: " + validateFiles.isSelected());
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.validateFilesNotSaved");
                Utils.warningDialog(headerText, contentText);
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
                if (StringUtils.isNotBlank(installationFolder.getText())) {
                    installationFolder.setText(installationFolder.getText().replaceAll(" ", "_"));
                }
                facade.updatePlatformInstallationFolder(installationFolder.getText());
                return null;
            }
        };
        task.setOnSucceeded(wse -> {
            try {
                facade.installUpdateServer(validateFiles.isSelected(), isBeta.isSelected(), betaBrunch.getText());
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
}
