package stories.lengthedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface LengthEditionFacade {
    ObservableList<SelectDto> listAllLength() throws SQLException;
}
