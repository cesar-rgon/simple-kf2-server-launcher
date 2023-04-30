package stories.console;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsoleController implements Initializable {

    @FXML private TextArea consoleArea;

    public ConsoleController() {
        super();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        consoleArea.setText(StringUtils.isNotBlank(Session.getInstance().getConsole())? Session.getInstance().getConsole(): "");
    }
}
