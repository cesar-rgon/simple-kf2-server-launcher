package stories.gametypesedition;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import javafx.collections.ObservableList;
import stories.listallitems.ListAllItemsFacadeResult;

public interface GameTypesEditionManagerFacade {

    ListAllItemsFacadeResult<GameTypeDto> execute() throws Exception;
    GameTypeDto createItem(String code, String description, String languageCode) throws Exception;
    void deleteItem(String code) throws Exception;
    GameTypeDto updateItemCode(String oldCode, String newCode) throws Exception;
    GameTypeDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception;

    // ----


    GameTypeDto updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws Exception;
    GameTypeDto updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws Exception;
    ProfileDto unselectGametypeInProfile(String profileName) throws Exception;
    ProfileDto findProfileDtoByName(String profileName) throws Exception;

}
