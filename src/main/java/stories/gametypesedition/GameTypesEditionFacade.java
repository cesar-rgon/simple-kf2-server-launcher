package stories.gametypesedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface GameTypesEditionFacade {
    ObservableList<SelectDto> listAllGameTypes() throws SQLException;
}
