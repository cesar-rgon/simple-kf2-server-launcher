package stories.gametypesedition;

import dtos.SelectDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameTypesEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(GameTypesEditionController.class);
    private final GameTypesEditionFacade facade;

    @FXML private TableView<SelectDto> gameTypesTable;
    @FXML private TableColumn<SelectDto, String> gameTypeCodeColumn;
    @FXML private TableColumn<SelectDto, String> gameTypeDescriptionColumn;

    public GameTypesEditionController() {
        this.facade = new GameTypesEditionFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            gameTypeCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            gameTypeCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            gameTypeDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            gameTypeDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            gameTypesTable.setItems(facade.listAllGameTypes());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void gameTypeCodeColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldGameTypeCode = (String)event.getOldValue();
        String newGameTypeCode = (String)event.getNewValue();
        try {
            SelectDto updatedGameTypeDto = facade.updateChangedGameTypeCode(oldGameTypeCode, newGameTypeCode);
            if (updatedGameTypeDto != null) {
                gameTypesTable.getItems().remove(edittedRowIndex);
                gameTypesTable.getItems().add(updatedGameTypeDto);
            } else {
                gameTypesTable.refresh();
                String message = "The game type can not be renamed in database: [old game type code = " + oldGameTypeCode + ", new game type code = " + newGameTypeCode + "]";
                logger.warn(message);
                Utils.warningDialog("Update operation is aborted!", message);
            }
        } catch (Exception e) {
            gameTypesTable.refresh();
            String message = "The game type can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void gameTypeDescriptionColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldGameTypeDescription = (String)event.getOldValue();
        String newGameTypeDescription = (String)event.getNewValue();
        try {
            String code = gameTypesTable.getItems().get(edittedRowIndex).getKey();
            SelectDto updatedGameTypeDto = facade.updateChangedGameTypeDescription(code, oldGameTypeDescription, newGameTypeDescription);
            if (updatedGameTypeDto != null) {
                gameTypesTable.getItems().remove(edittedRowIndex);
                gameTypesTable.getItems().add(updatedGameTypeDto);
            } else {
                gameTypesTable.refresh();
                String message = "The game type can not be renamed in database: [old game type description = " + oldGameTypeDescription + ", new game type description = " + newGameTypeDescription + "]";
                logger.warn(message);
                Utils.warningDialog("Update operation is aborted!", message);
            }
        } catch (Exception e) {
            gameTypesTable.refresh();
            String message = "The game type can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void addGameTypeOnAction() {
        try {
            Optional<SelectDto> result = Utils.TwoTextInputsDialog();
            if (result.isPresent() && StringUtils.isNotBlank(result.get().getKey()) && StringUtils.isNotBlank(result.get().getValue())) {
                String code = result.get().getKey();
                String description = result.get().getValue();
                gameTypesTable.getItems().add(facade.createNewGameType(code, description));
            }
        } catch (Exception e) {
            String message = "The game type can not be created!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void removeGameTypeOnAction() {
        try {
            int selectedIndex = gameTypesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                SelectDto selectedGameType = gameTypesTable.getSelectionModel().getSelectedItem();
                if (facade.deleteSelectedGameType(selectedGameType.getKey())) {
                    gameTypesTable.getItems().remove(selectedIndex);
                } else {
                    String message = "The game type can not be deleted from database";
                    logger.warn(message);
                    Utils.warningDialog(message, "Delete operation is aborted!");
                }
            } else {
                String message = "No selected game type to delete";
                logger.warn(message);
                Utils.warningDialog(message, "Delete operation is aborted!");
            }
        } catch (Exception e) {
            String message = "The game type can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }
}
