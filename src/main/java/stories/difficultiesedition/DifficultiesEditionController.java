package stories.difficultiesedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.listallitems.ListAllItemsFacadeResult;
import utils.Utils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DifficultiesEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(DifficultiesEditionController.class);
    private final DifficultiesEditionManagerFacade facade;
    private final PropertyService propertyService;
    protected String languageCode;

    @FXML private TableView<SelectDto> difficultiesTable;
    @FXML private TableColumn<SelectDto, String> difficultyCodeColumn;
    @FXML private TableColumn<SelectDto, String> difficultyDescriptionColumn;
    @FXML private Label titleConfigLabel;
    @FXML private Label messageLabel;
    @FXML private Button addDifficulty;
    @FXML private Button removeDifficulty;
    @FXML private Label difficultyCodeLabel;
    @FXML private Label difficultyDescriptionLabel;

    public DifficultiesEditionController() {
        facade = new DifficultiesEditionManagerFacadeImpl();
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

            ListAllItemsFacadeResult<SelectDto> result = facade.execute();
            difficultiesTable.setItems(result.getAllItemList());

            String titleConfigLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.difficultyTitle");
            titleConfigLabel.setText(titleConfigLabelText);

            String messageLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.itemMessage");
            messageLabel.setText(messageLabelText);

            Double tooltipDuration = Double.parseDouble(
                    propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );

            String addDifficultyText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addItem");
            addDifficulty.setText(addDifficultyText);
            Tooltip addDifficultyTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.addDifficulty"));
            addDifficultyTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            addDifficulty.setTooltip(addDifficultyTooltip);

            String removeDifficultyText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeItem");
            removeDifficulty.setText(removeDifficultyText);
            Tooltip removeDifficultyTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.removeDifficulty"));
            removeDifficultyTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            removeDifficulty.setTooltip(removeDifficultyTooltip);

            String difficultyCodeColumnText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.difficultyCode");
            difficultyCodeLabel.setText(difficultyCodeColumnText);
            Tooltip difficultyCodeLabelTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.difficultyCode"));
            difficultyCodeLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            difficultyCodeLabel.setTooltip(difficultyCodeLabelTooltip);

            String difficultyDescriptionColumnText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.difficultyDescription");
            difficultyDescriptionLabel.setText(difficultyDescriptionColumnText);
            Tooltip difficultyDescriptionLabelTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.difficultyDescription"));
            difficultyDescriptionLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            difficultyDescriptionLabel.setTooltip(difficultyDescriptionLabelTooltip);
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
            SelectDto updatedDifficultyDto = facade.updateItemCode(oldDifficultyCode, newDifficultyCode);
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
            SelectDto updatedGameTypeDto = facade.updateItemDescription(code, oldDifficultyDescription, newDifficultyDescription, languageCode);
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
                difficultiesTable.getItems().add(facade.createItem(code, description, languageCode));
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

                String question = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.deleteDifficultyQuestion");
                Optional<ButtonType> result = Utils.questionDialog(question, selectedDifficulty.getKey());
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                    ProfileDto actualProfile = facade.findProfileDtoByName(Session.getInstance().getActualProfileName());
                    if (actualProfile != null && actualProfile.getDifficulty() != null &&
                            selectedDifficulty.getKey().equals(actualProfile.getDifficulty().getKey())) {
                        facade.unselectDifficultyInProfile(actualProfile.getName());
                    }

                    facade.deleteItem(selectedDifficulty.getKey());
                    difficultiesTable.getItems().remove(selectedIndex);
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
