package stories.importcustommapsfromserver;

import entities.*;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ImportMapResultToDisplay;
import pojos.PlatformProfileMapToImport;
import services.*;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ImportCustomMapsFromServerFacadeImpl
        extends AbstractTransactionalFacade<ImportCustomMapsFromServerModelContext, ImportCustomMapsFromServerFacadeResult>
        implements ImportCustomMapsFromServerFacade {

    private static final Logger logger = LogManager.getLogger(ImportCustomMapsFromServerFacadeImpl.class);

    public ImportCustomMapsFromServerFacadeImpl(ImportCustomMapsFromServerModelContext importCustomMapsFromServerModelContext) {
        super(importCustomMapsFromServerModelContext, ImportCustomMapsFromServerFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(ImportCustomMapsFromServerModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ImportCustomMapsFromServerFacadeResult internalExecute(ImportCustomMapsFromServerModelContext facadeModelContext, EntityManager em) throws Exception {
        CustomMapModServiceImpl customMapService = new CustomMapModServiceImpl(em);

        PlatformProfileMapToImport importedPpm = null;
        Optional<AbstractMap> mapOptional = Optional.empty();
        List<ImportMapResultToDisplay> importMapResultToDisplayList = new ArrayList<ImportMapResultToDisplay>();

        for (PlatformProfileMapToImport ppmToImport: facadeModelContext.getPpmToImportList()) {
            try {
                importedPpm = importCustomMapModFromServer(
                        ppmToImport,
                        facadeModelContext.getProfileName(),
                        em
                );

                mapOptional = customMapService.findByIdWorkShop(importedPpm.getMapToDisplay().getIdWorkShop());
                if (!mapOptional.isPresent()) {
                    String errorMessage = "Can not find the map with idWorkShop: " + importedPpm.getMapToDisplay().getIdWorkShop();
                    logger.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                }

                importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                        importedPpm.getProfileName(),
                        importedPpm.getPlatformName(),
                        mapOptional.get().getCode(),
                        false,
                        importedPpm.getMapToDisplay().getIdWorkShop(),
                        new Date(),
                        StringUtils.EMPTY
                ));

            } catch (Exception e) {
                String errorMessage = "Error importing the custom map/mod with idWorkShop: " + ppmToImport.getMapToDisplay().getIdWorkShop() + " for platform " + ppmToImport.getPlatformName() + " and profile " + ppmToImport.getProfileName();
                logger.error(errorMessage, e);
                importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                        ppmToImport.getProfileName(),
                        ppmToImport.getPlatformName(),
                        ppmToImport.getMapToDisplay().getCommentary(),
                        false,
                        ppmToImport.getMapToDisplay().getIdWorkShop(),
                        null,
                        errorMessage
                ));
                continue;
            }
        }

        return new ImportCustomMapsFromServerFacadeResult(
                importMapResultToDisplayList
        );
    }

    private PlatformProfileMapToImport importCustomMapModFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);

        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(ppmToImport.getPlatformName());
        if (!platformOptional.isPresent()) {
            String message = "No platform was found: " + ppmToImport.getPlatformName();
            throw new RuntimeException(message);
        }

        Optional<Profile> profileOptional = profileService.findProfileByCode(ppmToImport.getProfileName());
        if (!profileOptional.isPresent()) {
            String message = "No profile was found: " + ppmToImport.getProfileName();
            throw new RuntimeException(message);
        }

        List<String> platformNameList = new ArrayList<String>();
        platformNameList.add(ppmToImport.getPlatformName());
        List<String> selectedProfileNameList = new ArrayList<String>();
        selectedProfileNameList.add(selectedProfileName);


        Optional mapInDataBase = customMapModService.findByIdWorkShop(ppmToImport.getMapToDisplay().getIdWorkShop());
        if (!mapInDataBase.isPresent()) {
            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();

            CustomMapMod customMap = createNewCustomMapFromWorkshop(
                    platformNameList,
                    ppmToImport.getMapToDisplay().getIdWorkShop(),
                    ppmToImport.getMapToDisplay().isMap(),
                    ppmToImport.getMapToDisplay().getCommentary(),
                    selectedProfileNameList,
                    success,
                    errors,
                    em
            );
            if (customMap == null) {
                throw new RuntimeException(errors.toString());
            }

        } else {
            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();
            CustomMapMod customMap = (CustomMapMod) mapInDataBase.get();

            Optional<PlatformProfileMap> ppmOptional = platformProfileMapService.findPlatformProfileMapByNames(platformOptional.get().getCode(), profileOptional.get().getName(), customMap.getCode());
            if (ppmOptional.isPresent()) {
                String errorMessage = "The relation between the map " + customMap.getCode() + ", the profile " + profileOptional.get().getName() + " and the platform " + platformOptional.get().getCode() + " already exits";
                errors.append(errorMessage + "\n");
                logger.warn(errorMessage);
            } else {
                List<PlatformProfileMap> platformProfileMapListToAdd = new ArrayList<PlatformProfileMap>();
                boolean isDownloadedMap = customMapModService.isDownloadedMap(platformOptional.get(), customMap);
                platformProfileMapListToAdd.add(
                        new PlatformProfileMap(platformOptional.get(), profileOptional.get(), customMap, customMap.getReleaseDate(), customMap.getUrlInfo(), customMap.getUrlPhoto(), isDownloadedMap, isDownloadedMap)
                );

                customMapModService.addPlatformProfileMapList(platformProfileMapListToAdd, success, errors);
            }

            if (StringUtils.isNotBlank(errors)) {
                throw new RuntimeException(errors.toString());
            }
        }

        return ppmToImport;
    }

    private CustomMapMod createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, Boolean isMap, String mapName, List<String> profileNameList, StringBuffer success, StringBuffer errors, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);
        PropertyService propertyService = new PropertyServiceImpl();

        List<Profile> profileList = profileNameList.stream().map(pn -> {
            try {
                Optional<Profile> profileOpt = profileService.findProfileByCode(pn);
                return profileOpt.orElse(null);
            } catch (Exception e) {
                logger.error("Error finding a profile by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            return null;
        }
        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            try {
                Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(pn);
                return platformOptional.orElse(null);
            } catch (Exception e) {
                logger.error("Error finding a platform by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());

        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
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
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

        File localfile = new File(StringUtils.EMPTY);
        for (AbstractPlatform platform: platformList) {
            String absoluteTargetFolder = platform.getInstallationFolder() + customMapLocalFolder;
            localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, Long.toString(idWorkShop));
        }
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

        return createNewCustomMap(platformList, mapName, idWorkShop, isMap, relativeTargetFolder, profileList, success, errors, em);
    }

    private CustomMapMod createNewCustomMap(List<AbstractPlatform> platformList, String mapName, Long idWorkShop, Boolean isMap, String urlPhoto, List<Profile> profileList, StringBuffer success, StringBuffer errors, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);

        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;

        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, idWorkShop, isMap);
        return (CustomMapMod) customMapModService.createMap(platformList, customMap, profileList, success, errors);
    }

}
