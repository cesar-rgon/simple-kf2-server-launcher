package services;

import daos.OfficialMapDao;
import daos.ProfileDao;
import entities.AbstractMap;
import entities.OfficialMap;
import entities.Profile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public abstract class AbstractMapService implements Kf2Service<AbstractMap> {

    private static final Logger logger = LogManager.getLogger(AbstractMapService.class);

    public AbstractMapService() {
        super();
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

    public AbstractMap createMap(AbstractMap map) throws Exception {
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        if (idsMapasOficiales.contains(map.getId())) {
            map.setOfficial(true);
        } else {
            map.setOfficial(false);
        }
        AbstractMap insertedMap = createItem(map);
        if (insertedMap != null) {
            map.getProfileList().stream().forEach(profile -> {
                try {
                    profile.getMapList().add(insertedMap);
                    ProfileDao.getInstance().update(profile);
                } catch (SQLException e) {
                    logger.error("Error updating the profile " + profile.getCode() + " with a new map: " + insertedMap.getCode(), e);
                }
            });
        }
        return insertedMap;
    }

    public boolean addProfilesToMap(AbstractMap map, List<Profile> profileList) {
        AtomicBoolean profilesAdded = new AtomicBoolean(false);
        profileList.stream().forEach(profile -> {
            try {
                if (!profile.getMapList().contains(map)) {
                    profile.getMapList().add(map);
                    ProfileDao.getInstance().update(profile);
                    map.getProfileList().addAll(profileList);
                    if (updateItem(map)) {
                        profilesAdded.set(true);
                    }
                }
            } catch (SQLException e) {
                logger.error("Error updating the profile " + profile.getCode() + " with the map: " + map.getCode(), e);
            }
        });
        return profilesAdded.get();
    }


    protected AbstractMap deleteMap(AbstractMap map, Profile profile) throws Exception {
        profile.getMapList().remove(map);
        ProfileDao.getInstance().update(profile);
        map.getProfileList().remove(profile);
        updateItem(map);
        return map;
    }

}
