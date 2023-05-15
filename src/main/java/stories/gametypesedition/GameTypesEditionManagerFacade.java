package stories.gametypesedition;

import dtos.GameTypeDto;
import dtos.ProfileDto;
import javafx.collections.ObservableList;
import stories.listallitems.ListAllItemsFacadeResult;

public interface GameTypesEditionManagerFacade {

    ListAllItemsFacadeResult<GameTypeDto> execute() throws Exception;
    GameTypeDto createItem(String code, String description, String languageCode) throws Exception;
    void deleteItem(String actualProfileName, String code) throws Exception;
    GameTypeDto updateItemCode(String oldCode, String newCode) throws Exception;
    GameTypeDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;

    // ----


    GameTypeDto updateChangedDifficultiesEnabled(String code, Boolean newDifficultiesEnabled) throws Exception;
    GameTypeDto updateChangedLengthsEnabled(String code, Boolean newLengthsEnabled) throws Exception;
}
