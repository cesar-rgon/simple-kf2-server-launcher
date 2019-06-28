package stories.maxplayersedition;

import dtos.SelectDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaxPlayersEditionController implements Initializable {

    private final MaxPlayersEditionFacade facade;

    @FXML private TableView<SelectDto> maxPlayersTable;
    @FXML private TableColumn<SelectDto, String> maxPlayersCodeColumn;
    @FXML private TableColumn<SelectDto, String> maxPlayersDescriptionColumn;

    public MaxPlayersEditionController() {
        super();
        this.facade = new MaxPlayersEditionFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            maxPlayersCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            maxPlayersCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            maxPlayersDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            maxPlayersDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            maxPlayersTable.setItems(facade.listAllMaxPlayers());
        } catch (SQLException e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }


    @FXML
    private void maxPlayersCodeColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldMaxPlayersCode = (String)event.getOldValue();
        String newMaxPlayersCode = (String)event.getNewValue();
        try {
            SelectDto updatedMaxPlayersDto = facade.updateChangedMaxPlayersCode(oldMaxPlayersCode, newMaxPlayersCode);
            if (updatedMaxPlayersDto != null) {
                maxPlayersTable.getItems().remove(edittedRowIndex);
                maxPlayersTable.getItems().add(updatedMaxPlayersDto);
            } else {
                maxPlayersTable.refresh();
                Utils.errorDialog("The max. players can not be renamed in database", "Update operation is aborted!", null);
            }
        } catch (SQLException e) {
            maxPlayersTable.refresh();
            Utils.errorDialog("The max. players can not be updated!", "See stacktrace for more details", e);
        }
    }

    @FXML
    private void maxPlayersDescriptionColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldMaxPlayersDescription = (String)event.getOldValue();
        String newMaxPlayersDescription = (String)event.getNewValue();
        try {
            String code = maxPlayersTable.getItems().get(edittedRowIndex).getKey();
            SelectDto selectedLanguage = Session.getInstance().getActualProfile().getLanguage();
            SelectDto updatedLengthDto = facade.updateChangedMaxPlayersDescription(code, oldMaxPlayersDescription, newMaxPlayersDescription, selectedLanguage);
            if (updatedLengthDto != null) {
                maxPlayersTable.getItems().remove(edittedRowIndex);
                maxPlayersTable.getItems().add(updatedLengthDto);
            } else {
                maxPlayersTable.refresh();
                Utils.errorDialog("The max. players can not be renamed in database", "Update operation is aborted!", null);
            }
        } catch (SQLException e) {
            maxPlayersTable.refresh();
            Utils.errorDialog("The max. players can not be updated!", "See stacktrace for more details", e);
        }
    }

    @FXML
    private void addMaxPlayersOnAction() {
        try {
            Optional<SelectDto> result = Utils.TwoTextInputsDialog();
            if (result.isPresent() && StringUtils.isNotBlank(result.get().getKey()) && StringUtils.isNotBlank(result.get().getValue())) {
                String code = result.get().getKey();
                String description = result.get().getValue();
                SelectDto selectedLanguage = Session.getInstance().getActualProfile().getLanguage();
                maxPlayersTable.getItems().add(facade.createNewMaxPlayers(code, description, selectedLanguage));
            }
        } catch (SQLException e) {
            Utils.errorDialog("The game type can not be created!", "See stacktrace for more details", e);
        }
    }

    @FXML
    private void removeMaxPlayersOnAction() {
        try {
            int selectedIndex = maxPlayersTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                SelectDto selectedMaxPlayers = maxPlayersTable.getSelectionModel().getSelectedItem();
                if (facade.deleteSelectedMaxPlayers(selectedMaxPlayers.getKey())) {
                    maxPlayersTable.getItems().remove(selectedIndex);
                } else {
                    Utils.errorDialog("The max. players can not be deleted from database", "Delete operation is aborted!", null);
                }
            } else {
                Utils.errorDialog("No selected max. players", "Delete operation is aborted!", null);
            }
        } catch (SQLException e) {
            Utils.errorDialog("The max. players can not be deleted!", "See stacktrace for more details", e);
        }
    }
}
