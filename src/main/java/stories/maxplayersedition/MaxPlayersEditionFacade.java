package stories.maxplayersedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface MaxPlayersEditionFacade {
    ObservableList<SelectDto> listAllMaxPlayers() throws SQLException;
    SelectDto createNewMaxPlayers(String code, String description, SelectDto selectedLanguage) throws SQLException;
    boolean deleteSelectedMaxPlayers(String code) throws SQLException;
    SelectDto updateChangedMaxPlayersCode(String oldCode, String newCode) throws SQLException;
    SelectDto updateChangedMaxPlayersDescription(String code, String oldDescription, String newDescription, SelectDto selectedLanguage) throws SQLException;
}
