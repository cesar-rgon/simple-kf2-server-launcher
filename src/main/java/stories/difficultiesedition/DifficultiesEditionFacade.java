package stories.difficultiesedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface DifficultiesEditionFacade {
    ObservableList<SelectDto> listAllDifficulties() throws SQLException;
    SelectDto createNewDifficulty(String code, String description, SelectDto selectedLanguage) throws SQLException;
    boolean deleteSelectedDifficulty(String code) throws SQLException;
    SelectDto updateChangedDifficultyCode(String oldCode, String newCode) throws SQLException;
    SelectDto updateChangedDifficultyDescription(String code, String oldDescription, String newDescription, SelectDto selectedLanguage) throws SQLException;
}
