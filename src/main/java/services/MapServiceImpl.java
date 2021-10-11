package services;

import daos.MapDao;
import daos.ProfileDao;
import entities.Map;
import entities.Profile;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ProfileToDisplay;
import pojos.ProfileToDisplayFactory;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MapServiceImpl implements MapService {

    private static final Logger logger = LogManager.getLogger(MapServiceImpl.class);

    private final PropertyService propertyService;
    private final ProfileToDisplayFactory profileToDisplayFactory;

    public MapServiceImpl() {
        super();
        this.propertyService = new PropertyServiceImpl();
        this.profileToDisplayFactory = new ProfileToDisplayFactory();
    }


    @Override
    public Map createItem(Map map) throws Exception {
        return MapDao.getInstance().insert(map);
    }

    @Override
    public boolean updateItemCode(Map map, String oldCode) throws Exception {
        return false;
    }

    @Override
    public void updateItemDescription(Map map) throws Exception {
    }

    @Override
    public boolean deleteItem(Map map) throws Exception {
        return MapDao.getInstance().remove(map);
    }

    @Override
    public Map createMap(Map map) throws Exception {
        Map insertedMap = createItem(map);
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

    @Override
    public boolean addProfilesToMap(Map map, List<Profile> profileList) throws SQLException {
        profileList.stream().forEach(profile -> {
            try {
                profile.getMapList().add(map);
                ProfileDao.getInstance().update(profile);
            } catch (SQLException e) {
                logger.error("Error updating the profile " + profile.getCode() + " with the map: " + map.getCode(), e);
            }
        });
        map.getProfileList().addAll(profileList);
        return MapDao.getInstance().update(map);
    }

    @Override
    public Map deleteMap(Map map, Profile profile, String installationFolder) throws Exception {
        profile.getMapList().remove(map);
        ProfileDao.getInstance().update(profile);
        map.getProfileList().remove(profile);
        MapDao.getInstance().update(map);

        if (!map.isOfficial() && map.getProfileList().isEmpty()) {
            deleteItem(map);
            File photo = new File(installationFolder + map.getUrlPhoto());
            photo.delete();
            File cacheFolder = new File(installationFolder + "/KFGame/Cache/" + map.getIdWorkShop());
            FileUtils.deleteDirectory(cacheFolder);
        }
        return map;
    }

    @Override
    public List<ProfileToDisplay> selectProfilesToImport(List<Profile> allProfiles, String defaultSelectedProfileName) throws Exception {
        List<ProfileToDisplay> allProfilesToDisplay = profileToDisplayFactory.newOnes(allProfiles);
        allProfilesToDisplay.stream().filter(p -> p.getProfileName().equalsIgnoreCase(defaultSelectedProfileName)).forEach(profile -> profile.setSelected(true));
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfiles");
        return Utils.selectProfilesDialog(headerText + ":", allProfilesToDisplay);
    }
}
