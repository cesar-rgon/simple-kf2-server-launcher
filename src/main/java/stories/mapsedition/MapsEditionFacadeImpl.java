package stories.mapsedition;

import constants.Constants;
import daos.MapDao;
import dtos.MapDto;
import dtos.factories.MapDtoFactory;
import entities.Map;
import org.apache.commons.lang3.StringUtils;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MapsEditionFacadeImpl implements MapsEditionFacade {

    private final MapDtoFactory mapDtoFactory;
    private final PropertyService propertyService;

    public MapsEditionFacadeImpl() {
        super();
        this.mapDtoFactory = new MapDtoFactory();
        this.propertyService = new PropertyServiceImpl();
    }

    @Override
    public List<Map> listAllMaps() throws SQLException {
        return MapDao.getInstance().listAll();
    }

    @Override
    public List<MapDto> getDtos(List<Map> mapList) {
        return mapDtoFactory.newDtos(mapList);
    }

    @Override
    public MapDto getDto(Map map) {
        return mapDtoFactory.newDto(map);
    }

    @Override
    public String findPropertyValue(String key) throws Exception {
        return propertyService.getPropertyValue("properties/config.properties", key);
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

    @Override
    public Map createNewCustomMapFromWorkshop(String urlIdWorkshop, String installationFolder) throws Exception {
        Long idWorkShop = null;
        URL urlWorkShop = null;
        if (urlIdWorkshop.contains("http")) {
            urlWorkShop = new URL(urlIdWorkshop);
            String[] array = urlIdWorkshop.split("=");
            idWorkShop = Long.parseLong(array[1]);
        } else {
            idWorkShop = Long.parseLong(urlIdWorkshop);
            String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_MAP_BASE_URL_WORKSHOP);
            urlWorkShop = new URL(baseUrlWorkshop + idWorkShop);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlWorkShop.openStream()));
        String strUrlMapImage = null;
        String mapName = null;
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("image_src")) {
                String[] array = line.split("\"");
                strUrlMapImage = array[3];
            }
            if (line.contains("workshopItemTitle")) {
                String[] array = line.split(">");
                String[] array2 = array[1].split("<");
                mapName = array2[0];
            }
            if (StringUtils.isNotEmpty(strUrlMapImage) && StringUtils.isNotEmpty(mapName)) {
                break;
            }
        }
        reader.close();
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_MAP_CUSTOM_LOCAL_FOLDER);
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

        return createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, false);
    }

    @Override
    public Map createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder) throws Exception {
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_MAP_BASE_URL_WORKSHOP);
        URL urlWorkShop = new URL(baseUrlWorkshop + idWorkShop);
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlWorkShop.openStream()));
        String strUrlMapImage = null;
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("image_src")) {
                String[] array = line.split("\"");
                strUrlMapImage = array[3];
            }
            if (StringUtils.isNotEmpty(strUrlMapImage)) {
                break;
            }
        }
        reader.close();
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_MAP_CUSTOM_LOCAL_FOLDER);
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

        return createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, true);
    }

    @Override
    public MapDto deleteSelectedMap(String mapName) throws SQLException {
        if (StringUtils.isBlank(mapName)) {
            return null;
        }
        Optional<Map> mapOpt = MapDao.getInstance().findByCode(mapName);
        if (mapOpt.isPresent() && MapDao.getInstance().remove(mapOpt.get())) {
            return mapDtoFactory.newDto(mapOpt.get());
        }
        return null;
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
    public Optional<Map> findMapByCode(String mapName) throws SQLException {
        return MapDao.getInstance().findByCode(mapName);
    }
}
