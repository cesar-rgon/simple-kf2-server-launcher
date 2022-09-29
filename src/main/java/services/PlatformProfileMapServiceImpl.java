package services;

import daos.OfficialMapDao;
import daos.PlatformProfileMapDao;
import entities.OfficialMap;
import entities.AbstractPlatform;
import entities.Profile;
import entities.PlatformProfileMap;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlatformProfileMapServiceImpl implements PlatformProfileMapService {

    @Override
    public PlatformProfileMap createItem(PlatformProfileMap entity) throws Exception {
        return PlatformProfileMapDao.getInstance().insert(entity);
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
