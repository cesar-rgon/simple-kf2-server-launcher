package stories.lengthedition;

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

public class LengthEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(LengthEditionController.class);
    private final LengthEditionFacade facade;

    @FXML private TableView<SelectDto> lengthTable;
    @FXML private TableColumn<SelectDto, String> lengthCodeColumn;
    @FXML private TableColumn<SelectDto, String> lengthDescriptionColumn;

    public LengthEditionController(){
        super();
        this.facade = new LengthEditionFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lengthCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            lengthCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            lengthDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            lengthDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            lengthTable.setItems(facade.listAllLength());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void lengthCodeColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldLengthCode = (String)event.getOldValue();
        String newLengthCode = (String)event.getNewValue();
        try {
            SelectDto updatedLengthDto = facade.updateChangedLengthCode(oldLengthCode, newLengthCode);
            if (updatedLengthDto != null) {
                lengthTable.getItems().remove(edittedRowIndex);
                lengthTable.getItems().add(updatedLengthDto);
            } else {
                lengthTable.refresh();
                String message = "The length can not be renamed in database: [old length code = " + oldLengthCode + ", new length code = " + newLengthCode + "]";
                logger.warn(message);
                Utils.warningDialog("Update operation is aborted!", message);
            }
        } catch (Exception e) {
            lengthTable.refresh();
            String message = "The length can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void lengthDescriptionColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldLengthDescription = (String)event.getOldValue();
        String newLengthDescription = (String)event.getNewValue();
        try {
            String code = lengthTable.getItems().get(edittedRowIndex).getKey();
            SelectDto updatedLengthDto = facade.updateChangedLengthDescription(code, oldLengthDescription, newLengthDescription);
            if (updatedLengthDto != null) {
                lengthTable.getItems().remove(edittedRowIndex);
                lengthTable.getItems().add(updatedLengthDto);
            } else {
                lengthTable.refresh();
                String message = "The length can not be renamed in database: [old length description = " + oldLengthDescription + ", new length description = " + newLengthDescription + "]";
                logger.warn(message);
                Utils.warningDialog("Update operation is aborted!", message);
            }
        } catch (Exception e) {
            lengthTable.refresh();
            String message = "The length can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void addLengthOnAction() {
        try {
            Optional<SelectDto> result = Utils.TwoTextInputsDialog();
            if (result.isPresent() && StringUtils.isNotBlank(result.get().getKey()) && StringUtils.isNotBlank(result.get().getValue())) {
                String code = result.get().getKey();
                String description = result.get().getValue();
                lengthTable.getItems().add(facade.createNewLength(code, description));
            }
        } catch (Exception e) {
            String message = "The length can not be created!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @FXML
    private void removeLengthOnAction() {
        try {
            int selectedIndex = lengthTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                SelectDto selectedLength = lengthTable.getSelectionModel().getSelectedItem();
                if (facade.deleteSelectedLength(selectedLength.getKey())) {
                    lengthTable.getItems().remove(selectedIndex);
                } else {
                    String message = "The length can not be deleted from database";
                    logger.warn(message);
                    Utils.warningDialog(message, "Delete operation is aborted!");
                }
            } else {
                String message = "No selected length to delete";
                logger.warn(message);
                Utils.warningDialog(message, "Delete operation is aborted!");
            }
        } catch (Exception e) {
            String message = "The length can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }
}
