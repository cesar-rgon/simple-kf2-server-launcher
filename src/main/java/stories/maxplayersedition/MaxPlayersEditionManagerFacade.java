package stories.maxplayersedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;
import stories.listallitems.ListAllItemsFacadeResult;

public interface MaxPlayersEditionManagerFacade {

    ListAllItemsFacadeResult<SelectDto> execute() throws Exception;
    SelectDto createItem(String code, String description, String languageCode) throws Exception;
    void deleteItem(String code) throws Exception;
    SelectDto updateItemCode(String oldCode, String newCode) throws Exception;
    SelectDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;

    // ----


    ProfileDto unselectMaxPlayersInProfile(String profileName) throws Exception;
    ProfileDto findProfileDtoByName(String name) throws Exception;

}
