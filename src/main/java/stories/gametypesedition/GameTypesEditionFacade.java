package stories.gametypesedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface GameTypesEditionFacade {
    ObservableList<SelectDto> listAllGameTypes() throws SQLException;
    SelectDto createNewGameType(String code, String description) throws Exception;
    boolean deleteSelectedGameType(String code) throws Exception;
    SelectDto updateChangedGameTypeCode(String oldCode, String newCode) throws Exception;
    SelectDto updateChangedGameTypeDescription(String code, String oldDescription, String newDescription) throws Exception;
}
