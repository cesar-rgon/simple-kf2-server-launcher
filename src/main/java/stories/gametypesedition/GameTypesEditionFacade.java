package stories.gametypesedition;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;
import pojos.session.Session;

import java.sql.SQLException;

public interface GameTypesEditionFacade {
    ObservableList<GameTypeDto> listAllItems() throws Exception;
    GameTypeDto createItem(String code, String description) throws Exception;
    boolean deleteItem(String code) throws Exception;
    GameTypeDto updateItemCode(String oldCode, String newCode) throws Exception;
    GameTypeDto updateItemDescription(String code, String oldDescription, String newDescription) throws Exception;
    GameTypeDto updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws Exception;
    GameTypeDto updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws Exception;
    ProfileDto unselectGametypeInProfile(String profileName) throws Exception;
}
