package services;

import daos.PlatformProfileMapDao;
import entities.AbstractPlatform;
import entities.PlatformProfileMap;
import entities.Profile;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PlatformProfileMapServiceImpl implements PlatformProfileMapService {

    @Override
    public PlatformProfileMap createItem(PlatformProfileMap entity) throws Exception {
        return PlatformProfileMapDao.getInstance().insert(entity);
    }

    @Override
    public boolean updateItem(PlatformProfileMap platformProfileMap) throws Exception {
        return PlatformProfileMapDao.getInstance().update(platformProfileMap);
    }

    @Override
    public List<PlatformProfileMap> listAll() throws Exception {
        return PlatformProfileMapDao.getInstance().listPlatformProfileMaps();
    }

    @Override
    public Optional<PlatformProfileMap> findByCode(String code) throws Exception {
        return Optional.empty();
    }

    @Override
    public boolean deleteItem(PlatformProfileMap entity) throws Exception {
        return PlatformProfileMapDao.getInstance().remove(entity);
    }

    @Override
    public Optional<PlatformProfileMap> findPlatformProfileMapByNames(String platformName, String profileName, String mapName) throws SQLException {
        return PlatformProfileMapDao.getInstance().findByPlatformNameProfileNameMapName(platformName, profileName, mapName);
    }

    @Override
    public List<PlatformProfileMap> listPlatformProfileMaps(AbstractPlatform platform, Profile profile) throws SQLException {
        return PlatformProfileMapDao.getInstance().listPlatformProfileMaps(platform, profile);
    }
}
