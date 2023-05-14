package old.gametypesedition;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import javafx.collections.ObservableList;

public interface OldGameTypesEditionFacade {
    ObservableList<GameTypeDto> listAllItems() throws Exception;
    GameTypeDto createItem(String code, String description, String languageCode) throws Exception;
    boolean deleteItem(String code) throws Exception;
    GameTypeDto updateItemCode(String oldCode, String newCode) throws Exception;
    GameTypeDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception;
    GameTypeDto updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws Exception;
    GameTypeDto updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws Exception;
    ProfileDto unselectGametypeInProfile(String profileName) throws Exception;
    ProfileDto findProfileDtoByName(String profileName) throws Exception;
}
