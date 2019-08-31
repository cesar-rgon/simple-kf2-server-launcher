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
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DifficultiesEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(DifficultiesEditionController.class);
    private final DifficultiesEditionFacade facade;
    private final PropertyService propertyService;
    protected String languageCode;

    @FXML private TableView<SelectDto> difficultiesTable;
    @FXML private TableColumn<SelectDto, String> difficultyCodeColumn;
    @FXML private TableColumn<SelectDto, String> difficultyDescriptionColumn;

    public DifficultiesEditionController() {
        facade = new DifficultiesEditionFacadeImpl();
        propertyService = new PropertyServiceImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            difficultyCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            difficultyCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            difficultyDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            difficultyDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            difficultiesTable.setItems(facade.listAllDifficulties());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
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
                logger.warn("The difficulty can not be renamed in database: [old difficulty code = " + oldDifficultyCode + ", new difficulty code = " + newDifficultyCode + "]");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.difficultyNotRenamed");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            difficultiesTable.refresh();
            String message = "The difficulty can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
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
                logger.warn("The difficulty can not be renamed in database: [old difficulty description = " + oldDifficultyDescription + ", new difficulty description = " + newDifficultyDescription + "]");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.difficultyNotRenamed");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            difficultiesTable.refresh();
            String message = "The difficulty can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
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
            Utils.errorDialog(message, e);
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
                    logger.warn("The difficulty can not be deleted from database: " + selectedDifficulty.getKey() + " - " + selectedDifficulty.getValue());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.difficultyNotDeleted");
                    Utils.warningDialog(headerText, contentText);
                }
            } else {
                logger.warn("No selected difficulty to delete");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.difficultyNotSelected");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            String message = "The difficulty can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }
}
