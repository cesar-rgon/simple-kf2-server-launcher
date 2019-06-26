package stories.maxplayersedition;

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

public class MaxPlayersEditionController implements Initializable {

    private final MaxPlayersEditionFacade facade;

    @FXML private TableView<SelectDto> maxPlayersTable;
    @FXML private TableColumn<SelectDto, String> maxPlayersCodeColumn;
    @FXML private TableColumn<SelectDto, String> maxPlayersDescriptionColumn;

    public MaxPlayersEditionController() {
        super();
        this.facade = new MaxPlayersEditionFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            maxPlayersCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            maxPlayersCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            maxPlayersDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            maxPlayersDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            maxPlayersTable.setItems(facade.listAllMaxPlayers());
        } catch (SQLException e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }


    @FXML
    private void maxPlayersCodeColumnOnEditCommit() {
    }

    @FXML
    private void maxPlayersDescriptionColumnOnEditCommit() {
    }

    @FXML
    private void addMaxPlayersOnAction() {
    }

    @FXML
    private void removeMaxPlayersOnAction() {
    }
}
