package stories.difficultiesedition;

import dtos.SelectDto;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import utils.Utils;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DifficultiesEditionController implements Initializable {

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
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void difficultyCodeColumnOnEditCommit() {
    }

    @FXML
    private void difficultyDescriptionColumnOnEditCommit() {
    }

    @FXML
    private void addDifficultyOnAction() {
    }

    @FXML
    private void removeDifficultyOnAction() {
    }
}
