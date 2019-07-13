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

    public List<Map> listAll() throws SQLException {
        String query="select m from entities.Map m order by m.id asc";
        return list(query, null);
    }

    public List<Map> listDownloadedMaps() throws SQLException {
        String query="select m from entities.Map m where m.downloaded=true order by m.id asc";
        return list(query, null);
    }

    public List<Map> listNotDownloadedMaps() throws SQLException {
        String query="select m from entities.Map m where m.downloaded=false order by m.id asc";
        return list(query, null);
    }

    public Optional<Map> findByCode(String code) throws SQLException {
        String query="select m from entities.Map m where m.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}