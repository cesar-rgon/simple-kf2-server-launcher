package stories.profilesedition;

import constants.Constants;
import dtos.ProfileDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfilesEditionController implements Initializable {

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
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void addProfileOnAction() {
        try {
            String installationFolder = facade.findPropertyValue(Constants.KEY_INSTALLATION_FOLDER);
            if (StringUtils.isBlank(installationFolder)) {
                Utils.warningDialog("You can not add a new profile!","You need to define an installation folder in Install/Update section.");
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
            Utils.errorDialog("The profile can not be created!", "See stacktrace for more details", e);
        }
    }

    @FXML
    private void removeProfileOnAction() {
        try {
            String installationFolder = facade.findPropertyValue(Constants.KEY_INSTALLATION_FOLDER);
            if (StringUtils.isBlank(installationFolder)) {
                Utils.warningDialog("You can not remove a profile!","You need to define an installation folder in Install/Update section.");
                return;
            }
            int selectedIndex = profilesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                ProfileDto selectedProfile = profilesTable.getSelectionModel().getSelectedItem();
                if (facade.deleteSelectedProfile(selectedProfile.getName())) {
                    profilesTable.getItems().remove(selectedIndex);
                    File profileConfigFolder = new File(facade.findPropertyValue(Constants.KEY_INSTALLATION_FOLDER) + "/KFGame/Config/" + selectedProfile.getName());
                    FileUtils.deleteDirectory(profileConfigFolder);
                } else {
                    Utils.errorDialog("The profile can not be deleted from database", "Delete operation is aborted!", null);
                }
            } else {
                Utils.warningDialog("No selected profile", "Delete operation is aborted!");
            }
        } catch (Exception e) {
            Utils.errorDialog("The profile can not be deleted!", "See stacktrace for more details", e);
        }
    }

    @FXML
    private void profileNameColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldProfileName = (String)event.getOldValue();
        String newProfileName = ((String)event.getNewValue()).replaceAll(" ", "_");
        try {
            String installationFolder = facade.findPropertyValue(Constants.KEY_INSTALLATION_FOLDER);
            if (StringUtils.isBlank(installationFolder)) {
                Utils.warningDialog("You can not edit a profile!","You need to define an installation folder in Install/Update section.");
                return;
            }
            ProfileDto updatedProfileDto = facade.updateChangedProfile(oldProfileName, newProfileName);
            if (updatedProfileDto != null) {
                profilesTable.getItems().remove(edittedRowIndex);
                profilesTable.getItems().add(updatedProfileDto);
                String configFolder = installationFolder + "/KFGame/Config/";
                File oldProfileConfigFolder = new File(configFolder + oldProfileName);
                if (oldProfileConfigFolder.exists() && oldProfileConfigFolder.isDirectory()) {
                    File newProfileConfigFolder = new File(configFolder + newProfileName);
                    FileUtils.moveDirectory(oldProfileConfigFolder, newProfileConfigFolder);
                }
            } else {
                profilesTable.refresh();
                Utils.errorDialog("The profile can not be renamed in database", "Update operation is aborted!", null);
            }
        } catch (Exception e) {
            profilesTable.refresh();
            Utils.errorDialog("The profile can not be updated!", "See stacktrace for more details", e);
        }
    }
}
