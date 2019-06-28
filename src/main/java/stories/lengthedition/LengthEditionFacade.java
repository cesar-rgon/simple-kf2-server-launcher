package stories.lengthedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface LengthEditionFacade {
    ObservableList<SelectDto> listAllLength() throws SQLException;
    SelectDto createNewLength(String code, String description, SelectDto selectedLanguage) throws SQLException;
    boolean deleteSelectedLength(String code) throws SQLException;
    SelectDto updateChangedLengthCode(String oldCode, String newCode) throws SQLException;
    SelectDto updateChangedLengthDescription(String code, String oldDescription, String newDescription, SelectDto selectedLanguage) throws SQLException;
}
