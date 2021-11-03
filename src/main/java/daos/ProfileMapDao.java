package daos;

import entities.Profile;
import entities.ProfileMap;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ProfileMapDao extends AbstractDao<ProfileMap> {

    private static ProfileMapDao instance = null;

    /**
     * Singleton constructor
     */
    private ProfileMapDao() {
        super(ProfileMap.class);
    }

    public static ProfileMapDao getInstance() {
        if (instance == null) {
            instance = new ProfileMapDao();
        }
        return instance;
    }

    public Optional<ProfileMap> findByProfileNameMapName(String profileName, String mapName) throws SQLException {
        String query = "select pm from entities.ProfileMap pm where pm.profile.name = :PROFILENAME and pm.map.code = :MAPNAME";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PROFILENAME", profileName);
        parameters.put("MAPNAME", mapName);
        return find(query, parameters);
    }

    public List<ProfileMap> listProfileMaps(Profile profile) throws SQLException {
        String query = "select pm from entities.ProfileMap pm where pm.profile = :PROFILE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PROFILE", profile);
        return list(query, parameters);
    }
}
