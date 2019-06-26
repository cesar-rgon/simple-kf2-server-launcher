package stories.lengthedition;

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

public class LengthEditionController implements Initializable {

    private final LengthEditionFacade facade;

    @FXML private TableView<SelectDto> lengthTable;
    @FXML private TableColumn<SelectDto, String> lengthCodeColumn;
    @FXML private TableColumn<SelectDto, String> lengthDescriptionColumn;

    public LengthEditionController(){
        super();
        this.facade = new LengthEditionFacadeImpl();
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            lengthCodeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            lengthCodeColumn.setCellValueFactory(cellData -> cellData.getValue().getKeyProperty());
            lengthDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            lengthDescriptionColumn.setCellValueFactory(cellData -> cellData.getValue().getValueProperty());
            lengthTable.setItems(facade.listAllLength());
        } catch (SQLException e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    @FXML
    private void lengthCodeColumnOnEditCommit() {
    }

    @FXML
    private void lengthDescriptionColumnOnEditCommit() {
    }

    @FXML
    private void addLengthOnAction() {
    }

    @FXML
    private void removeLengthOnAction() {
    }
}
