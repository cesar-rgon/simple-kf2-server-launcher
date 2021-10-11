package stories.gametypesedition;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;
import pojos.session.Session;

import java.sql.SQLException;

public interface GameTypesEditionFacade {
    ObservableList<GameTypeDto> listAllItems() throws SQLException;
    GameTypeDto createItem(String code, String description) throws Exception;
    boolean deleteItem(String code) throws Exception;
    GameTypeDto updateItemCode(String oldCode, String newCode) throws Exception;
    GameTypeDto updateItemDescription(String code, String oldDescription, String newDescription) throws Exception;
    GameTypeDto updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws SQLException;
    GameTypeDto updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws SQLException;
    ProfileDto unselectGametypeInProfile(String profileName) throws SQLException;
}
