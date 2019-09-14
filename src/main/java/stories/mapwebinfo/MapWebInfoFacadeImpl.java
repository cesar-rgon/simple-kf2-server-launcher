package stories.mapwebinfo;

import daos.MapDao;
import daos.ProfileDao;
import dtos.MapDto;
import dtos.factories.MapDtoFactory;
import entities.Map;
import entities.Profile;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MapWebInfoFacadeImpl implements MapWebInfoFacade {

    private final PropertyService propertyService;
    private final MapDtoFactory mapDtoFactory;

    public MapWebInfoFacadeImpl() {
        super();
        propertyService = new PropertyServiceImpl();
        mapDtoFactory = new MapDtoFactory();
    }

    @Override
    public boolean isMapInProfile(Long idWorkShop, String profileName) {
        try {
            Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
            if (profileOpt.isPresent()) {
                Optional<Map> mapOpt = profileOpt.get().getMapList().stream().filter(m -> idWorkShop.equals(m.getIdWorkShop())).findFirst();
                return mapOpt.isPresent();
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
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

    @Override
    public MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String strUrlMapImage, String installationFolder, String profileName) throws Exception {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (!profileOpt.isPresent()) {
            return null;
        }
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();
        Map insertedMap = createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, false, null, profileOpt.get());
        return insertedMap != null ? mapDtoFactory.newDto(insertedMap): null;
    }

    private Map createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto, boolean downloaded, Boolean mod, Profile profile) throws Exception {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        List<Profile> profileList = new ArrayList<Profile>();
        profileList.add(profile);
        Map customMap = new Map(mapName, false, urlInfo, idWorkShop, urlPhoto, downloaded, mod, profileList);
        Map insertedMap = MapDao.getInstance().insert(customMap);
        profile.getMapList().add(insertedMap);
        ProfileDao.getInstance().update(profile);
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
    public boolean addProfileToMap(String mapName, String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {
            Optional<Map> mapOpt = profileOpt.get().getMapList().stream().filter(m -> m.getCode().equalsIgnoreCase(mapName)).findFirst();
            if (!mapOpt.isPresent()) {
                Optional<Map> mapOptional = MapDao.getInstance().findByCode(mapName);
                if (mapOptional.isPresent()) {
                    profileOpt.get().getMapList().add(mapOptional.get());
                    ProfileDao.getInstance().update(profileOpt.get());
                    mapOptional.get().getProfileList().add(profileOpt.get());
                    return MapDao.getInstance().update(mapOptional.get());
                }
            }
        }
        return false;
    }
}
