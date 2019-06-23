package stories.profilesedition;

import dtos.ProfileDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldTableCell;
import utils.Utils;

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
            profileNameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
            profileNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            profilesTable.setItems(facade.listAllProfiles());
        } catch (SQLException e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void addProfileOnAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Simple Killing Floor 2 Server Launcher");
        dialog.setHeaderText("Add a new profile");
        dialog.setContentText("Please enter profile name:");

        Optional<String> profileNameOpt = dialog.showAndWait();
        if (profileNameOpt.isPresent() && !profileNameOpt.get().isEmpty()){
            //profilesTable.getItems().add(new ProfileDto(profileNameOpt.get()));
        }

    }

    @FXML
    private void removeProfileOnAction() {
        int selectedIndex = profilesTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            profilesTable.getItems().remove(selectedIndex);
        } else {
            Utils.errorDialog("No selected profile", "Delete operation is aborted!", null);
        }
    }
}
