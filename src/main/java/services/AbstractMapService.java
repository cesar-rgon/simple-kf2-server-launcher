package services;

import daos.ProfileDao;
import entities.AbstractMap;
import entities.Profile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

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

    public abstract AbstractMap createItem(AbstractMap map) throws Exception;
    public abstract boolean deleteItem(AbstractMap map) throws Exception;
    public abstract boolean updateItem(AbstractMap map) throws SQLException;

    public AbstractMap createMap(AbstractMap map) throws Exception {
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

    public boolean addProfilesToMap(AbstractMap map, List<Profile> profileList) throws SQLException {
        profileList.stream().forEach(profile -> {
            try {
                profile.getMapList().add(map);
                ProfileDao.getInstance().update(profile);
            } catch (SQLException e) {
                logger.error("Error updating the profile " + profile.getCode() + " with the map: " + map.getCode(), e);
            }
        });
        map.getProfileList().addAll(profileList);
        return updateItem(map);
    }


    protected AbstractMap deleteMap(AbstractMap map, Profile profile) throws Exception {
        profile.getMapList().remove(map);
        ProfileDao.getInstance().update(profile);
        map.getProfileList().remove(profile);
        updateItem(map);
        return map;
    }

}
