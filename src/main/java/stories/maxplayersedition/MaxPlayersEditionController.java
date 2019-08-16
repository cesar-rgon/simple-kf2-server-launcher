package stories.maxplayersedition;

import dtos.SelectDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import stories.difficultiesedition.DifficultiesEditionController;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaxPlayersEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MaxPlayersEditionController.class);
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
            logger.error(e.getMessage(), e);
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
                String message = "The max. players can not be renamed in database: [old max. players code = " + oldMaxPlayersCode + ", new max. players code = " + newMaxPlayersCode + "]";
                logger.warn(message);
                Utils.warningDialog("Update operation is aborted!", message);
            }
        } catch (Exception e) {
            maxPlayersTable.refresh();
            String message = "The max. players can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void maxPlayersDescriptionColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldMaxPlayersDescription = (String)event.getOldValue();
        String newMaxPlayersDescription = (String)event.getNewValue();
        try {
            String code = maxPlayersTable.getItems().get(edittedRowIndex).getKey();
            SelectDto updatedLengthDto = facade.updateChangedMaxPlayersDescription(code, oldMaxPlayersDescription, newMaxPlayersDescription);
            if (updatedLengthDto != null) {
                maxPlayersTable.getItems().remove(edittedRowIndex);
                maxPlayersTable.getItems().add(updatedLengthDto);
            } else {
                maxPlayersTable.refresh();
                String message = "The max. players can not be renamed in database: [old max. players description = " + oldMaxPlayersDescription + ", new max. players description = " + newMaxPlayersDescription + "]";
                logger.warn(message);
                Utils.warningDialog("Update operation is aborted!", message);
            }
        } catch (Exception e) {
            maxPlayersTable.refresh();
            String message = "The max. players can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void addMaxPlayersOnAction() {
        try {
            Optional<SelectDto> result = Utils.TwoTextInputsDialog();
            if (result.isPresent() && StringUtils.isNotBlank(result.get().getKey()) && StringUtils.isNotBlank(result.get().getValue())) {
                String code = result.get().getKey();
                String description = result.get().getValue();
                maxPlayersTable.getItems().add(facade.createNewMaxPlayers(code, description));
            }
        } catch (Exception e) {
            String message = "The max. players can not be created!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
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
                    String message = "The max. players can not be deleted from database";
                    logger.warn(message);
                    Utils.warningDialog(message, "Delete operation is aborted!");
                }
            } else {
                String message = "No selected max. players to delete";
                logger.warn(message);
                Utils.warningDialog(message, "Delete operation is aborted!");
            }
        } catch (Exception e) {
            String message = "The max. players can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }
}
