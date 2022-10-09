package stories.profilesedition;

import dtos.ProfileDto;
import entities.Profile;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.PlatformProfileToDisplay;
import pojos.ProfileToDisplay;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.util.*;

public class ProfilesEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(ProfilesEditionController.class);
    private final ProfilesEditionFacade facade;
    private final PropertyService propertyService;
    protected String languageCode;

    @FXML private TableView<ProfileDto> profilesTable;
    @FXML private TableColumn<ProfileDto, String> profileNameColumn;
    @FXML private Label titleConfigLabel;
    @FXML private Label messageLabel;
    @FXML private Button addProfile;
    @FXML private Button cloneProfile;
    @FXML private Button removeProfile;
    @FXML private Button importProfile;
    @FXML private Button exportProfile;
    @FXML private Label profileNameLabel;
    @FXML private ProgressIndicator progressIndicator;

    public ProfilesEditionController() {
        facade = new ProfilesEditionFacadeImpl();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            profileNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            profilesTable.setItems(facade.listAllProfiles());

            String titleConfigLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileTitle");
            titleConfigLabel.setText(titleConfigLabelText);

            String messageLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.itemMessage");
            messageLabel.setText(messageLabelText);

            Double tooltipDuration = Double.parseDouble(
                    propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );

            String addProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addItem");
            addProfile.setText(addProfileText);
            Tooltip addProfileTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.addProfile"));
            addProfileTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            addProfile.setTooltip(addProfileTooltip);

            String cloneProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.cloneItem");
            cloneProfile.setText(cloneProfileText);
            Tooltip cloneProfileTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.cloneProfile"));
            cloneProfileTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            cloneProfile.setTooltip(cloneProfileTooltip);

            String removeProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeItem");
            removeProfile.setText(removeProfileText);
            Tooltip removeProfileTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.removeProfile"));
            removeProfileTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            removeProfile.setTooltip(removeProfileTooltip);

            String importProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importProfile");
            importProfile.setText(importProfileText);
            Tooltip importProfileTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.importProfiles"));
            importProfileTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            importProfile.setTooltip(importProfileTooltip);

            String exportProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.exportProfile");
            exportProfile.setText(exportProfileText);
            Tooltip exportProfileTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.exportProfiles"));
            exportProfileTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            exportProfile.setTooltip(exportProfileTooltip);

            String profileNameColumnText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileName");
            profileNameLabel.setText(profileNameColumnText);
            Tooltip profileNameLabelTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.profileName"));
            profileNameLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            profileNameLabel.setTooltip(profileNameLabelTooltip);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void addProfileOnAction() {
        try {
            String addProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addProfile");
            String enterProfileName = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.enterProfileName");
            Optional<String> profileNameOpt = Utils.OneTextInputDialog(addProfileText, enterProfileName + ":");
            if (profileNameOpt.isPresent() && StringUtils.isNotBlank(profileNameOpt.get())){
                progressIndicator.setVisible(true);

                Task<ProfileDto> task = new Task<ProfileDto>() {
                    @Override
                    protected ProfileDto call() throws Exception {
                        String profileName = profileNameOpt.get().replaceAll(" ", "_");
                        return facade.createNewProfile(profileName);
                    }
                };

                task.setOnSucceeded(wse -> {
                    try {
                        ProfileDto newProfile = task.getValue();
                        profilesTable.getItems().add(newProfile);
                        if (profilesTable.getItems().size() == 1) {
                            Session.getInstance().setActualProfileName(profilesTable.getItems().get(0).getName());
                            propertyService.setProperty("properties/config.properties", "prop.config.lastSelectedProfile", profilesTable.getItems().get(0).getName());
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                    progressIndicator.setVisible(false);
                });

                task.setOnFailed(wse -> {
                    progressIndicator.setVisible(false);
                });

                Thread thread = new Thread(task);
                thread.start();

            }
        } catch (Exception e) {
            String message = "The profile can not be created!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void cloneProfileOnAction() {
        try {
            int selectedIndex = profilesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ProfileDto selectedProfile = profilesTable.getSelectionModel().getSelectedItem();
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.cloneProfile");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.newProfileName");
                Optional<String> newProfileNameOpt = Utils.OneTextInputDialog(headerText + ": " + selectedProfile.getName(), contentText);
                if (newProfileNameOpt.isPresent() && StringUtils.isNotBlank(newProfileNameOpt.get())) {
                    progressIndicator.setVisible(true);

                    Task<ProfileDto> task = new Task<ProfileDto>() {
                        @Override
                        protected ProfileDto call() throws Exception {
                            String newProfileName = newProfileNameOpt.get().replaceAll(" ", "_");
                            return facade.cloneSelectedProfile(selectedProfile.getName(), newProfileName);
                        }
                    };

                    task.setOnSucceeded(wse -> {
                        try {
                            ProfileDto clonedProfile = task.getValue();
                            if (clonedProfile != null) {
                                profilesTable.getItems().add(clonedProfile);
                            } else {
                                logger.warn("The profile can not be cloned in database. Selected profile: " + selectedProfile.getName() + ". New profile name: " + clonedProfile.getName());
                                String notOperationDoneText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                                String profileNotColnedText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotCloned");
                                Utils.warningDialog(notOperationDoneText, profileNotColnedText);
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                        progressIndicator.setVisible(false);
                    });

                    task.setOnFailed(wse -> {
                        progressIndicator.setVisible(false);
                    });

                    Thread thread = new Thread(task);
                    thread.start();
                }
            } else {
                logger.warn("No selected profile to clone");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileToCloneNotSelected");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            String message = "The profile can not be cloned!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void removeProfileOnAction() {
        try {
            int selectedIndex = profilesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ProfileDto selectedProfile = profilesTable.getSelectionModel().getSelectedItem();

                String question = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.deleteProfileQuestion");
                Optional<ButtonType> result = Utils.questionDialog(question, selectedProfile.getName());
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    progressIndicator.setVisible(true);

                    Task<Boolean> task = new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            return facade.deleteSelectedProfile(selectedProfile.getName());
                        }
                    };

                    task.setOnSucceeded(wse -> {
                        try {
                            Boolean profileDeleted = task.getValue();
                            if (profileDeleted != null && profileDeleted) {

                                if (StringUtils.isNotBlank(Session.getInstance().getActualProfileName()) && selectedProfile.getName().equalsIgnoreCase(Session.getInstance().getActualProfileName())) {
                                    Session.getInstance().setActualProfileName(StringUtils.EMPTY);
                                    propertyService.removeProperty("properties/config.properties", "prop.config.lastSelectedProfile");
                                }
                                if (Session.getInstance().getMapsProfile() != null && selectedProfile.getName().equalsIgnoreCase(Session.getInstance().getMapsProfile().getName())) {
                                    Session.getInstance().setMapsProfile(null);
                                }

                                profilesTable.getItems().remove(selectedIndex);
                            } else {
                                logger.warn("The profile can not be deleted from database: " + selectedProfile.getName());
                                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotDeleted");
                                Utils.warningDialog(headerText, contentText);
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                        progressIndicator.setVisible(false);
                    });

                    task.setOnFailed(wse -> {
                        progressIndicator.setVisible(false);
                    });

                    Thread thread = new Thread(task);
                    thread.start();

                }

            } else {
                logger.warn("No selected profile to delete");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotSelected");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            String message = "The profile can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void profileNameColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldProfileName = (String)event.getOldValue();
        String newProfileName = ((String)event.getNewValue()).replaceAll(" ", "_");
        try {
            String steamInstallationFolder = facade.findConfigPropertyValue("prop.config.steamInstallationFolder");
            String epicInstallationFolder = facade.findConfigPropertyValue("prop.config.epicInstallationFolder");
            ProfileDto updatedProfileDto = facade.updateChangedProfile(oldProfileName, newProfileName);
            if (updatedProfileDto != null) {
                if (Session.getInstance().getActualProfile() != null && oldProfileName.equalsIgnoreCase(Session.getInstance().getActualProfile().getName())) {
                    Session.getInstance().setActualProfileName(updatedProfileDto.getName());
                    propertyService.setProperty("properties/config.properties", "prop.config.lastSelectedProfile", newProfileName);
                }
                if (Session.getInstance().getMapsProfile() != null && oldProfileName.equalsIgnoreCase(Session.getInstance().getMapsProfile().getName())) {
                    Session.getInstance().setMapsProfile(updatedProfileDto);
                }
                profilesTable.getItems().remove(edittedRowIndex);
                profilesTable.getItems().add(updatedProfileDto);
                String steamConfigFolder = steamInstallationFolder + "/KFGame/Config/";
                File oldSteamProfileConfigFolder = new File(steamConfigFolder + oldProfileName);
                File newSteamProfileConfigFolder = new File(steamConfigFolder + newProfileName);
                if (oldSteamProfileConfigFolder.exists() && oldSteamProfileConfigFolder.isDirectory() && !newSteamProfileConfigFolder.exists()) {
                    FileUtils.moveDirectory(oldSteamProfileConfigFolder, newSteamProfileConfigFolder);
                }

                String epicConfigFolder = epicInstallationFolder + "/KFGame/Config/";
                File oldEpicProfileConfigFolder = new File(epicConfigFolder + oldProfileName);
                File newEpicProfileConfigFolder = new File(epicConfigFolder + newProfileName);
                if (oldEpicProfileConfigFolder.exists() && oldEpicProfileConfigFolder.isDirectory() && !newEpicProfileConfigFolder.exists()) {
                    FileUtils.moveDirectory(oldEpicProfileConfigFolder, newEpicProfileConfigFolder);
                }
            } else {
                profilesTable.refresh();
                logger.warn("The profile can not be renamed in database: [old profile name = " + oldProfileName + ", new profile name = " + newProfileName + "]");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotRenamed");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            profilesTable.refresh();
            String message = "The profile can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void importProfileOnAction() {
        try {
            FileChooser fileChooser = new FileChooser();
            String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importProfiles");
            fileChooser.setTitle(message + " ...");
            File selectedFile = fileChooser.showOpenDialog(MainApplication.getPrimaryStage());
            if (selectedFile != null) {
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfilesToImport");
                StringBuffer errorMessage = new StringBuffer();

                Optional<ButtonType> result = facade.questionToImportEntitiesFromFile();
                if (!result.isPresent() || result.get().equals(ButtonType.CANCEL)) {
                    return;
                }

                progressIndicator.setVisible(true);

                Task<Properties> entitiesTask = new Task<Properties>() {
                    @Override
                    protected Properties call() throws Exception {
                        return facade.importEntitiesFromFile(selectedFile);
                    }
                };

                String finalMessage = message;
                entitiesTask.setOnSucceeded(entitiesWse -> {
                    progressIndicator.setVisible(false);
                    Properties properties = entitiesTask.getValue();
                    List<Profile> selectedProfileList = new ArrayList<Profile>();
                    try {
                        selectedProfileList = facade.questionToImportProfilesFromFile(properties, finalMessage);
                        if (selectedProfileList == null || selectedProfileList.isEmpty()) {
                            return;
                        }
                        ObservableList<ProfileDto> importedProfiles = facade.importProfilesFromFile(
                                selectedProfileList,
                                properties, errorMessage
                        );

                        if (importedProfiles == null || importedProfiles.isEmpty()) {
                            progressIndicator.setVisible(false);
                            return;
                        }

                        for (ProfileDto importedProfile: importedProfiles) {
                            try {
                                profilesTable.getItems().add(importedProfile);
                                facade.createConfigFolder(importedProfile.getName());
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }

                        if (StringUtils.isBlank(errorMessage)) {
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.OperationDone");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profilesImported");
                            Utils.infoDialog(headerText, contentText + ":\n" + selectedFile.getAbsolutePath());
                        }

                        if (StringUtils.isNotBlank(errorMessage)) {
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profilesNotImported");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.seeLauncherLog");
                            Utils.warningDialog(headerText + ":", errorMessage.toString() + "\n" + contentText);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                });

                entitiesTask.setOnFailed(entitiesWse -> {
                    progressIndicator.setVisible(false);
                });

                new Thread(entitiesTask).start();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            progressIndicator.setVisible(false);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void exportProfileOnAction() {
        try {
            String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfilesToExport");
            List<ProfileToDisplay> selectedProfiles = facade.selectProfilesToBeExported(message);

            if (selectedProfiles != null && !selectedProfiles.isEmpty()) {
                FileChooser fileChooser = new FileChooser();
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.exportProfiles");
                fileChooser.setTitle(message + " ...");
                File selectedFile = fileChooser.showSaveDialog(MainApplication.getPrimaryStage());
                if (selectedFile != null) {
                    progressIndicator.setVisible(true);

                    Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            facade.exportProfilesToFile(selectedProfiles, selectedFile);
                            return null;
                        }
                    };

                    task.setOnSucceeded(wse -> {
                        try {
                            progressIndicator.setVisible(false);
                            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.OperationDone");
                            String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profilesExported");
                            Utils.infoDialog(headerText, contentText + ":\n" + selectedFile.getAbsolutePath());
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    });

                    task.setOnFailed(entitiesWse -> {
                        progressIndicator.setVisible(false);
                    });

                    new Thread(task).start();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
