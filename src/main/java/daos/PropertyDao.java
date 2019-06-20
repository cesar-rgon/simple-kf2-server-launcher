package daos;

import entities.Property;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PropertyDao extends CommonDao<Property> {

    private static PropertyDao instance = null;

    /**
     * Singleton constructor
     */
    private PropertyDao() {
        super(Property.class);
    }

    public static PropertyDao getInstance() {
        if (instance == null) {
            instance = new PropertyDao();
        }
        return instance;
    }

    public List<Property> listAll() throws SQLException {
        String query = "select p from entities.Property p order by p.id asc";
        return list(query, null);
    }

    public Optional<Property> findByKey(String key) throws SQLException {
        String query = "select p from entities.Property p where p.key = :KEY_PROPERTY";
        Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("KEY_PROPERTY", key);
        return find(query, parameters);
    }
}
