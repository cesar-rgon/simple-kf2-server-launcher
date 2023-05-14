package stories.lengthedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import stories.listallitems.ListAllItemsFacadeResult;

public interface LengthEditionManagerFacade {

    ListAllItemsFacadeResult<SelectDto> execute() throws Exception;
    SelectDto createItem(String code, String description, String languageCode) throws Exception;
    void deleteItem(String code) throws Exception;
    SelectDto updateItemCode(String oldCode, String newCode) throws Exception;
    SelectDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception;

    // ----


    ProfileDto unselectLengthInProfile(String profileName) throws Exception;
    ProfileDto findProfileDtoByName(String profileName) throws Exception;

}
