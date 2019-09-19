package stories.difficultiesedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface DifficultiesEditionFacade {
    ObservableList<SelectDto> listAllDifficulties() throws SQLException;
    SelectDto createNewDifficulty(String code, String description) throws Exception;
    boolean deleteSelectedDifficulty(String code) throws Exception;
    SelectDto updateChangedDifficultyCode(String oldCode, String newCode) throws Exception;
    SelectDto updateChangedDifficultyDescription(String code, String oldDescription, String newDescription) throws Exception;
    ProfileDto unselectDifficultyInProfile(String profileName) throws SQLException;
}
