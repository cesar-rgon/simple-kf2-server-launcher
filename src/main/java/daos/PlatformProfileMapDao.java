package daos;

import entities.Platform;
import entities.Profile;
import entities.PlatformProfileMap;
import pojos.enums.EnumPlatform;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PlatformProfileMapDao extends AbstractDao<PlatformProfileMap> {

    private static PlatformProfileMapDao instance = null;

    /**
     * Singleton constructor
     */
    private PlatformProfileMapDao() {
        super(PlatformProfileMap.class);
    }

    public static PlatformProfileMapDao getInstance() {
        if (instance == null) {
            instance = new PlatformProfileMapDao();
        }
        return instance;
    }

    public Optional<PlatformProfileMap> findByPlatformNameProfileNameMapName(String platformName, String profileName, String mapName) throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm where ppm.platform.code = :PLATFORM and ppm.profile.name = :PROFILENAME and ppm.map.code = :MAPNAME";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PLATFORM", platformName);
        parameters.put("PROFILENAME", profileName);
        parameters.put("MAPNAME", mapName);
        return find(query, parameters);
    }

    public List<PlatformProfileMap> listPlatformProfileMaps(Platform platform, Profile profile) throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm where ppm.platform = :PLATFORM and ppm.profile = :PROFILE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PLATFORM", platform);
        parameters.put("PROFILE", profile);
        return list(query, parameters);
    }
}
