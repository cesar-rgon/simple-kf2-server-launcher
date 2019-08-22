package stories.profilesedition;

import constants.Constants;
import dtos.ProfileDto;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import start.MainApplication;
import stories.difficultiesedition.DifficultiesEditionController;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfilesEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(ProfilesEditionController.class);
    private final ProfilesEditionFacade facade;

    @FXML private TableView<ProfileDto> profilesTable;
    @FXML private TableColumn<ProfileDto, String> profileNameColumn;

    public ProfilesEditionController() {
        this.facade = new ProfilesEditionFacadeImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            profileNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            profilesTable.setItems(facade.listAllProfiles());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void addProfileOnAction() {
        try {
            String installationFolder = facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER);
            if (StringUtils.isBlank(installationFolder)) {
                String message = "You need to define an installation folder in Install/Update section.";
                logger.warn(message);
                Utils.warningDialog("You can not add a new profile!",message);
                return;
            }
            Optional<String> profileNameOpt = Utils.OneTextInputDialog("Add a new profile", "Enter profile name:");
            if (profileNameOpt.isPresent() && StringUtils.isNotBlank(profileNameOpt.get())){
                String profileName = profileNameOpt.get().replaceAll(" ", "_");
                profilesTable.getItems().add(facade.createNewProfile(profileName));
                Kf2Common kf2Common = Kf2Factory.getInstance();
                kf2Common.createConfigFolder(installationFolder, profileName);
            }
        } catch (Exception e) {
            String message = "The profile can not be created!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void cloneProfileOnAction() {
        try {
            String installationFolder = facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER);
            if (StringUtils.isBlank(installationFolder)) {
                String message = "You need to define an installation folder in Install/Update section.";
                logger.warn(message);
                Utils.warningDialog("You can not clone a profile!", message);
                return;
            }
            int selectedIndex = profilesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ProfileDto selectedProfile = profilesTable.getSelectionModel().getSelectedItem();
                Optional<String> newProfileNameOpt = Utils.OneTextInputDialog("Clone the profile: " + selectedProfile.getName(), "Enter a new profile name:");
                if (newProfileNameOpt.isPresent() && StringUtils.isNotBlank(newProfileNameOpt.get())) {
                    String newProfileName = newProfileNameOpt.get().replaceAll(" ", "_");
                    ProfileDto clonedProfile = facade.cloneSelectedProfile(selectedProfile.getName(), newProfileName);
                    if (clonedProfile != null) {
                        profilesTable.getItems().add(clonedProfile);
                        Kf2Common kf2Common = Kf2Factory.getInstance();
                        kf2Common.createConfigFolder(installationFolder, newProfileName);
                    } else {
                        String message = "The profile can not be cloned in database";
                        logger.warn(message);
                        Utils.warningDialog(message, "Clone operation is aborted!");
                    }
                }
            } else {
                String message = "No selected profile to clone";
                logger.warn(message);
                Utils.warningDialog(message, "Clone operation is aborted!");
            }
        } catch (Exception e) {
            String message = "The profile can not be cloned!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void removeProfileOnAction() {
        try {
            String installationFolder = facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER);
            if (StringUtils.isBlank(installationFolder)) {
                String message = "You need to define an installation folder in Install/Update section.";
                logger.warn(message);
                Utils.warningDialog("You can not remove a profile!",message);
                return;
            }
            int selectedIndex = profilesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ProfileDto selectedProfile = profilesTable.getSelectionModel().getSelectedItem();
                if (facade.deleteSelectedProfile(selectedProfile.getName())) {
                    profilesTable.getItems().remove(selectedIndex);
                    File profileConfigFolder = new File(facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER) + "/KFGame/Config/" + selectedProfile.getName());
                    FileUtils.deleteDirectory(profileConfigFolder);
                } else {
                    String message = "The profile can not be deleted from database";
                    logger.warn(message);
                    Utils.warningDialog(message, "Delete operation is aborted!");
                }
            } else {
                String message = "No selected profile to delete";
                logger.warn(message);
                Utils.warningDialog(message, "Delete operation is aborted!");
            }
        } catch (Exception e) {
            String message = "The profile can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void profileNameColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldProfileName = (String)event.getOldValue();
        String newProfileName = ((String)event.getNewValue()).replaceAll(" ", "_");
        try {
            String installationFolder = facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER);
            if (StringUtils.isBlank(installationFolder)) {
                String message = "You need to define an installation folder in Install/Update section.";
                logger.warn(message);
                Utils.warningDialog("You can not edit a profile!",message);
                return;
            }
            ProfileDto updatedProfileDto = facade.updateChangedProfile(oldProfileName, newProfileName);
            if (updatedProfileDto != null) {
                if (Session.getInstance().getActualProfile() != null && oldProfileName.equalsIgnoreCase(Session.getInstance().getActualProfile().getName())) {
                    Session.getInstance().setActualProfile(updatedProfileDto);
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
                String message = "The profile can not be renamed in database: [old profile name = " + oldProfileName + ", new profile name = " + newProfileName + "]";
                logger.warn(message);
                Utils.warningDialog("Update operation is aborted!", message);
            }
        } catch (Exception e) {
            profilesTable.refresh();
            String message = "The profile can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void importProfileOnAction() {
        try {
            String installationFolder = facade.findPropertyValue(Constants.CONFIG_INSTALLATION_FOLDER);
            if (StringUtils.isBlank(installationFolder)) {
                String message = "You need to define an installation folder in Install/Update section.";
                logger.warn(message);
                Utils.warningDialog("You can not import profiles!", message);
                return;
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import profiles from a file ...");
            File selectedFile = fileChooser.showOpenDialog(MainApplication.getPrimaryStage());
            if (selectedFile != null) {
                ObservableList<ProfileDto> profilesToBeImported = facade.getProfilesToBeImportedFromFile(selectedFile);
                List<ProfileDto> selectedProfiles = Utils.selectProfilesDialog("Select profiles to be imported:", profilesToBeImported, profilesToBeImported);
                if (selectedProfiles != null && !selectedProfiles.isEmpty()) {
                    List<ProfileDto> insertedProfileList = facade.insertProfiles(selectedProfiles);
                    for (ProfileDto insertedProfile: insertedProfileList) {
                        profilesTable.getItems().add(insertedProfile);
                        Kf2Common kf2Common = Kf2Factory.getInstance();
                        kf2Common.createConfigFolder(installationFolder, insertedProfile.getName());
                    }

                    if (insertedProfileList.size() == selectedProfiles.size()) {
                        Utils.infoDialog("Operation complete!", "All the profiles have been imported from file:\n" + selectedFile.getAbsolutePath());
                    } else {
                        StringBuffer errorMessage = new StringBuffer();
                        for (ProfileDto selectedProfile: selectedProfiles) {
                            if (!insertedProfileList.contains(selectedProfile)) {
                                errorMessage.append(selectedProfile.getName() + "\n");
                            }
                        }

                        Utils.warningDialog("Next profiles could not be imported:", errorMessage.toString() + "\nFor more details see file launcher.log");
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void exportProfileOnAction() {
        try {
            ObservableList<ProfileDto> allProfiles = facade.listAllProfiles();
            List<ProfileDto> selectedProfiles = Utils.selectProfilesDialog("Select profiles to be exported:", allProfiles, allProfiles);
            if (selectedProfiles != null && !selectedProfiles.isEmpty()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Export profiles to a file and save as ...");
                File selectedFile = fileChooser.showSaveDialog(MainApplication.getPrimaryStage());
                if (selectedFile != null) {
                    facade.exportProfilesToFile(selectedProfiles, selectedFile);
                    Utils.infoDialog("Operation complete!", "The profiles have been exported to file:\n" + selectedFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }
}
