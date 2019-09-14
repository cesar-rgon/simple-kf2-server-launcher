package stories.profilesedition;

import dtos.ProfileDto;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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

            String addProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addItem");
            addProfile.setText(addProfileText);
            addProfile.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.addProfile")));

            String cloneProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.cloneItem");
            cloneProfile.setText(cloneProfileText);
            cloneProfile.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.cloneProfile")));

            String removeProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeItem");
            removeProfile.setText(removeProfileText);
            removeProfile.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.removeProfile")));

            String importProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.importProfile");
            importProfile.setText(importProfileText);
            importProfile.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.importProfiles")));

            String exportProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.exportProfile");
            exportProfile.setText(exportProfileText);
            exportProfile.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.exportProfiles")));

            String profileNameColumnText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileName");
            profileNameLabel.setText(profileNameColumnText);
            profileNameLabel.setTooltip(new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.profileName")));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void addProfileOnAction() {
        try {
            String installationFolder = facade.findPropertyValue("prop.config.installationFolder");
            if (StringUtils.isBlank(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            String addProfileText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addProfile");
            String enterProfileName = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.enterProfileName");
            Optional<String> profileNameOpt = Utils.OneTextInputDialog(addProfileText, enterProfileName + ":");
            if (profileNameOpt.isPresent() && StringUtils.isNotBlank(profileNameOpt.get())){
                String profileName = profileNameOpt.get().replaceAll(" ", "_");
                profilesTable.getItems().add(facade.createNewProfile(profileName));
                if (profilesTable.getItems().size() == 1) {
                    Session.getInstance().setActualProfile(profilesTable.getItems().get(0));
                    propertyService.setProperty("properties/config.properties", "prop.config.lastSelectedProfile", profilesTable.getItems().get(0).getName());
                }

                Kf2Common kf2Common = Kf2Factory.getInstance();
                kf2Common.createConfigFolder(installationFolder, profileName);
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
            String installationFolder = facade.findPropertyValue("prop.config.installationFolder");
            if (StringUtils.isBlank(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            int selectedIndex = profilesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ProfileDto selectedProfile = profilesTable.getSelectionModel().getSelectedItem();
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.cloneProfile");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.newProfileName");
                Optional<String> newProfileNameOpt = Utils.OneTextInputDialog(headerText + ": " + selectedProfile.getName(), contentText);
                if (newProfileNameOpt.isPresent() && StringUtils.isNotBlank(newProfileNameOpt.get())) {
                    String newProfileName = newProfileNameOpt.get().replaceAll(" ", "_");
                    ProfileDto clonedProfile = facade.cloneSelectedProfile(selectedProfile.getName(), newProfileName);
                    if (clonedProfile != null) {
                        profilesTable.getItems().add(clonedProfile);
                        Kf2Common kf2Common = Kf2Factory.getInstance();
                        kf2Common.createConfigFolder(installationFolder, newProfileName);
                    } else {
                        logger.warn("The profile can not be cloned in database. Selected profile: "+ selectedProfile.getName() + ". New profile name: " + newProfileName);
                        headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                        contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotCloned");
                        Utils.warningDialog(headerText, contentText);
                    }
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
            String installationFolder = facade.findPropertyValue("prop.config.installationFolder");
            if (StringUtils.isBlank(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            int selectedIndex = profilesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ProfileDto selectedProfile = profilesTable.getSelectionModel().getSelectedItem();
                if (facade.deleteSelectedProfile(selectedProfile.getName(), installationFolder)) {
                    profilesTable.getItems().remove(selectedIndex);
                    if (Session.getInstance().getActualProfile() != null && selectedProfile.getName().equalsIgnoreCase(Session.getInstance().getActualProfile().getName())) {
                        if (profilesTable.getItems().size() > 0) {
                            Session.getInstance().setActualProfile(profilesTable.getItems().get(0));
                            propertyService.setProperty("properties/config.properties", "prop.config.lastSelectedProfile", profilesTable.getItems().get(0).getName());
                        } else {
                            Session.getInstance().setActualProfile(null);
                            propertyService.removeProperty("properties/config.properties", "prop.config.lastSelectedProfile");
                        }
                    }
                    File profileConfigFolder = new File(facade.findPropertyValue("prop.config.installationFolder") + "/KFGame/Config/" + selectedProfile.getName());
                    FileUtils.deleteDirectory(profileConfigFolder);
                } else {
                    logger.warn("The profile can not be deleted from database: " + selectedProfile.getName());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotDeleted");
                    Utils.warningDialog(headerText, contentText);
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
            String installationFolder = facade.findPropertyValue("prop.config.installationFolder");
            if (StringUtils.isBlank(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            ProfileDto updatedProfileDto = facade.updateChangedProfile(oldProfileName, newProfileName);
            if (updatedProfileDto != null) {
                if (Session.getInstance().getActualProfile() != null && oldProfileName.equalsIgnoreCase(Session.getInstance().getActualProfile().getName())) {
                    Session.getInstance().setActualProfile(updatedProfileDto);
                    propertyService.setProperty("properties/config.properties", "prop.config.lastSelectedProfile", newProfileName);
                }
                profilesTable.getItems().remove(edittedRowIndex);
                profilesTable.getItems().add(updatedProfileDto);
                String configFolder = installationFolder + "/KFGame/Config/";
                File oldProfileConfigFolder = new File(configFolder + oldProfileName);
                File newProfileConfigFolder = new File(configFolder + newProfileName);
                if (oldProfileConfigFolder.exists() && oldProfileConfigFolder.isDirectory() && !newProfileConfigFolder.exists()) {
                    FileUtils.moveDirectory(oldProfileConfigFolder, newProfileConfigFolder);
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
            String installationFolder = facade.findPropertyValue("prop.config.installationFolder");
            if (StringUtils.isBlank(installationFolder)) {
                logger.warn("Installation folder can not be empty and can not contains whitespaces. Define in Install/Update section: " + installationFolder);
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
                return;
            }
            FileChooser fileChooser = new FileChooser();
            String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.importProfiles");
            fileChooser.setTitle(message + " ...");
            File selectedFile = fileChooser.showOpenDialog(MainApplication.getPrimaryStage());
            if (selectedFile != null) {
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfilesToImport");
                StringBuffer errorMessage = new StringBuffer();
                ObservableList<ProfileDto> importedProfiles = facade.addProfilesToBeImportedFromFile(selectedFile, message, errorMessage);
                if (importedProfiles != null && !importedProfiles.isEmpty()) {

                    for (ProfileDto importedProfile: importedProfiles) {
                        profilesTable.getItems().add(importedProfile);
                        Kf2Common kf2Common = Kf2Factory.getInstance();
                        kf2Common.createConfigFolder(installationFolder, importedProfile.getName());
                    }
                }

                if (StringUtils.isBlank(errorMessage)) {
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.OperationDone");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profilesImported");
                    Utils.infoDialog(headerText, contentText + ":\n" + selectedFile.getAbsolutePath());
                } else {
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profilesNotImported");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.seeLauncherLog");
                    Utils.warningDialog(headerText + ":", errorMessage.toString() + "\n" + contentText);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void exportProfileOnAction() {
        try {
            String message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfilesToExport");
            List<ProfileDto> selectedProfiles = facade.selectProfiles(message);

            if (selectedProfiles != null && !selectedProfiles.isEmpty()) {
                FileChooser fileChooser = new FileChooser();
                message = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.exportProfiles");
                fileChooser.setTitle(message + " ...");
                File selectedFile = fileChooser.showSaveDialog(MainApplication.getPrimaryStage());
                if (selectedFile != null) {
                    facade.exportProfilesToFile(selectedProfiles, selectedFile);
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.OperationDone");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profilesExported");
                    Utils.infoDialog(headerText, contentText + ":\n" + selectedFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
