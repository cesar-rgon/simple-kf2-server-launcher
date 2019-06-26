package stories.difficultiesedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface DifficultiesEditionFacade {
    ObservableList<SelectDto> listAllDifficulties() throws SQLException;
}
