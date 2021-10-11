package stories.difficultiesedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface DifficultiesEditionFacade {
    ObservableList<SelectDto> listAllItems() throws SQLException;
    SelectDto createItem(String code, String description) throws Exception;
    boolean deleteItem(String code) throws Exception;
    SelectDto updateItemCode(String oldCode, String newCode) throws Exception;
    SelectDto updateItemDescription(String code, String oldDescription, String newDescription) throws Exception;
    ProfileDto unselectDifficultyInProfile(String profileName) throws SQLException;
}
