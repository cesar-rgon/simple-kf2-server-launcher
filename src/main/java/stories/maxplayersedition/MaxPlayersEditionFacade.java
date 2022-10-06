package stories.maxplayersedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface MaxPlayersEditionFacade {
    ObservableList<SelectDto> listAllItems() throws Exception;
    SelectDto createItem(String code, String description) throws Exception;
    boolean deleteItem(String code) throws Exception;
    SelectDto updateItemCode(String oldCode, String newCode) throws Exception;
    SelectDto updateItemDescription(String code, String oldDescription, String newDescription) throws Exception;
    ProfileDto unselectMaxPlayersInProfile(String profileName) throws Exception;
}
