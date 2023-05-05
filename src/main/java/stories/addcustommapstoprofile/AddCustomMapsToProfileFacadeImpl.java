package stories.addcustommapstoprofile;

import dtos.PlatformProfileMapDto;
import dtos.factories.PlatformProfileMapDtoFactory;
import entities.*;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.PlatformProfile;
import services.*;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddCustomMapsToProfileFacadeImpl
        extends AbstractTransactionalFacade<AddCustomMapsToProfileModelContext, AddCustomMapsToProfileFacadeResult>
        implements AddCustomMapsToProfileFacade {

    private static final Logger logger = LogManager.getLogger(AddCustomMapsToProfileFacadeImpl.class);

    public AddCustomMapsToProfileFacadeImpl(AddCustomMapsToProfileModelContext addCustomMapsToProfileModelContext) {
        super(addCustomMapsToProfileModelContext, AddCustomMapsToProfileFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(AddCustomMapsToProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        Optional<Profile> profileOptional = profileService.findProfileByCode(facadeModelContext.getProfileName());
        return profileOptional.isPresent();
    }

    @Override
    protected AddCustomMapsToProfileFacadeResult internalExecute(AddCustomMapsToProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        CustomMapModServiceImpl customMapService = new CustomMapModServiceImpl(em);
        PlatformProfileMapDtoFactory platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory(em);
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);
        PropertyService propertyService = new PropertyServiceImpl();
        StringBuffer success = new StringBuffer();
        StringBuffer errors = new StringBuffer();

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        List<AbstractPlatform> platformList = getPlatformListByNames(facadeModelContext.getPlatformNameList(), success, errors, em);

        List<PlatformProfileMapDto> mapAddedList = new ArrayList<PlatformProfileMapDto>();
        if (StringUtils.isNotBlank(facadeModelContext.getMapNameList())) {
            String[] idUrlWorkShopArray = facadeModelContext.getMapNameList().replaceAll(" ", "").split(",");

            for (int i = 0; i < idUrlWorkShopArray.length; i++) {
                try {
                    Long idWorkShop = null;
                    if (idUrlWorkShopArray[i].contains("http")) {
                        String[] array = idUrlWorkShopArray[i].split("=");
                        idWorkShop = Long.parseLong(array[1]);
                    } else {
                        idWorkShop = Long.parseLong(idUrlWorkShopArray[i]);
                    }

                    String[] result = getMapNameAndUrlImage(idWorkShop);
                    String mapName = result[0];
                    String strUrlMapImage = result[1];

                    Optional<CustomMapMod> mapModInDataBase = customMapService.findByIdWorkShop(idWorkShop);
                    if (!mapModInDataBase.isPresent()) {
                        // The map is not in dabatabase, create a new map for actual profile
                        List<String> profileNameList = new ArrayList<String>();
                        profileNameList.add(facadeModelContext.getProfileName());

                        CustomMapMod customMap = createNewCustomMapFromWorkshop(
                                facadeModelContext.getPlatformNameList(), idWorkShop, profileNameList, mapName, strUrlMapImage, success, errors, em
                        );
                        if (customMap != null) {
                            if (facadeModelContext.getProfileName().equalsIgnoreCase(facadeModelContext.getActualSelectedProfile())) {
                                Optional<Profile> profileOptional = profileService.findByCode(facadeModelContext.getProfileName());
                                if (!platformList.isEmpty() && profileOptional.isPresent()) {
                                    for (AbstractPlatform platform: platformList) {
                                        PlatformProfileMap platformProfileMap = new PlatformProfileMap(platform, profileOptional.get(), customMap, customMap.getReleaseDate(), customMap.getUrlInfo(), customMap.getUrlPhoto(), false);
                                        mapAddedList.add(platformProfileMapDtoFactory.newDto(platformProfileMap));
                                    }
                                }
                            }
                        } else {
                            throw new RuntimeException("Error adding map/mod with name with idWorkshop " + idWorkShop);
                        }
                    } else {
                        List<AbstractPlatform> platformListForProfileWithoutMap = getPlatformProfileListWithoutMap(idWorkShop, em).stream().map(PlatformProfile::getPlatform).collect(Collectors.toList());
                        List<PlatformProfileMap> ppmList = new ArrayList<PlatformProfileMap>();
                        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

                        for (AbstractPlatform platform: platformList) {
                            if (platformListForProfileWithoutMap.contains(platform)) {
                                PlatformProfileMap ppm = new PlatformProfileMap(platform, profileOpt.get(), mapModInDataBase.get(), mapModInDataBase.get().getReleaseDate(), mapModInDataBase.get().getUrlInfo(), mapModInDataBase.get().getUrlPhoto(), false);
                                ppmList.add(ppm);

                                String absoluteTargetFolder = ppm.getPlatform().getInstallationFolder() + customMapLocalFolder;
                                Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapModInDataBase.get().getCode());
                            } else {
                                String errorMessage = "Error creating the relation between the map " + mapName + ", the profile " + profileOpt.get().getCode() + " and the platform " + platform.getDescription();
                                errors.append(errorMessage + "\n");
                            }
                        }

                        customMapModService.addPlatformProfileMapList(ppmList, success, errors);
                        mapAddedList.addAll(platformProfileMapDtoFactory.newDtos(ppmList));
                    }

                } catch (Exception e) {
                    String message = "Error adding map/mod to the launcher";
                    logger.error(message, e);
                }
            }
        }

        return new AddCustomMapsToProfileFacadeResult(
                mapAddedList,
                success,
                errors
        );
    }

    private List<AbstractPlatform> getPlatformListByNames(List<String> platformNameList, StringBuffer success, StringBuffer errors, EntityManager em) {
        PlatformService platformService = new PlatformServiceImpl(em);

        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            String message = "Error finding the platform with name" + pn;
            try {
                Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(pn);
                if (platformOptional.isPresent()) {
                    return platformOptional.get();
                }
                errors.append(message + "\n");
                return null;

            } catch (SQLException e) {
                errors.append(message + "\n");
                logger.error(message, e);
                throw new RuntimeException(message, e);
            }
        }).collect(Collectors.toList());
        if (platformList == null || platformList.isEmpty()) {
            String message = "No platforms were found";
            errors.append(message + "\n");
            throw new RuntimeException(message);
        }

        return platformList;
    }

    private String[] getMapNameAndUrlImage(Long idWorkShop) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();

        URL urlWorkShop = null;
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        urlWorkShop = new URL(baseUrlWorkshop + idWorkShop);

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
        return new String[]{mapName, strUrlMapImage};
    }

    private CustomMapMod createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, List<String> profileNameList, String mapName, String strUrlMapImage, StringBuffer success, StringBuffer errors, EntityManager em) throws Exception {
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
            } catch (SQLException e) {
                logger.error("Error finding a platform by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());
        if (platformList == null || platformList.isEmpty()) {
            return null;
        }

        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

        File localfile = new File(StringUtils.EMPTY);
        for (AbstractPlatform platform: platformList) {
            String absoluteTargetFolder = platform.getInstallationFolder() + customMapLocalFolder;
            localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        }
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

        return createNewCustomMap(platformList, mapName, idWorkShop, relativeTargetFolder, profileList, success, errors, em);
    }

    private CustomMapMod createNewCustomMap(List<AbstractPlatform> platformList, String mapName, Long idWorkShop, String urlPhoto, List<Profile> profileList, StringBuffer success, StringBuffer errors, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);

        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, idWorkShop);
        return (CustomMapMod) customMapModService.createMap(platformList, customMap, profileList, success, errors);
    }

    private List<PlatformProfile> getPlatformProfileListWithoutMap(Long idWorkShop, EntityManager em) throws SQLException {
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        CustomMapModServiceImpl customMapService = new CustomMapModServiceImpl(em);

        List<AbstractPlatform> fullPlatformList = platformService.listAllPlatforms();
        List<Profile> fullProfileList = profileService.listAllProfiles();
        List<PlatformProfile> platformProfileListWithoutMap = new ArrayList<PlatformProfile>();

        for (Profile profile: fullProfileList) {
            for (AbstractPlatform platform: fullPlatformList) {
                Optional<PlatformProfileMap> platformProfileMapOptional = platformProfileMapService.listPlatformProfileMaps(platform, profile).stream().
                        filter(ppm -> {
                            try {
                                Optional<AbstractMap> customMapModOptional = customMapService.findMapByCode(ppm.getMap().getCode());
                                if (customMapModOptional.isPresent()) {
                                    CustomMapMod customMap = (CustomMapMod) Hibernate.unproxy(customMapModOptional.get());
                                    return idWorkShop.equals(customMap.getIdWorkShop());
                                }
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                            return false;
                        }).findFirst();

                if (!platformProfileMapOptional.isPresent()) {
                    platformProfileListWithoutMap.add(new PlatformProfile(platform, profile));
                }
            }
        }

        return platformProfileListWithoutMap;
    }
}
