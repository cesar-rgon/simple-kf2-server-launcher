package stories.gametypesedition;

import daos.GameTypeDao;
import dtos.SelectDto;
import dtos.factories.GameTypeDtoFactory;
import entities.GameType;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class GameTypesEditionFacadeImpl implements GameTypesEditionFacade {

    private final GameTypeDtoFactory gateTypeDtoFactory;

    public GameTypesEditionFacadeImpl() {
        super();
        gateTypeDtoFactory = new GameTypeDtoFactory();
    }

    @Override
    public ObservableList<SelectDto> listAllGameTypes() throws SQLException {
        List<GameType> gameTypes = GameTypeDao.getInstance().listAll();
        return gateTypeDtoFactory.newDtos(gameTypes);
    }
}
