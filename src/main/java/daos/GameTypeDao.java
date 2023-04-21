package daos;

import entities.GameType;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameTypeDao extends AbstractDao<GameType> {

    public GameTypeDao(EntityManager em) {
        super(GameType.class, em);
    }

    public List<GameType> listAll() throws SQLException {
        String query="select gt from entities.GameType gt order by gt.code asc";
        return list(query, null);
    }

    public Optional<GameType> findByCode(String code) throws SQLException {
        String query="select gt from entities.GameType gt where gt.code = :CODE";
        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
