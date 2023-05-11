package stories.importofficialmapsfromserver;

import dtos.AbstractMapDto;
import entities.*;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ImportMapResultToDisplay;
import pojos.PlatformProfileMapToImport;
import services.*;
import stories.maps.MapsController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ImportOfficialMapsFromServerFacadeImpl
        extends AbstractTransactionalFacade<ImportOfficialMapsFromServerModelContext, ImportOfficialMapsFromServerFacadeResult>
        implements ImportOfficialMapsFromServerFacade {

    private static final Logger logger = LogManager.getLogger(ImportOfficialMapsFromServerFacadeImpl.class);

    public ImportOfficialMapsFromServerFacadeImpl(ImportOfficialMapsFromServerModelContext importOfficialMapsFromServerModelContext) {
        super(importOfficialMapsFromServerModelContext, ImportOfficialMapsFromServerFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(ImportOfficialMapsFromServerModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ImportOfficialMapsFromServerFacadeResult internalExecute(ImportOfficialMapsFromServerModelContext facadeModelContext, EntityManager em) throws Exception {
        OfficialMapServiceImpl officialMapService = new OfficialMapServiceImpl(em);

        PlatformProfileMapToImport importedPpm = null;
        Optional<AbstractMap> mapOptional = Optional.empty();
        List<ImportMapResultToDisplay> importMapResultToDisplayList = new ArrayList<ImportMapResultToDisplay>();

        for (PlatformProfileMapToImport ppmToImport: facadeModelContext.getPpmToImportList()) {
            try {
                importedPpm = importOfficialMapFromServer(
                        ppmToImport,
                        facadeModelContext.getProfileName(),
                        em
                );

                mapOptional = officialMapService.findMapByCode(importedPpm.getMapToDisplay().getCommentary());
                if (!mapOptional.isPresent()) {
                    String errorMessage = "Can not find the map with name: " + importedPpm.getMapToDisplay().getCommentary();
                    logger.error(errorMessage);
                    throw new RuntimeException(errorMessage);
                }

                importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                        importedPpm.getProfileName(),
                        importedPpm.getPlatformName(),
                        mapOptional.get().getCode(),
                        true,
                        null,
                        new Date(),
                        StringUtils.EMPTY
                ));

            } catch (Exception e) {
                String errorMessage = "Error importing the official map with name: " + ppmToImport.getMapToDisplay().getCommentary() + " for platform " + ppmToImport.getPlatformName() + " and profile " + ppmToImport.getProfileName();
                logger.error(errorMessage, e);
                importMapResultToDisplayList.add(new ImportMapResultToDisplay(
                        ppmToImport.getProfileName(),
                        ppmToImport.getPlatformName(),
                        ppmToImport.getMapToDisplay().getCommentary(),
                        true,
                        null,
                        null,
                        errorMessage
                ));
                continue;
            }
        }

        return new ImportOfficialMapsFromServerFacadeResult(
                importMapResultToDisplayList
        );
    }


    private PlatformProfileMapToImport importOfficialMapFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);

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
        String officialMapName = ppmToImport.getMapToDisplay().getCommentary();
        List<String> selectedProfileNameList = new ArrayList<String>();
        selectedProfileNameList.add(selectedProfileName);

        Optional<AbstractMap> mapInDataBase = findMapByName(officialMapName, em);
        if (!mapInDataBase.isPresent()) {

            OfficialMap insertedMap = insertOfficialMap(
                    platformNameList,
                    officialMapName,
                    selectedProfileNameList,
                    em
            );

            if (insertedMap == null) {
                String errorMessage = "Error creating the relation between the map " + officialMapName + ", the profile " + ppmToImport.getProfileName() + " and the platform " + ppmToImport.getPlatformName();
                throw new RuntimeException(errorMessage);
            }

        } else {
            OfficialMap officialMap = (OfficialMap) mapInDataBase.get();

            List<PlatformProfileMap> platformProfileMapListToAdd = new ArrayList<PlatformProfileMap>();
            platformProfileMapListToAdd.add(
                    new PlatformProfileMap(platformOptional.get(), profileOptional.get(), officialMap, officialMap.getReleaseDate(), officialMap.getUrlInfo(), officialMap.getUrlPhoto(), true)
            );

            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();
            customMapModService.addPlatformProfileMapList(platformProfileMapListToAdd, success, errors);

            if (StringUtils.isNotBlank(errors)) {
                throw new RuntimeException(errors.toString());
            }
        }

        return ppmToImport;
    }

    private Optional<AbstractMap> findMapByName(String mapName, EntityManager em) throws Exception {
        OfficialMapServiceImpl officialMapService = new OfficialMapServiceImpl(em);
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);

        Optional<AbstractMap> officialMapOptional = officialMapService.findMapByCode(mapName);
        if (officialMapOptional.isPresent()) {
            return officialMapOptional;
        }
        Optional<AbstractMap> customMapModOptional = customMapModService.findMapByCode(mapName);
        return customMapModOptional;
    }

    private OfficialMap insertOfficialMap(List<String> platformNameList, String mapName, List<String> profileNameList, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);
        OfficialMapServiceImpl officialMapService = new OfficialMapServiceImpl(em);

        List<Profile> profileList = profileNameList.stream().map(pn -> {
            try {
                Optional<Profile> profileOpt = profileService.findProfileByCode(pn);
                return profileOpt.orElse(null);
            } catch (Exception e) {
                logger.error("Error finding a profile by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());

        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            try {
                Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(pn);
                return platformOptional.orElse(null);
            } catch (Exception e) {
                logger.error("Error finding a platform by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());

        StringBuffer success = new StringBuffer();
        StringBuffer errors = new StringBuffer();

        OfficialMap newOfficialMap = new OfficialMap(mapName, "", "/KFGame/Web/images/maps/" + mapName + ".jpg", null);
        OfficialMap insertedMap = (OfficialMap) officialMapService.createMap(platformList, newOfficialMap, profileList, success, errors);

        if (StringUtils.isNotBlank(errors)) {
            throw new RuntimeException(errors.toString());
        }

        return insertedMap;
    }

}
