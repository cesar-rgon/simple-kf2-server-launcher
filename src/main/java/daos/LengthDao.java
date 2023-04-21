package daos;

import entities.Length;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LengthDao extends AbstractDao<Length> {

    public LengthDao(EntityManager em) {
        super(Length.class, em);
    }

    public List<Length> listAll() throws SQLException {
        String query="select l from entities.Length l order by l.code asc";
        return list(query, null);
    }

    public Optional<Length> findByCode(String code) throws SQLException {
        String query="select l from entities.Length l where l.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
