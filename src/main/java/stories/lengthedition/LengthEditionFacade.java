package stories.lengthedition;

import dtos.SelectDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface LengthEditionFacade {
    ObservableList<SelectDto> listAllLength() throws SQLException;
    SelectDto createNewLength(String code, String description) throws Exception;
    boolean deleteSelectedLength(String code) throws Exception;
    SelectDto updateChangedLengthCode(String oldCode, String newCode) throws Exception;
    SelectDto updateChangedLengthDescription(String code, String oldDescription, String newDescription) throws Exception;
}
