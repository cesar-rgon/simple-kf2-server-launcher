package stories.mapwebinfo;

import constants.Constants;
import daos.MapDao;
import entities.Map;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

public class MapWebInfoFacadeImpl implements MapWebInfoFacade {

    private final PropertyService propertyService;

    public MapWebInfoFacadeImpl() {
        super();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public boolean isMapInDataBase(Long idWorkShop) {
        try {
            Optional<Map> mapOpt = MapDao.getInstance().findByIdWorkShop(idWorkShop);
            return mapOpt.isPresent();
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
    public Map createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String strUrlMapImage, String installationFolder) throws Exception {
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_MAP_CUSTOM_LOCAL_FOLDER);
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();
        return createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, false);
    }

    private Map createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto, boolean downloaded) throws Exception {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_MAP_BASE_URL_WORKSHOP);
        String urlInfo = baseUrlWorkshop + idWorkShop;
        Map customMap = new Map(mapName, false, urlInfo, idWorkShop, urlPhoto, downloaded);
        return MapDao.getInstance().insert(customMap);
    }
}
