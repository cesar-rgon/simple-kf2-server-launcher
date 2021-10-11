package daos;

import entities.Length;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LengthDao extends AbstractDao<Length> {

    private static LengthDao instance = null;

    /**
     * Singleton constructor
     */
    private LengthDao() {
        super(Length.class);
    }

    public static LengthDao getInstance() {
        if (instance == null) {
            instance = new LengthDao();
        }
        return instance;
    }

    @Override
    public List<Length> listAll() throws SQLException {
        String query="select l from entities.Length l order by l.code asc";
        return list(query, null);
    }

    @Override
    public Optional<Length> findByCode(String code) throws SQLException {
        String query="select l from entities.Length l where l.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
