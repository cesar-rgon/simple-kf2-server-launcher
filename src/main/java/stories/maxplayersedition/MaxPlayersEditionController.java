package stories.maxplayersedition;

import dtos.SelectDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class MaxPlayersEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(MaxPlayersEditionController.class);
    private final MaxPlayersEditionFacade facade;
    private final PropertyService propertyService;
    protected String languageCode;

    @FXML private TableView<SelectDto> maxPlayersTable;
    @FXML private TableColumn<SelectDto, String> maxPlayersCodeColumn;
    @FXML private TableColumn<SelectDto, String> maxPlayersDescriptionColumn;
    @FXML private Label titleConfigLabel;
    @FXML private Label messageLabel;
    @FXML private Button addMaxPlayers;
    @FXML private Button removeMaxPlayers;

    public MaxPlayersEditionController() {
        super();
        facade = new MaxPlayersEditionFacadeImpl();
        propertyService = new PropertyServiceImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            maxPlayersCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            maxPlayersCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            maxPlayersDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            maxPlayersDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            maxPlayersTable.setItems(facade.listAllMaxPlayers());

            String titleConfigLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.maxPlayersTitle");
            titleConfigLabel.setText(titleConfigLabelText);
            String messageLabelText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.itemMessage");
            messageLabel.setText(messageLabelText);
            String addMaxPlayersText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addItem");
            addMaxPlayers.setText(addMaxPlayersText);
            String removeMaxPlayersText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeItem");
            removeMaxPlayers.setText(removeMaxPlayersText);
            String maxPlayersCodeColumnText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.maxPlayersCode");
            maxPlayersCodeColumn.setText(maxPlayersCodeColumnText);
            String maxPlayersDescriptionColumnText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.maxPlayersDescription");
            maxPlayersDescriptionColumn.setText(maxPlayersDescriptionColumnText);
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
            SelectDto updatedMaxPlayersDto = facade.updateChangedMaxPlayersCode(oldMaxPlayersCode, newMaxPlayersCode);
            if (updatedMaxPlayersDto != null) {
                maxPlayersTable.getItems().remove(edittedRowIndex);
                maxPlayersTable.getItems().add(updatedMaxPlayersDto);
            } else {
                maxPlayersTable.refresh();
                logger.warn("The max. players can not be renamed in database: [old max. players code = " + oldMaxPlayersCode + ", new max. players code = " + newMaxPlayersCode + "]");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.maxPlayersNotRenamed");
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
            SelectDto updatedLengthDto = facade.updateChangedMaxPlayersDescription(code, oldMaxPlayersDescription, newMaxPlayersDescription);
            if (updatedLengthDto != null) {
                maxPlayersTable.getItems().remove(edittedRowIndex);
                maxPlayersTable.getItems().add(updatedLengthDto);
            } else {
                maxPlayersTable.refresh();
                logger.warn("The max. players can not be renamed in database: [old max. players description = " + oldMaxPlayersDescription + ", new max. players description = " + newMaxPlayersDescription + "]");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.maxPlayersNotRenamed");
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
                maxPlayersTable.getItems().add(facade.createNewMaxPlayers(code, description));
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
                if (facade.deleteSelectedMaxPlayers(selectedMaxPlayers.getKey())) {
                    maxPlayersTable.getItems().remove(selectedIndex);
                } else {
                    logger.warn("The max. players can not be deleted from database: " + selectedMaxPlayers.getKey());
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.maxPlayersNotDeleted");
                    Utils.warningDialog(headerText, contentText);
                }
            } else {
                logger.warn("No selected max. players to delete");
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.maxPlayersNotSelected");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            String message = "The max. players can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }
}
