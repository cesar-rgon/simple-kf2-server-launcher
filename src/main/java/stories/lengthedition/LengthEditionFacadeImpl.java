package stories.lengthedition;

import daos.LengthDao;
import dtos.SelectDto;
import dtos.factories.LengthDtoFactory;
import entities.Length;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class LengthEditionFacadeImpl implements LengthEditionFacade {

    private final LengthDtoFactory lengthDtoFactory;

    public LengthEditionFacadeImpl() {
        super();
        this.lengthDtoFactory = new LengthDtoFactory();
    }

    public ObservableList<SelectDto> listAllLength() throws SQLException {
        List<Length> lengthList = LengthDao.getInstance().listAll();
        return lengthDtoFactory.newDtos(lengthList);
    }
}
