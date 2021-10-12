package daos;

import entities.OfficialMap;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class OfficialMapDao extends AbstractDao<OfficialMap> {

    private static OfficialMapDao instance = null;

    /**
     * Singleton constructor
     */
    private OfficialMapDao() {
        super(OfficialMap.class);
    }

    public static OfficialMapDao getInstance() {
        if (instance == null) {
            instance = new OfficialMapDao();
        }
        return instance;
    }

    @Override
    public List<OfficialMap> listAll() throws SQLException {
        String query="select m from entities.OfficialMap om";
        return list(query, null);
    }

    @Override
    public Optional<OfficialMap> findByCode(String code) throws SQLException {
        String query="select m from entities.OfficialMap om where om.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }

}
