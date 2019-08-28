package stories.gametypesedition;

import dtos.GameTypeDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface GameTypesEditionFacade {
    ObservableList<GameTypeDto> listAllGameTypes() throws SQLException;
    GameTypeDto createNewGameType(String code, String description) throws Exception;
    boolean deleteSelectedGameType(String code) throws Exception;
    GameTypeDto updateChangedGameTypeCode(String oldCode, String newCode) throws Exception;
    GameTypeDto updateChangedGameTypeDescription(String code, String oldDescription, String newDescription) throws Exception;
    GameTypeDto updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws SQLException;
    GameTypeDto updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws SQLException;
}
