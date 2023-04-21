package daos;

import entities.OfficialMap;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class OfficialMapDao extends AbstractDao<OfficialMap> {

    public OfficialMapDao(EntityManager em) {
        super(OfficialMap.class, em);
    }

    public List<OfficialMap> listAll() throws SQLException {
        String query="select om from entities.OfficialMap om";
        return list(query, null);
    }

    public Optional<OfficialMap> findByCode(String code) throws SQLException {
        String query="select om from entities.OfficialMap om where om.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }

}
