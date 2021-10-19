package daos;

import entities.GameType;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameTypeDao extends AbstractExtendedDao<GameType> {

    private static GameTypeDao instance = null;

    /**
     * Singleton constructor
     */
    private GameTypeDao() {
        super(GameType.class);
    }

    public static GameTypeDao getInstance() {
        if (instance == null) {
            instance = new GameTypeDao();
        }
        return instance;
    }

    @Override
    public List<GameType> listAll() throws SQLException {
        String query="select gt from entities.GameType gt order by gt.code asc";
        return list(query, null);
    }

    @Override
    public Optional<GameType> findByCode(String code) throws SQLException {
        String query="select gt from entities.GameType gt where gt.code = :CODE";
        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
