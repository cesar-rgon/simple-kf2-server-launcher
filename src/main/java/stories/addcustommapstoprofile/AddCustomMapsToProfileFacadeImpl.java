package stories.addcustommapstoprofile;

import dtos.PlatformProfileMapDto;
import dtos.factories.PlatformProfileMapDtoFactory;
import entities.*;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.MapToDisplay;
import pojos.PlatformProfile;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        PlatformService platformService = new PlatformServiceImpl(em);
        CustomMapModServiceImpl customMapService = new CustomMapModServiceImpl(em);
        PlatformProfileMapDtoFactory platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory(em);
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);
        PropertyService propertyService = new PropertyServiceImpl();
        StringBuffer success = new StringBuffer();
        StringBuffer errors = new StringBuffer();

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        List<AbstractPlatform> platformList = platformService.getPlatformListByNames(facadeModelContext.getPlatformNameList(), success, errors);

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

                        CustomMapMod customMap = customMapService.createNewCustomMapFromWorkshop(
                                facadeModelContext.getPlatformNameList(), idWorkShop, profileNameList, mapName, strUrlMapImage, success, errors
                        );
                        if (customMap != null) {
                            customMapService.downloadMapFromSteamCmd(facadeModelContext.getPlatformNameList(), customMap, em);
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
                        customMapService.downloadMapFromSteamCmd(facadeModelContext.getPlatformNameList(), mapModInDataBase.get(), em);
                        List<AbstractPlatform> platformListForProfileWithoutMap = getPlatformProfileListWithoutMap(idWorkShop, em).stream().map(PlatformProfile::getPlatform).collect(Collectors.toList());
                        List<PlatformProfileMap> ppmList = new ArrayList<PlatformProfileMap>();
                        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

                        for (AbstractPlatform platform: platformList) {
                            if (platformListForProfileWithoutMap.contains(platform)) {
                                PlatformProfileMap ppm = new PlatformProfileMap(platform, profileOpt.get(), mapModInDataBase.get(), mapModInDataBase.get().getReleaseDate(), mapModInDataBase.get().getUrlInfo(), mapModInDataBase.get().getUrlPhoto(), false);
                                ppmList.add(ppm);

                                String absoluteTargetFolder = ppm.getPlatform().getInstallationFolder() + customMapLocalFolder;
                                Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, Long.toString(mapModInDataBase.get().getIdWorkShop()));
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
