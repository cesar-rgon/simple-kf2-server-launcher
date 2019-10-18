package stories.mapwebinfo;

import daos.MapDao;
import daos.ProfileDao;
import dtos.MapDto;
import dtos.factories.MapDtoFactory;
import entities.Map;
import entities.Profile;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ProfileToDisplay;
import pojos.ProfileToDisplayFactory;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.profilesedition.ProfilesEditionFacadeImpl;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapWebInfoFacadeImpl implements MapWebInfoFacade {

    private static final Logger logger = LogManager.getLogger(MapWebInfoFacadeImpl.class);
    private final PropertyService propertyService;
    private final MapDtoFactory mapDtoFactory;

    public MapWebInfoFacadeImpl() {
        super();
        propertyService = new PropertyServiceImpl();
        mapDtoFactory = new MapDtoFactory();
    }

    @Override
    public String findPropertyValue(String key) throws Exception {
        return propertyService.getPropertyValue("properties/config.properties", key);
    }

    @Override
    public boolean isCorrectInstallationFolder(String installationFolder) {
        if (StringUtils.isNotBlank(installationFolder)) {
            File windowsExecutable = new File(installationFolder + "/Binaries/Win64/KFServer.exe");
            File linuxExecutable = new File(installationFolder + "/Binaries/Win64/KFGameSteamServer.bin.x86_64");
            if (windowsExecutable.exists() && linuxExecutable.exists()) {
                return true;
            }
        }
        return false;
    }

    private Profile findProfileByName(String profileName) {
        try {
            Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
            if (profileOpt.isPresent()) {
                return profileOpt.get();
            }
            return null;
        } catch (SQLException e) {
            logger.error("Error finding a profile by name " + profileName, e);
            return null;
        }
    }

    @Override
    public MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String strUrlMapImage, String installationFolder, List<String> profileNameList) throws Exception {
        List<Profile> profileList = profileNameList.stream().map(pn -> findProfileByName(pn)).collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            return null;
        }
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();
        Map insertedMap = createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, false, null, profileList);
        return insertedMap != null ? mapDtoFactory.newDto(insertedMap): null;
    }

    private Map createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto, boolean downloaded, Boolean mod, List<Profile> profileList) throws Exception {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        Map customMap = new Map(mapName, false, urlInfo, idWorkShop, urlPhoto, downloaded, mod, profileList);
        Map insertedMap = MapDao.getInstance().insert(customMap);
        if (insertedMap != null) {
            profileList.stream().forEach(profile -> {
                try {
                    profile.getMapList().add(insertedMap);
                    ProfileDao.getInstance().update(profile);
                } catch (SQLException e) {
                    logger.error("Error updating the profile " + profile.getName() + " with a new map: " + insertedMap.getCode(), e);
                }
            });
        }
        return insertedMap;
    }

    @Override
    public MapDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException {
        Optional<Map> mapOpt = MapDao.getInstance().findByIdWorkShop(idWorkShop);
        if (mapOpt.isPresent()) {
            return mapDtoFactory.newDto(mapOpt.get());
        }
        return null;
    }

    @Override
    public boolean addProfilesToMap(String mapName, List<String> profileNameList) throws SQLException {
        List<Profile> profileList = profileNameList.stream().map(pn -> findProfileByName(pn)).collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            return false;
        }
        Optional<Map> mapOptional = MapDao.getInstance().findByCode(mapName);
        if (!mapOptional.isPresent()) {
            return false;
        }
        profileList.stream().forEach(profile -> {
            try {
                profile.getMapList().add(mapOptional.get());
                ProfileDao.getInstance().update(profile);
            } catch (SQLException e) {
                logger.error("Error updating the profile " + profile.getName() + " with the map: " + mapOptional.get().getCode(), e);
            }
        });

        mapOptional.get().getProfileList().addAll(profileList);
        return MapDao.getInstance().update(mapOptional.get());
    }

    private boolean isMapInProfile(Long idWorkShop, Profile profile) {
        Optional<Map> mapOpt = profile.getMapList().stream().filter(m -> idWorkShop.equals(m.getIdWorkShop())).findFirst();
        return mapOpt.isPresent();
    }

    @Override
    public List<ProfileToDisplay> getProfilesWithoutMap(Long idWorkShop) throws SQLException {
        List<Profile> allProfiles = ProfileDao.getInstance().listAll();
        List<Profile> profilesWithoutMap = allProfiles.stream().filter(p -> !isMapInProfile(idWorkShop, p)).collect(Collectors.toList());
        ProfileToDisplayFactory profileToDisplayFactory = new ProfileToDisplayFactory();
        return profileToDisplayFactory.newOnes(profilesWithoutMap);
    }
}
