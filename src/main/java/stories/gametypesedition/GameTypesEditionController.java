package stories.gametypesedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import entities.GameType;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class GameTypesEditionController implements Initializable {

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
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void gameTypeCodeColumnOnEditCommit() {

    }

    @FXML
    private void gameTypeDescriptionColumnOnEditCommit() {

    }

    @FXML
    private void addGameTypeOnAction() {
        Dialog<SelectDto> dialog = new Dialog<SelectDto>();
        dialog.setTitle("Simple Killing Floor 2 Server Launcher");
        dialog.setHeaderText("Add a new game type");
        dialog.setResizable(true);
        Label code = new Label("Game type code: ");
        Label description = new Label("Game type description: ");
        TextField codeText = new TextField();
        TextField descriptionText = new TextField();
        GridPane grid = new GridPane();
        grid.add(code, 1, 1);
        grid.add(codeText, 2, 1);
        grid.add(description, 1, 2);
        grid.add(descriptionText, 2, 2);
        dialog.getDialogPane().setContent(grid);
        ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        dialog.setResultConverter(new Callback<ButtonType, SelectDto>() {
            @Override
            public SelectDto call(ButtonType b) {
                if (b == buttonTypeOk) {
                    return new SelectDto(codeText.getText(), descriptionText.getText());
                }
                return null;
            }
        });
        Optional<SelectDto> result = dialog.showAndWait();
    }

    @FXML
    private void removeGameTypeOnAction() {

    }
}
