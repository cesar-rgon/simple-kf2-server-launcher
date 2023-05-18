package stories.lengthedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import stories.listallitems.ListAllItemsFacadeResult;
import utils.Utils;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LengthEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(LengthEditionController.class);
    private final LengthEditionManagerFacade facade;
    protected String languageCode;

    @FXML private TableView<SelectDto> lengthTable;
    @FXML private TableColumn<SelectDto, String> lengthCodeColumn;
    @FXML private TableColumn<SelectDto, String> lengthDescriptionColumn;
    @FXML private Label titleConfigLabel;
    @FXML private Label messageLabel;
    @FXML private Button addLength;
    @FXML private Button removeLength;
    @FXML private Label lengthCodeLabel;
    @FXML private Label lengthDescriptionLabel;
    @FXML private ProgressIndicator progressIndicator;

    public LengthEditionController(){
        super();
        facade = new LengthEditionManagerFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = facade.findPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            lengthCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            lengthCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            lengthDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            lengthDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            ListAllItemsFacadeResult<SelectDto> result = facade.execute();
            lengthTable.setItems(result.getAllItemList());

            String titleConfigLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthTitle");
            titleConfigLabel.setText(titleConfigLabelText);

            String messageLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.itemMessage");
            messageLabel.setText(messageLabelText);

            Double tooltipDuration = Double.parseDouble(
                    facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );

            String addLengthText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addItem");
            addLength.setText(addLengthText);
            Tooltip addLengthTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.addLength"));
            addLengthTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            addLength.setTooltip(addLengthTooltip);

            String removeLengthText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeItem");
            removeLength.setText(removeLengthText);
            Tooltip removeLengthTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.removeLength"));
            removeLengthTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            removeLength.setTooltip(removeLengthTooltip);

            String lengthCodeColumnText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthCode");
            lengthCodeLabel.setText(lengthCodeColumnText);
            Tooltip lengthCodeLabelTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.lengthCode"));
            lengthCodeLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            lengthCodeLabel.setTooltip(lengthCodeLabelTooltip);

            String lengthDescriptionColumnText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthDescription");
            lengthDescriptionLabel.setText(lengthDescriptionColumnText);
            Tooltip lengthDescriptionLabelTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.lengthDescription"));
            lengthDescriptionLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            lengthDescriptionLabel.setTooltip(lengthDescriptionLabelTooltip);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void lengthCodeColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldLengthCode = (String)event.getOldValue();
        String newLengthCode = (String)event.getNewValue();
        try {
            SelectDto updatedLengthDto = facade.updateItemCode(oldLengthCode, newLengthCode);
            if (updatedLengthDto != null) {
                lengthTable.getItems().remove(edittedRowIndex);
                lengthTable.getItems().add(updatedLengthDto);
            } else {
                lengthTable.refresh();
                logger.warn("The length can not be renamed in database: [old length code = " + oldLengthCode + ", new length code = " + newLengthCode + "]");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.lengthNotRenamed");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            lengthTable.refresh();
            String message = "The length can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void lengthDescriptionColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldLengthDescription = (String)event.getOldValue();
        String newLengthDescription = (String)event.getNewValue();
        try {
            String code = lengthTable.getItems().get(edittedRowIndex).getKey();
            SelectDto updatedLengthDto = facade.updateItemDescription(code, oldLengthDescription, newLengthDescription, languageCode);
            if (updatedLengthDto != null) {
                lengthTable.getItems().remove(edittedRowIndex);
                lengthTable.getItems().add(updatedLengthDto);
            } else {
                lengthTable.refresh();
                logger.warn("The length can not be renamed in database: [old length description = " + oldLengthDescription + ", new length description = " + newLengthDescription + "]");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.lengthNotRenamed");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            lengthTable.refresh();
            String message = "The length can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void addLengthOnAction() {
        try {
            Optional<SelectDto> result = Utils.TwoTextInputsDialog();
            if (result.isPresent() && StringUtils.isNotBlank(result.get().getKey()) && StringUtils.isNotBlank(result.get().getValue())) {
                String code = result.get().getKey();
                String description = result.get().getValue();
                lengthTable.getItems().add(facade.createItem(code, description, languageCode));
            }
        } catch (Exception e) {
            String message = "The length can not be created!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void removeLengthOnAction() {
        try {
            int selectedIndex = lengthTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                SelectDto selectedLength = lengthTable.getSelectionModel().getSelectedItem();

                String question = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.deleteLengthQuestion");
                Optional<ButtonType> result = Utils.questionDialog(question, selectedLength.getKey());
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    facade.deleteItem(Session.getInstance().getActualProfileName(), selectedLength.getKey());
                    lengthTable.getItems().remove(selectedIndex);
                }
            } else {
                logger.warn("No selected length to delete");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.lengthNotSelected");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            String message = "The length can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void loadDefaultsOnAction() {
        try {
            String question = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.loadDefaulValues");
            String content = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.loadDefaulValuesExplanation");
            Optional<ButtonType> result = Utils.questionDialog(question, content);

            if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                progressIndicator.setVisible(true);

                Task<List<SelectDto>> task = new Task<List<SelectDto>>() {
                    @Override
                    protected List<SelectDto> call() throws Exception {
                        return facade.loadDefaultValues();
                    }
                };

                task.setOnSucceeded(wse -> {
                    List<SelectDto> defaultLengthList = task.getValue();
                    lengthTable.getItems().clear();
                    for (SelectDto Length: defaultLengthList) {
                        lengthTable.getItems().add(Length);
                    }
                    progressIndicator.setVisible(false);
                });

                task.setOnFailed(wse -> {
                    String message = "The default values could not be loaded for the lengths";
                    logger.error(message);
                    progressIndicator.setVisible(false);
                    try {
                        String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                        String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.itemsNotLoaded");
                        Utils.warningDialog(headerText, contentText);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                });

                Thread thread = new Thread(task);
                thread.start();
            }
        } catch (Exception e) {
            logger.error("Error loading default values for all the lengths", e);
        }
    }
}
