package stories.lengthedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import stories.listallitems.ListAllItemsFacadeResult;

import java.util.List;

public interface LengthEditionManagerFacade {

    ListAllItemsFacadeResult<SelectDto> execute() throws Exception;
    SelectDto createItem(String code, String description, String languageCode) throws Exception;
    void deleteItem(String actualProfileName, String code) throws Exception;
    SelectDto updateItemCode(String oldCode, String newCode) throws Exception;
    SelectDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;
    List<SelectDto> loadDefaultValues() throws Exception;
}
