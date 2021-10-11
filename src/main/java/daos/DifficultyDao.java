package daos;

import entities.Difficulty;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class DifficultyDao extends AbstractDao<Difficulty> {

    private static DifficultyDao instance = null;

    /**
     * Singleton constructor
     */
    private DifficultyDao() {
        super(Difficulty.class);
    }

    public static DifficultyDao getInstance() {
        if (instance == null) {
            instance = new DifficultyDao();
        }
        return instance;
    }

    @Override
    public List<Difficulty> listAll() throws SQLException {
        String query="select d from entities.Difficulty d order by d.code asc";
        return list(query, null);
    }

    @Override
    public Optional<Difficulty> findByCode(String code) throws SQLException {
        String query="select d from entities.Difficulty d where d.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
