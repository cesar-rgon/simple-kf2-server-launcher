package stories.maxplayersedition;

import daos.MaxPlayersDao;
import dtos.SelectDto;
import dtos.factories.MaxPlayersDtoFactory;
import entities.MaxPlayers;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class MaxPlayersEditionFacadeImpl implements MaxPlayersEditionFacade {

    private final MaxPlayersDtoFactory maxPlayersDtoFactory;

    public MaxPlayersEditionFacadeImpl() {
        super();
        this.maxPlayersDtoFactory = new MaxPlayersDtoFactory();
    }

    public ObservableList<SelectDto> listAllMaxPlayers() throws SQLException {
        List<MaxPlayers> maxPlayersList = MaxPlayersDao.getInstance().listAll();
        return maxPlayersDtoFactory.newDtos(maxPlayersList);
    }
}
