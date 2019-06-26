package stories.maxplayersedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface MaxPlayersEditionFacade {
    ObservableList<SelectDto> listAllMaxPlayers() throws SQLException;
}
