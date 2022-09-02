package services;

import daos.OfficialMapDao;
import daos.PlatformProfileMapDao;
import entities.OfficialMap;
import entities.Platform;
import entities.Profile;
import entities.PlatformProfileMap;
import pojos.enums.EnumPlatform;

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
        Optional<PlatformProfileMap> platformProfileMapOpt = PlatformProfileMapDao.getInstance().findByPlatformNameProfileNameMapName(platformName, profileName, mapName);
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (platformProfileMapOpt.isPresent()) {
            if (idsMapasOficiales.contains(platformProfileMapOpt.get().getMap().getId())) {
                platformProfileMapOpt.get().getMap().setOfficial(true);
            } else {
                platformProfileMapOpt.get().getMap().setOfficial(false);
            }
        }
        return platformProfileMapOpt;
    }

    @Override
    public List<PlatformProfileMap> listPlatformProfileMaps(Platform platform, Profile profile) throws SQLException {
        List<PlatformProfileMap> platformProfileMapList = PlatformProfileMapDao.getInstance().listPlatformProfileMaps(platform, profile);
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (platformProfileMapList != null && !platformProfileMapList.isEmpty()) {

            platformProfileMapList.forEach(ppm -> {
                if (idsMapasOficiales.contains(ppm.getMap().getId())) {
                    ppm.getMap().setOfficial(true);
                } else {
                    ppm.getMap().setOfficial(false);
                }
            });
        }
        return platformProfileMapList;
    }
}
