package stories.maxplayersedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface MaxPlayersEditionFacade {
    ObservableList<SelectDto> listAllMaxPlayers() throws SQLException;
    SelectDto createNewMaxPlayers(String code, String description) throws Exception;
    boolean deleteSelectedMaxPlayers(String code) throws Exception;
    SelectDto updateChangedMaxPlayersCode(String oldCode, String newCode) throws Exception;
    SelectDto updateChangedMaxPlayersDescription(String code, String oldDescription, String newDescription) throws Exception;
}
