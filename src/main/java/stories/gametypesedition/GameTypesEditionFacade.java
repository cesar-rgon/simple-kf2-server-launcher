package stories.gametypesedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface GameTypesEditionFacade {
    ObservableList<SelectDto> listAllGameTypes() throws SQLException;
    SelectDto createNewGameType(String code, String description, SelectDto selectedLanguage) throws SQLException;
    boolean deleteSelectedGameType(String code) throws SQLException;
    SelectDto updateChangedGameTypeCode(String oldCode, String newCode) throws SQLException;
    SelectDto updateChangedGameTypeDescription(String code, String oldDescription, String newDescription, SelectDto selectedLanguage) throws SQLException;
}
