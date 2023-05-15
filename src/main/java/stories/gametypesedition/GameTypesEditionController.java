package stories.gametypesedition;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Duration;
import old.gametypesedition.OldGameTypesEditionFacade;
import old.gametypesedition.OldGameTypesEditionFacadeImpl;
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

public class GameTypesEditionController implements Initializable {

    private static final Logger logger = LogManager.getLogger(GameTypesEditionController.class);
    private final GameTypesEditionManagerFacade facade;
    protected String languageCode;

    @FXML private TableView<GameTypeDto> gameTypesTable;
    @FXML private TableColumn<GameTypeDto, String> gameTypeCodeColumn;
    @FXML private TableColumn<GameTypeDto, String> gameTypeDescriptionColumn;
    @FXML private TableColumn<GameTypeDto, Boolean> difficultiesEnabledColumn;
    @FXML private TableColumn<GameTypeDto, Boolean> lengthsEnabledColumn;
    @FXML private Label titleConfigLabel;
    @FXML private Label messageLabel;
    @FXML private Button addGameType;
    @FXML private Button removeGameType;
    @FXML private Label gameTypeCodeLabel;
    @FXML private Label gameTypeDescriptionLabel;
    @FXML private Label difficultiesEnabledLabel;
    @FXML private Label lengthsEnabledLabel;

    public GameTypesEditionController() {
        facade = new GameTypesEditionManagerFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            languageCode = facade.findPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            gameTypeCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            gameTypeCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            gameTypeDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            gameTypeDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());

            String titleConfigLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.gameTypeTitle");
            titleConfigLabel.setText(titleConfigLabelText);

            String messageLabelText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.itemMessage");
            messageLabel.setText(messageLabelText);

            Double tooltipDuration = Double.parseDouble(
                    facade.findPropertyValue("properties/config.properties", "prop.config.tooltipDuration")
            );

            String addGameTypeText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.addItem");
            addGameType.setText(addGameTypeText);
            Tooltip addGameTypeTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.addGameType"));
            addGameTypeTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            addGameType.setTooltip(addGameTypeTooltip);

            String removeGameTypeText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.removeItem");
            removeGameType.setText(removeGameTypeText);
            Tooltip removeGameTypeTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.removeGameType"));
            removeGameTypeTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            removeGameType.setTooltip(removeGameTypeTooltip);

            String gameTypeCodeColumnText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.gameTypeCode");
            gameTypeCodeLabel.setText(gameTypeCodeColumnText);
            Tooltip gameTypeCodeLabelTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.gameTypeCode"));
            gameTypeCodeLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            gameTypeCodeLabel.setTooltip(gameTypeCodeLabelTooltip);

            String gameTypeDescriptionColumnText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.gameTypeDescription");
            gameTypeDescriptionLabel.setText(gameTypeDescriptionColumnText);
            Tooltip gameTypeDescriptionLabelTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.gameTypeDescription"));
            gameTypeDescriptionLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            gameTypeDescriptionLabel.setTooltip(gameTypeDescriptionLabelTooltip);

            String difficultiesEnabledColumnText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.difficultiesEnabled");
            difficultiesEnabledLabel.setText(difficultiesEnabledColumnText);
            Tooltip difficultiesEnabledLabelTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.difficultiesEnabled"));
            difficultiesEnabledLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            difficultiesEnabledLabel.setTooltip(difficultiesEnabledLabelTooltip);

            String lengthsEnabledColumnText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.lengthsEnabled");
            lengthsEnabledLabel.setText(lengthsEnabledColumnText);
            Tooltip lengthsEnabledLabelTooltip = new Tooltip(facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.tooltip.lengthsEnabled"));
            lengthsEnabledLabelTooltip.setShowDuration(Duration.seconds(tooltipDuration));
            lengthsEnabledLabel.setTooltip(lengthsEnabledLabelTooltip);

            difficultiesEnabledColumn.setCellFactory(col -> {
                CheckBoxTableCell<GameTypeDto, Boolean> cell = new CheckBoxTableCell<>(index -> {
                    BooleanProperty active = new SimpleBooleanProperty(gameTypesTable.getItems().get(index).isDifficultyEnabled());
                    active.addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            try {
                                String code = gameTypesTable.getItems().get(index).getKey();
                                GameTypeDto updatedGameTypeDto = facade.updateChangedDifficultiesEnabled(code, newValue);
                                if (updatedGameTypeDto == null) {
                                    gameTypesTable.refresh();
                                    logger.warn("The game type can not be changed in database: " + code + ". Difficulties enabled value is restored.");
                                    String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                                    String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.difficultiesEnabledNotUpdated");
                                    Utils.warningDialog(headerText, contentText);
                                }
                            } catch (Exception e) {
                                gameTypesTable.refresh();
                                String message = "The game type can not be updated!";
                                logger.error(message, e);
                                Utils.errorDialog(message, e);
                            }
                        }
                    });
                    return active;
                });
                return cell ;
            });
            difficultiesEnabledColumn.setCellValueFactory(cellData -> cellData.getValue().isDifficultyEnabledProperty());

            lengthsEnabledColumn.setCellFactory(col -> {
                CheckBoxTableCell<GameTypeDto, Boolean> cell = new CheckBoxTableCell<>(index -> {
                    BooleanProperty active = new SimpleBooleanProperty(gameTypesTable.getItems().get(index).isLengthEnabled());
                    active.addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            try {
                                String code = gameTypesTable.getItems().get(index).getKey();
                                GameTypeDto updatedGameTypeDto = facade.updateChangedLengthsEnabled(code, newValue);
                                if (updatedGameTypeDto == null) {
                                    gameTypesTable.refresh();
                                    logger.warn("The game type can not be changed in database: " + code + ". Lengths enabled value is restored");
                                    String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                                    String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.lengthsEnabledNotUpdated");
                                    Utils.warningDialog(headerText, contentText);
                                }
                            } catch (Exception e) {
                                gameTypesTable.refresh();
                                String message = "The game type can not be updated!";
                                logger.error(message, e);
                                Utils.errorDialog(message, e);
                            }
                        }
                    });
                    return active;
                });
                return cell ;
            });
            lengthsEnabledColumn.setCellValueFactory(cellData -> cellData.getValue().isLengthEnabledProperty());

            ListAllItemsFacadeResult<GameTypeDto> result = facade.execute();
            gameTypesTable.setItems(result.getAllItemList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    @FXML
    private void gameTypeCodeColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldGameTypeCode = (String)event.getOldValue();
        String newGameTypeCode = (String)event.getNewValue();
        try {
            GameTypeDto updatedGameTypeDto = facade.updateItemCode(oldGameTypeCode, newGameTypeCode);
            if (updatedGameTypeDto != null) {
                gameTypesTable.getItems().remove(edittedRowIndex);
                gameTypesTable.getItems().add(updatedGameTypeDto);
            } else {
                gameTypesTable.refresh();
                logger.warn("The game type can not be renamed in database: [old game type code = " + oldGameTypeCode + ", new game type code = " + newGameTypeCode + "]");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.gameTypeNotRenamed");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            gameTypesTable.refresh();
            String message = "The game type can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void gameTypeDescriptionColumnOnEditCommit(TableColumn.CellEditEvent<?,?> event) {
        int edittedRowIndex = event.getTablePosition().getRow();
        String oldGameTypeDescription = (String)event.getOldValue();
        String newGameTypeDescription = (String)event.getNewValue();
        try {
            String code = gameTypesTable.getItems().get(edittedRowIndex).getKey();
            GameTypeDto updatedGameTypeDto = facade.updateItemDescription(code, oldGameTypeDescription, newGameTypeDescription, languageCode);
            if (updatedGameTypeDto != null) {
                gameTypesTable.getItems().remove(edittedRowIndex);
                gameTypesTable.getItems().add(updatedGameTypeDto);
            } else {
                gameTypesTable.refresh();
                logger.warn("The game type can not be renamed in database: [old game type description = " + oldGameTypeDescription + ", new game type description = " + newGameTypeDescription + "]");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.gameTypeNotRenamed");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            gameTypesTable.refresh();
            String message = "The game type can not be updated!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void addGameTypeOnAction() {
        try {
            Optional<SelectDto> result = Utils.TwoTextInputsDialog();
            if (result.isPresent() && StringUtils.isNotBlank(result.get().getKey()) && StringUtils.isNotBlank(result.get().getValue())) {
                String code = result.get().getKey();
                String descriptionValue = result.get().getValue();
                gameTypesTable.getItems().add(facade.createItem(code, descriptionValue, languageCode));
            }
        } catch (Exception e) {
            String message = "The game type can not be created!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @FXML
    private void removeGameTypeOnAction() {
        try {
            int selectedIndex = gameTypesTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                SelectDto selectedGameType = gameTypesTable.getSelectionModel().getSelectedItem();

                String question = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.deleteGameTypeQuestion");
                Optional<ButtonType> result = Utils.questionDialog(question, selectedGameType.getKey());
                if (result.isPresent() && result.get().equals(ButtonType.OK)) {

                    ProfileDto actualProfile = facade.findProfileDtoByName(Session.getInstance().getActualProfileName());
                    if (actualProfile != null &&
                            actualProfile.getGametype() != null &&
                            selectedGameType.getKey().equals(actualProfile.getGametype().getKey())) {
                        facade.unselectGametypeInProfile(actualProfile.getName());
                    }

                    facade.deleteItem(selectedGameType.getKey());
                    gameTypesTable.getItems().remove(selectedIndex);
                }
            } else {
                logger.warn("No selected game type to delete");
                String headerText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = facade.findPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.gameTypeNotSelected");
                Utils.warningDialog(headerText, contentText);
            }
        } catch (Exception e) {
            String message = "The game type can not be deleted!";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }
}
