package stories.lengthedition;

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
import utils.Utils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LengthEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(LengthEditionController.class);
    private final LengthEditionFacade facade;
    private final PropertyService propertyService;
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

    public LengthEditionController(){
        super();
        facade = new LengthEditionFacadeImpl();
        propertyService = new PropertyServiceImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            lengthCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            lengthCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            lengthDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            lengthDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            lengthTable.setItems(facade.listAllItems());

            String titleConfigLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthTitle");
            titleConfigLabel.setText(titleConfigLabelText);

            String messageLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.itemMessage");
            messageLabel.setText(messageLabelText);

            Double tooltipDuration = Double.parseDouble(
                    propertyService.getPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );

            String addLengthText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addItem");
            addLength.setText(addLengthText);
            Tooltip addLengthTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.addLength"));
            addLengthTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            addLength.setTooltip(addLengthTooltip);

            String removeLengthText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeItem");
            removeLength.setText(removeLengthText);
            Tooltip removeLengthTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.removeLength"));
            removeLengthTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            removeLength.setTooltip(removeLengthTooltip);

            String lengthCodeColumnText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthCode");
            lengthCodeLabel.setText(lengthCodeColumnText);
            Tooltip lengthCodeLabelTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.lengthCode"));
            lengthCodeLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            lengthCodeLabel.setTooltip(lengthCodeLabelTooltip);

            String lengthDescriptionColumnText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthDescription");
            lengthDescriptionLabel.setText(lengthDescriptionColumnText);
            Tooltip lengthDescriptionLabelTooltip = new Tooltip(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.lengthDescription"));
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
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.lengthNotRenamed");
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
            SelectDto updatedLengthDto = facade.updateItemDescription(code, oldLengthDescription, newLengthDescription);
            if (updatedLengthDto != null) {
                lengthTable.getItems().remove(edittedRowIndex);
                lengthTable.getItems().add(updatedLengthDto);
            } else {
                lengthTable.refresh();
                logger.warn("The length can not be renamed in database: [old length description = " + oldLengthDescription + ", new length description = " + newLengthDescription + "]");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.lengthNotRenamed");
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
                lengthTable.getItems().add(facade.createItem(code, description));
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

                String question = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.deleteLengthQuestion");
                Optional<ButtonType> result = Utils.questionDialog(question, selectedLength.getKey());
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                    ProfileDto actualProfile = Session.getInstance().getActualProfile();
                    if (actualProfile != null && actualProfile.getLength() != null &&
                            selectedLength.getKey().equals(actualProfile.getLength().getKey())) {
                        facade.unselectLengthInProfile(actualProfile.getName());
                    }

                    if (facade.deleteItem(selectedLength.getKey())) {
                        lengthTable.getItems().remove(selectedIndex);
                    } else {
                        logger.warn("The length can not be deleted from database: " + selectedLength.getKey() + " - " + selectedLength.getValue());
                        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                        String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.lengthNotDeleted");
                        Utils.warningDialog(headerText, contentText);
                    }
                }
            } else {
                logger.warn("No selected length to delete");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.lengthNotSelected");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            String message = "The length can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }
}
