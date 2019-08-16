package stories.difficultiesedition;

import dtos.SelectDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.listener.TimeListener;
import pojos.session.Session;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DifficultiesEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(DifficultiesEditionController.class);
    private final DifficultiesEditionFacade facade;

    @FXML private TableView<SelectDto> difficultiesTable;
    @FXML private TableColumn<SelectDto, String> difficultyCodeColumn;
    @FXML private TableColumn<SelectDto, String> difficultyDescriptionColumn;

    public DifficultiesEditionController() {
        this.facade = new DifficultiesEditionFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            difficultyCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            difficultyCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            difficultyDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            difficultyDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            difficultiesTable.setItems(facade.listAllDifficulties());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void difficultyCodeColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldDifficultyCode = (String)event.getOldValue();
        String newDifficultyCode = (String)event.getNewValue();
        try {
            SelectDto updatedDifficultyDto = facade.updateChangedDifficultyCode(oldDifficultyCode, newDifficultyCode);
            if (updatedDifficultyDto != null) {
                difficultiesTable.getItems().remove(edittedRowIndex);
                difficultiesTable.getItems().add(updatedDifficultyDto);
            } else {
                difficultiesTable.refresh();
                String message = "The difficulty can not be renamed in database: [old difficulty code = " + oldDifficultyCode + ", new difficulty code = " + newDifficultyCode + "]";
                logger.warn(message);
                Utils.warningDialog("Update operation is aborted!", message);
            }
        } catch (Exception e) {
            difficultiesTable.refresh();
            String message = "The difficulty can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void difficultyDescriptionColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldDifficultyDescription = (String)event.getOldValue();
        String newDifficultyDescription = (String)event.getNewValue();
        try {
            String code = difficultiesTable.getItems().get(edittedRowIndex).getKey();
            SelectDto updatedGameTypeDto = facade.updateChangedDifficultyDescription(code, oldDifficultyDescription, newDifficultyDescription);
            if (updatedGameTypeDto != null) {
                difficultiesTable.getItems().remove(edittedRowIndex);
                difficultiesTable.getItems().add(updatedGameTypeDto);
            } else {
                difficultiesTable.refresh();
                String message = "The difficulty can not be renamed in database: [old difficulty description = " + oldDifficultyDescription + ", new difficulty description = " + newDifficultyDescription + "]";
                logger.warn(message);
                Utils.warningDialog("Update operation is aborted!", message);
            }
        } catch (Exception e) {
            difficultiesTable.refresh();
            String message = "The difficulty can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void addDifficultyOnAction() {
        try {
            Optional<SelectDto> result = Utils.TwoTextInputsDialog();
            if (result.isPresent() && StringUtils.isNotBlank(result.get().getKey()) && StringUtils.isNotBlank(result.get().getValue())) {
                String code = result.get().getKey();
                String description = result.get().getValue();
                difficultiesTable.getItems().add(facade.createNewDifficulty(code, description));
            }
        } catch (Exception e) {
            String message = "The difficulty can not be created!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void removeDifficultyOnAction() {
        try {
            int selectedIndex = difficultiesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                SelectDto selectedDifficulty = difficultiesTable.getSelectionModel().getSelectedItem();
                if (facade.deleteSelectedDifficulty(selectedDifficulty.getKey())) {
                    difficultiesTable.getItems().remove(selectedIndex);
                } else {
                    String message = "The difficulty can not be deleted from database";
                    logger.warn(message);
                    Utils.warningDialog(message, "Delete operation is aborted!");
                }
            } else {
                String message = "No selected difficulty to delete";
                logger.warn(message);
                Utils.warningDialog(message, "Delete operation is aborted!");
            }
        } catch (Exception e) {
            String message = "The difficulty can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }
}
