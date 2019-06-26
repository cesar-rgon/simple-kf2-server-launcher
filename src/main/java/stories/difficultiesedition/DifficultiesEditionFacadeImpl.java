package stories.difficultiesedition;

import daos.DifficultyDao;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import entities.Difficulty;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class DifficultiesEditionFacadeImpl implements DifficultiesEditionFacade {

    private final DifficultyDtoFactory difficultyDtoFactory;

    public DifficultiesEditionFacadeImpl() {
        super();
        this.difficultyDtoFactory = new DifficultyDtoFactory();
    }

    @Override
    public ObservableList<SelectDto> listAllDifficulties() throws SQLException {
        List<Difficulty> difficulties = DifficultyDao.getInstance().listAll();
        return difficultyDtoFactory.newDtos(difficulties);
    }
}
