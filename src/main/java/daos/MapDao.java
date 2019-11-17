package daos;

import entities.Map;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MapDao extends CommonDao<Map> {

    private static MapDao instance = null;

    /**
     * Singleton constructor
     */
    private MapDao() {
        super(Map.class);
    }

    public static MapDao getInstance() {
        if (instance == null) {
            instance = new MapDao();
        }
        return instance;
    }

    public List<Map> listNotOfficialMaps() throws SQLException {
        String query="select m from entities.Map m where m.official=false";
        return list(query, null);
    }

    public Optional<Map> findByCode(String code) throws SQLException {
        String query="select m from entities.Map m where m.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }

    public Optional<Map> findByIdWorkShop(Long idWorkShop) throws SQLException {
        String query="select m from entities.Map m where m.official=false and m.idWorkShop=:IDWORKSHOP";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("IDWORKSHOP", idWorkShop);
        List<Map> list = list(query, parameters);
        return (list != null && !list.isEmpty())? Optional.ofNullable(list.get(0)): Optional.empty();
    }

    public List<Map> listOfficialMaps() throws SQLException {
        String query="select m from entities.Map m where m.official=true order by m.code asc";
        return list(query, null);
    }
}
