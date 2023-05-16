package stories.maxplayersedition;

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
import stories.listallitems.ListAllItemsFacadeResult;
import utils.Utils;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MaxPlayersEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MaxPlayersEditionController.class);
    private final MaxPlayersEditionManagerFacade facade;
    protected String languageCode;

    @FXML private TableView<SelectDto> maxPlayersTable;
    @FXML private TableColumn<SelectDto, String> maxPlayersCodeColumn;
    @FXML private TableColumn<SelectDto, String> maxPlayersDescriptionColumn;
    @FXML private Label titleConfigLabel;
    @FXML private Label messageLabel;
    @FXML private Button addMaxPlayers;
    @FXML private Button removeMaxPlayers;
    @FXML private Label maxPlayersCodeLabel;
    @FXML private Label maxPlayersDescriptionLabel;

    public MaxPlayersEditionController() {
        super();
        facade = new MaxPlayersEditionManagerFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = facade.findPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            maxPlayersCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            maxPlayersCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            maxPlayersDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            maxPlayersDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            ListAllItemsFacadeResult<SelectDto> result = facade.execute();
            maxPlayersTable.setItems(result.getAllItemList());

            String titleConfigLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.maxPlayersTitle");
            titleConfigLabel.setText(titleConfigLabelText);

            String messageLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.itemMessage");
            messageLabel.setText(messageLabelText);

            Double tooltipDuration = Double.parseDouble(
                    facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );

            String addMaxPlayersText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addItem");
            addMaxPlayers.setText(addMaxPlayersText);
            Tooltip addMaxPlayersTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.addMaxPlayers"));
            addMaxPlayersTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            addMaxPlayers.setTooltip(addMaxPlayersTooltip);

            String removeMaxPlayersText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeItem");
            removeMaxPlayers.setText(removeMaxPlayersText);
            Tooltip removeMaxPlayersTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.removeMaxPlayers"));
            removeMaxPlayersTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            removeMaxPlayers.setTooltip(removeMaxPlayersTooltip);

            String maxPlayersCodeColumnText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.maxPlayersCode");
            maxPlayersCodeLabel.setText(maxPlayersCodeColumnText);
            Tooltip maxPlayersCodeLabelTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.maxPlayersCode"));
            maxPlayersCodeLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            maxPlayersCodeLabel.setTooltip(maxPlayersCodeLabelTooltip);

            String maxPlayersDescriptionColumnText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.maxPlayersDescription");
            maxPlayersDescriptionLabel.setText(maxPlayersDescriptionColumnText);
            Tooltip maxPlayersDescriptionLabelTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.maxPlayersDescription"));
            maxPlayersDescriptionLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            maxPlayersDescriptionLabel.setTooltip(maxPlayersDescriptionLabelTooltip);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }


    @FXML
    private void maxPlayersCodeColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldMaxPlayersCode = (String)event.getOldValue();
        String newMaxPlayersCode = (String)event.getNewValue();
        try {
            SelectDto updatedMaxPlayersDto = facade.updateItemCode(oldMaxPlayersCode, newMaxPlayersCode);
            if (updatedMaxPlayersDto != null) {
                maxPlayersTable.getItems().remove(edittedRowIndex);
                maxPlayersTable.getItems().add(updatedMaxPlayersDto);
            } else {
                maxPlayersTable.refresh();
                logger.warn("The max. players can not be renamed in database: [old max. players code = " + oldMaxPlayersCode + ", new max. players code = " + newMaxPlayersCode + "]");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.maxPlayersNotRenamed");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            maxPlayersTable.refresh();
            String message = "The max. players can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void maxPlayersDescriptionColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldMaxPlayersDescription = (String)event.getOldValue();
        String newMaxPlayersDescription = (String)event.getNewValue();
        try {
            String code = maxPlayersTable.getItems().get(edittedRowIndex).getKey();
            SelectDto updatedLengthDto = facade.updateItemDescription(code, oldMaxPlayersDescription, newMaxPlayersDescription, languageCode);
            if (updatedLengthDto != null) {
                maxPlayersTable.getItems().remove(edittedRowIndex);
                maxPlayersTable.getItems().add(updatedLengthDto);
            } else {
                maxPlayersTable.refresh();
                logger.warn("The max. players can not be renamed in database: [old max. players description = " + oldMaxPlayersDescription + ", new max. players description = " + newMaxPlayersDescription + "]");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.maxPlayersNotRenamed");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            maxPlayersTable.refresh();
            String message = "The max. players can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void addMaxPlayersOnAction() {
        try {
            Optional<SelectDto> result = Utils.TwoTextInputsDialog();
            if (result.isPresent() && StringUtils.isNotBlank(result.get().getKey()) && StringUtils.isNotBlank(result.get().getValue())) {
                String code = result.get().getKey();
                String description = result.get().getValue();
                maxPlayersTable.getItems().add(facade.createItem(code, description, languageCode));
            }
        } catch (Exception e) {
            String message = "The max. players can not be created!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void removeMaxPlayersOnAction() {
        try {
            int selectedIndex = maxPlayersTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                SelectDto selectedMaxPlayers = maxPlayersTable.getSelectionModel().getSelectedItem();

                String question = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.deleteMaxPlayersQuestion");
                Optional<ButtonType> result = Utils.questionDialog(question, selectedMaxPlayers.getKey());
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    facade.deleteItem(Session.getInstance().getActualProfileName(), selectedMaxPlayers.getKey());
                    maxPlayersTable.getItems().remove(selectedIndex);
                }
            } else {
                logger.warn("No selected max. players to delete");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.maxPlayersNotSelected");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            String message = "The max. players can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void loadDefaultsOnAction() {

    }
}
