package stories.lengthedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface LengthEditionFacade {
    ObservableList<SelectDto> listAllItems() throws Exception;
    SelectDto createItem(String code, String description) throws Exception;
    boolean deleteItem(String code) throws Exception;
    SelectDto updateItemCode(String oldCode, String newCode) throws Exception;
    SelectDto updateItemDescription(String code, String oldDescription, String newDescription) throws Exception;
    ProfileDto unselectLengthInProfile(String profileName) throws Exception;
}
