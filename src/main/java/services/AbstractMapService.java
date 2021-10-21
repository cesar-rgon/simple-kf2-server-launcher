package services;

import daos.OfficialMapDao;
import daos.ProfileDao;
import daos.ProfileMapDao;
import entities.AbstractMap;
import entities.OfficialMap;
import entities.Profile;
import entities.ProfileMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public abstract class AbstractMapService implements AbstractExtendedService<AbstractMap> {

    private static final Logger logger = LogManager.getLogger(AbstractMapService.class);
    private ProfileMapService profileMapService;

    protected AbstractMapService() {
        super();
        this.profileMapService = new ProfileMapServiceImpl();
    }

    @Override
    public boolean updateItemCode(AbstractMap map, String oldCode) throws Exception {
        return false;
    }

    @Override
    public void updateItemDescription(AbstractMap map) throws Exception {
    }

    public abstract List<AbstractMap> listAll() throws SQLException;
    public abstract Optional<AbstractMap> findByCode(String mapName) throws SQLException;
    public abstract AbstractMap createItem(AbstractMap map) throws Exception;
    public abstract boolean deleteItem(AbstractMap map) throws Exception;
    public abstract boolean updateItem(AbstractMap map) throws SQLException;


    public Optional<AbstractMap> findMapByCode(String mapName) throws SQLException {
        Optional<AbstractMap> mapOpt = findByCode(mapName);
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (mapOpt.isPresent()) {
            if (idsMapasOficiales.contains(mapOpt.get().getId())) {
                mapOpt.get().setOfficial(true);
            } else {
                mapOpt.get().setOfficial(false);
            }
        }
        return mapOpt;
    }

    public List<AbstractMap> listAllMaps() throws SQLException {
        List<AbstractMap> mapList = listAll();
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (mapList != null && !mapList.isEmpty()) {
            mapList.forEach(m -> {
                if (idsMapasOficiales.contains(m.getId())) {
                    m.setOfficial(true);
                } else {
                    m.setOfficial(false);
                }
            });
        }
        return mapList;
    }

    public AbstractMap createMap(AbstractMap map, List<Profile> profileList) throws Exception {
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (idsMapasOficiales.contains(map.getId())) {
            map.setOfficial(true);
        } else {
            map.setOfficial(false);
        }

        AbstractMap insertedMap = createItem(map);
        if (insertedMap != null) {
            profileList.stream().forEach(profile -> {
                try {
                    ProfileMap newProfileMap = new ProfileMap(profile, map);
                    profileMapService.createItem(newProfileMap);
                    map.getProfileMapList().add(newProfileMap);
                } catch (Exception e) {
                    logger.error("Error creating the relation between the profile: " + profile.getCode() + " and the new map: " + insertedMap.getCode(), e);
                }
            });
        }
        return insertedMap;
    }

    public AbstractMap addProfilesToMap(AbstractMap map, List<Profile> profileList) {
        profileList.stream().forEach(profile -> {
            try {
                if (!profile.getMapList().contains(map)) {
                    ProfileMap newProfileMap = new ProfileMap(profile, map);
                    profileMapService.createItem(newProfileMap);
                    map.getProfileMapList().add(newProfileMap);
                }
            } catch (Exception e) {
                logger.error("Error creating the relation between the profile: " + profile.getCode() + " and the new map: " + map.getCode(), e);
            }
        });
        return map;
    }


    protected AbstractMap deleteMap(AbstractMap map, Profile profile) throws Exception {
        ProfileMap.IdProfileMap idProfileMap = new ProfileMap.IdProfileMap(profile.getId(), map.getId());
        ProfileMap profileMap = ProfileMapDao.getInstance().get(idProfileMap);
        profileMapService.deleteItem(profileMap);
        return map;
    }

}
