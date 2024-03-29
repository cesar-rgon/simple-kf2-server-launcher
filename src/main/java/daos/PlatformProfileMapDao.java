package daos;

import entities.AbstractMap;
import entities.AbstractPlatform;
import entities.Profile;
import entities.PlatformProfileMap;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PlatformProfileMapDao extends AbstractDao<PlatformProfileMap> {

    public PlatformProfileMapDao(EntityManager em) {
        super(PlatformProfileMap.class, em);
    }

    public Optional<PlatformProfileMap> findByPlatformNameProfileNameMapName(String platformName, String profileName, String mapName) throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm where ppm.platform.code = :PLATFORM and ppm.profile.name = :PROFILENAME and ppm.map.code = :MAPNAME";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PLATFORM", platformName);
        parameters.put("PROFILENAME", profileName);
        parameters.put("MAPNAME", mapName);
        return find(query, parameters);
    }

    public List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform, Profile profile) throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm where ppm.platform = :PLATFORM and ppm.profile = :PROFILE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PLATFORM", platform);
        parameters.put("PROFILE", profile);
        return list(query, parameters);
    }

    public List<PlatformProfileMap> listPlatformProfileMaps(List<AbstractPlatform> platformList, List<Profile> profileList) throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm where ppm.platform in (:PLATFORM) and ppm.profile in (:PROFILE)";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PLATFORM", platformList);
        parameters.put("PROFILE", profileList);
        return list(query, parameters);
    }

    public List<PlatformProfileMap> listPlatformProfileMaps(AbstractMap map) throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm where ppm.map = :MAP";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("MAP", map);
        return list(query, parameters);
    }

    public List<PlatformProfileMap> listPlatformProfileMaps(Profile profile) throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm where ppm.profile = :PROFILE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PROFILE", profile);
        return list(query, parameters);
    }

    public List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform) throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm where ppm.platform = :PLATFORM";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PLATFORM", platform);
        return list(query, parameters);
    }

    public List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform, AbstractMap map) throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm where ppm.platform = :PLATFORM and ppm.map = :MAP";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PLATFORM", platform);
        parameters.put("MAP", map);
        return list(query, parameters);
    }

    public List<PlatformProfileMap> listPlatformProfileMaps() throws SQLException {
        String query = "select ppm from entities.PlatformProfileMap ppm";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        return list(query, parameters);
    }


}
