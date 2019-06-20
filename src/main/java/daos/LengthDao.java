package daos;

import entities.Length;

import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LengthDao extends CommonDao<Length> {

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

    public List<Length> listAll() throws SQLException {
        String query="select l from entities.Length l order by l.id asc";
        return list(query, null);
    }

    public Optional<Length> findByCode(String code) throws SQLException {
        String query="select l from entities.Length l where l.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
