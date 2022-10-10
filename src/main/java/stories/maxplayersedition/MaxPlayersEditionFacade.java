package stories.maxplayersedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import entities.Description;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface MaxPlayersEditionFacade {
    ObservableList<SelectDto> listAllItems() throws Exception;
    SelectDto createItem(String code, String description, String languageCode) throws Exception;
    boolean deleteItem(String code) throws Exception;
    SelectDto updateItemCode(String oldCode, String newCode) throws Exception;
    SelectDto updateItemDescription(String code, String oldDescription, String newDescription, String languageCode) throws Exception;
    ProfileDto unselectMaxPlayersInProfile(String profileName) throws Exception;
    ProfileDto findProfileDtoByName(String name) throws Exception;
}
