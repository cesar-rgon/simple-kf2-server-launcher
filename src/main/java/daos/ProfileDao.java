package daos;

import entities.Map;
import entities.Profile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ProfileDao extends CommonDao<Profile> {

    private static ProfileDao instance = null;

    /**
     * Singleton constructor
     */
    private ProfileDao() {
        super(Profile.class);
    }

    public static ProfileDao getInstance() {
        if (instance == null) {
            instance = new ProfileDao();
        }
        return instance;
    }

    public List<Profile> listAll() throws SQLException {
        String query = "select p from entities.Profile p order by p.name asc";
        return list(query, null);
    }

    public Optional<Profile> findByName(String profileName) throws SQLException {
        String query = "select p from entities.Profile p where p.name = :PROFILENAME";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PROFILENAME", profileName);
        return find(query, parameters);
    }
}
