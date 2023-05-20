package services;

import daos.PlatformProfileMapDao;
import entities.AbstractMap;
import entities.AbstractPlatform;
import entities.PlatformProfileMap;
import entities.Profile;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PlatformProfileMapServiceImpl extends AbstractService<PlatformProfileMap> implements PlatformProfileMapService {

    public PlatformProfileMapServiceImpl(EntityManager em) {
        super(em);
    }

    @Override
    public PlatformProfileMap createItem(PlatformProfileMap entity) throws Exception {
        return new PlatformProfileMapDao(em).insert(entity);
    }

    @Override
    public boolean updateItem(PlatformProfileMap platformProfileMap) throws Exception {
        return new PlatformProfileMapDao(em).update(platformProfileMap);
    }

    @Override
    public List<PlatformProfileMap> listAll() throws Exception {
        return new PlatformProfileMapDao(em).listPlatformProfileMaps();
    }

    @Override
    public Optional<PlatformProfileMap> findByCode(String code) throws Exception {
        return Optional.empty();
    }

    @Override
    public boolean deleteItem(PlatformProfileMap entity) throws Exception {
        return new PlatformProfileMapDao(em).remove(entity);
    }

    @Override
    public boolean deleteItem(PlatformProfileMap entity, List<Profile> profileList) throws Exception {
        return false;
    }

    @Override
    public boolean deleteAllItems(List<PlatformProfileMap> entityList, List<Profile> profileList) throws Exception {
        return false;
    }

    @Override
    public Optional<PlatformProfileMap> findPlatformProfileMapByNames(String platformName, String profileName, String mapName) throws SQLException {
        return new PlatformProfileMapDao(em).findByPlatformNameProfileNameMapName(platformName, profileName, mapName);
    }

    @Override
    public List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform, Profile profile) throws SQLException {
        return new PlatformProfileMapDao(em).listPlatformProfileMaps(platform, profile);
    }

    @Override
    public List<PlatformProfileMap> listPlatformProfileMaps(List<AbstractPlatform> platformList, List<Profile> profileList) throws SQLException {
        return new PlatformProfileMapDao(em).listPlatformProfileMaps(platformList, profileList);
    }

    @Override
    public List<PlatformProfileMap> listPlatformProfileMaps(AbstractMap map) throws SQLException {
        return new PlatformProfileMapDao(em).listPlatformProfileMaps(map);
    }

    @Override
    public List<PlatformProfileMap> listPlatformProfileMaps(Profile profile) throws SQLException {
        return new PlatformProfileMapDao(em).listPlatformProfileMaps(profile);
    }

    @Override
    public List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform) throws SQLException {
        return new PlatformProfileMapDao(em).listPlatformProfileMaps(platform);
    }

    @Override
    public List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform, AbstractMap map) throws SQLException {
        return new PlatformProfileMapDao(em).listPlatformProfileMaps(platform, map);
    }
}
