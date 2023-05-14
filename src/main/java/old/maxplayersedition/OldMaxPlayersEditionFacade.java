package old.maxplayersedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;

public interface OldMaxPlayersEditionFacade {
    ObservableList<SelectDto> listAllItems() throws Exception;
    SelectDto createItem(String code, String description, String languageCode) throws Exception;
    boolean deleteItem(String code) throws Exception;
    SelectDto updateItemCode(String oldCode, String newCode) throws Exception;
    SelectDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception;
    ProfileDto unselectMaxPlayersInProfile(String profileName) throws Exception;
    ProfileDto findProfileDtoByName(String name) throws Exception;
}
