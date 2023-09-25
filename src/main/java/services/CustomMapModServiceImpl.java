package services;

import daos.CustomMapModDao;
import entities.*;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import stories.addcustommapstoprofile.AddCustomMapsToProfileFacadeImpl;
import utils.Utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CustomMapModServiceImpl extends AbstractMapService {

    private static final Logger logger = LogManager.getLogger(CustomMapModServiceImpl.class);

    private final PlatformProfileMapService platformProfileMapService;
    private final PlatformService platformService;
    private final PropertyService propertyService;

    public CustomMapModServiceImpl(EntityManager em) {
        super(em);
        this.platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        this.platformService = new PlatformServiceImpl(em);
        this.propertyService = new PropertyServiceImpl();
    }

    @Override
    public List listAll() throws SQLException {
        return new CustomMapModDao(em).listAll();
    }

    @Override
    public Optional<AbstractMap> findByCode(String mapName) throws Exception {
        Optional<CustomMapMod> customMapModOptional = new CustomMapModDao(em).findByCode(mapName);
        if (!customMapModOptional.isPresent()) {
            return Optional.empty();
        }
        return Optional.ofNullable(customMapModOptional.get());
    }

    @Override
    public AbstractMap createItem(AbstractMap map) throws Exception {
        return new CustomMapModDao(em).insert((CustomMapMod) map);
    }

    @Override
    public boolean deleteItem(AbstractMap map) throws Exception {
        return new CustomMapModDao(em).remove((CustomMapMod) map);
    }

    @Override
    public boolean deleteItem(AbstractMap map, List<Profile> profileList) throws Exception {
        return false;
    }

    @Override
    public boolean deleteAllItems(List<AbstractMap> entityList, List<Profile> profileList) throws Exception {
        return false;
    }

    @Override
    public boolean updateItem(AbstractMap map) throws SQLException {
        return new CustomMapModDao(em).update((CustomMapMod) map);
    }

    @Override
    protected boolean idDownloadedMap() {
        return false;
    }

    public CustomMapMod deleteMap(AbstractPlatform platform, CustomMapMod map, Profile profile) throws Exception {

        super.deleteMap(platform, map, profile);
        List<PlatformProfileMap> ppmListForMap = platformProfileMapService.listPlatformProfileMaps(map);
        List<PlatformProfileMap> ppmListForPlatformMap = ppmListForMap.stream().filter(ppm -> ppm.getPlatform().getCode().equals(platform.getCode())).collect(Collectors.toList());

        if (ppmListForPlatformMap.isEmpty()) {
            File photo = new File(platform.getInstallationFolder() + map.getUrlPhoto());
            photo.delete();
            File cacheFolder = new File(platform.getInstallationFolder() + "/KFGame/Cache/" + map.getIdWorkShop());
            FileUtils.deleteDirectory(cacheFolder);
        }

        if (ppmListForMap.isEmpty()) {
            deleteItem(map);
        }
        return map;
    }

    public Optional findByIdWorkShop(Long idWorkShop) throws SQLException {
        return new CustomMapModDao(em).findByIdWorkShop(idWorkShop);
    }


    public CustomMapMod createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, List<String> profileNameList, String mapName, String strUrlMapImage, StringBuffer success, StringBuffer errors) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        List<Profile> profileList = profileService.getProfileListByNames(profileNameList, success, errors);
        List<AbstractPlatform> platformList = platformService.getPlatformListByNames(platformNameList, success, errors);
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

        File localfile = null;
        for (AbstractPlatform platform: platformList) {
            String absoluteTargetFolder = platform.getInstallationFolder() + customMapLocalFolder;
            localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, Long.toString(idWorkShop));
        }

        if (localfile == null) {
            String message = "The map with name " + mapName + " could not be added to all selected profiles and platforms";
            errors.append(message + "\n");
            return null;
        }

        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();
        CustomMapMod insertedMap = createNewCustomMap(platformList, mapName, idWorkShop, relativeTargetFolder, profileList, success, errors, em);
        if (insertedMap == null) {
            String message = "The map with name " + mapName + " could not be added to the launcher";
            errors.append(message + "\n");
            return null;
        }

        success.append("The map with name " + mapName + " was added to all selected profiles and platforms" + "\n");
        return insertedMap;
    }

    private CustomMapMod createNewCustomMap(List<AbstractPlatform> platformList, String mapName, Long idWorkShop, String urlPhoto, List<Profile> profileList, StringBuffer success, StringBuffer errors, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);

        if (platformList.isEmpty() || StringUtils.isBlank(mapName) || idWorkShop == null) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, idWorkShop);
        return (CustomMapMod) customMapModService.createMap(platformList, customMap, profileList, success, errors);
    }

    public void downloadMapFromSteamCmd(List<String> platformNameList, CustomMapMod customMap, EntityManager em) {
        PlatformService platformService = new PlatformServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        StringBuffer success = new StringBuffer();
        StringBuffer errors = new StringBuffer();
        boolean alreadyDownloaded = false;
        List<AbstractPlatform> platformList = platformService.getPlatformListByNames(platformNameList, success, errors);

        for (AbstractPlatform platform: platformList) {
            try {
                List<PlatformProfileMap> ppmList = platformProfileMapService.listPlatformProfileMaps(platform, customMap);
                Optional<PlatformProfileMap> downloadedPpm = ppmList.stream()
                        .filter(ppm -> ppm.isDownloaded())
                        .findFirst();
                if (downloadedPpm.isPresent()) {
                    for (PlatformProfileMap ppm: ppmList){
                        ppm.setDownloaded(true);
                        platformProfileMapService.updateItem(ppm);
                    }
                    continue;
                }

                Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
                if (alreadyDownloaded || kf2Common.downloadMapFromSteamCmd(customMap)) {
                    alreadyDownloaded = true;

                    kf2Common.copyMapToCachePlatform(customMap);

                    for (PlatformProfileMap ppm: ppmList){
                        ppm.setDownloaded(true);
                        platformProfileMapService.updateItem(ppm);
                    }
                }
            } catch (Exception e) {
                logger.error("Error downloading map from SteamCmd", e);
                Utils.errorDialog(e.getMessage(), e);
            }
        }
    }

}
