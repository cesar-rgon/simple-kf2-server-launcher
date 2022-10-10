package stories.mapwebinfo;

import daos.*;
import dtos.CustomMapModDto;
import dtos.ProfileDto;
import dtos.factories.MapDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.*;
import entities.AbstractMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplayFactory;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;
import stories.AbstractFacade;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class MapWebInfoFacadeImpl extends AbstractFacade implements MapWebInfoFacade {

    private static final Logger logger = LogManager.getLogger(MapWebInfoFacadeImpl.class);

    private final PropertyService propertyService;
    private final MapDtoFactory mapDtoFactory;
    private final OfficialMapServiceImpl officialMapService;
    private final CustomMapModServiceImpl customMapModService;
    private final ProfileService profileService;
    private final ProfileDtoFactory profileDtoFactory;
    private final PlatformService platformService;
    private final PlatformProfileMapService platformProfileMapService;

    public MapWebInfoFacadeImpl() {
        super();
        this.propertyService = new PropertyServiceImpl();
        this.mapDtoFactory = new MapDtoFactory();
        this.officialMapService = new OfficialMapServiceImpl();
        this.customMapModService = new CustomMapModServiceImpl();
        this.profileService = new ProfileServiceImpl();
        this.profileDtoFactory = new ProfileDtoFactory();
        this.platformService = new PlatformServiceImpl();
        this.platformProfileMapService = new PlatformProfileMapServiceImpl();
    }

    @Override
    public boolean isCorrectInstallationFolder(String platformName) {
        try {
            if (EnumPlatform.STEAM.name().equals(platformName)) {
                Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
                if (!steamPlatformOptional.isPresent()) {
                    return false;
                }

                Kf2Common steamKf2Common = Kf2Factory.getInstance(
                        steamPlatformOptional.get()
                );

                return steamKf2Common.isValidInstallationFolder();
            }

            if (EnumPlatform.EPIC.name().equals(platformName)) {
                Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
                if (!epicPlatformOptional.isPresent()) {
                    return false;
                }

                Kf2Common epicKf2Common = Kf2Factory.getInstance(
                        epicPlatformOptional.get()
                );

                return epicKf2Common.isValidInstallationFolder();
            }

            return false;
        } catch (SQLException e) {
            logger.error("Error validating the installation folder", e);
            return false;
        }
    }

    private List<Profile> getProfileListByNames(List<String> profileNameList, StringBuffer success, StringBuffer errors) {
        List<Profile> profileList = profileNameList.stream().map(pn -> {
            try {
                return findProfileByCode(pn);
            } catch (Exception e) {
                String message = "Error finding the profile with name" + pn;
                errors.append(message + "\n");
                logger.error(message, e);
                throw new RuntimeException(message, e);
            }
        }).collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            String message = "No profiles were found";
            errors.append(message + "\n");
            throw new RuntimeException(message);
        }
        return profileList;
    }

    private List<AbstractPlatform> getPlatformListByNames(List<String> platformNameList, StringBuffer success, StringBuffer errors) {
        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            String message = "Error finding the platform with name" + pn;
            try {
                Optional<AbstractPlatform> steamPlatformOptional = platformService.findPlatformByName(pn);
                if (steamPlatformOptional.isPresent()) {
                    return steamPlatformOptional.get();
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

    @Override
    public CustomMapModDto createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, String mapName, String strUrlMapImage, List<String> profileNameList, StringBuffer success, StringBuffer errors) throws Exception {

        List<Profile> profileList = getProfileListByNames(profileNameList, success, errors);
        List<AbstractPlatform> platformList = getPlatformListByNames(platformNameList, success, errors);

        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

        File localfile = null;
        for (AbstractPlatform platform: platformList) {
            String absoluteTargetFolder = platform.getInstallationFolder() + customMapLocalFolder;
            localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        }

        if (localfile != null) {
            String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();
            CustomMapMod insertedMap = createNewCustomMap(platformList, mapName, idWorkShop, relativeTargetFolder, profileList, success, errors);
            if (insertedMap == null) {
                String message = "The map with name " + mapName + " could not be added to the launcher";
                errors.append(message + "\n");
                return null;
            }

            success.append("The map with name " + mapName + " was added to all selected profiles and platforms" + "\n");
            return (CustomMapModDto) mapDtoFactory.newDto(insertedMap);
        }

        String message = "The map with name " + mapName + " could not be added to all selected profiles and platforms";
        errors.append(message + "\n");
        return null;
    }

    private CustomMapMod createNewCustomMap(List<AbstractPlatform> platformList, String mapName, Long idWorkShop, String urlPhoto, List<Profile> profileList, StringBuffer success, StringBuffer errors) throws Exception {
        if (platformList.isEmpty() || StringUtils.isBlank(mapName) || idWorkShop == null) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, idWorkShop);

        return (CustomMapMod) customMapModService.createMap(platformList, customMap, profileList, success, errors);
    }

    @Override
    public CustomMapModDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException {
        Optional<CustomMapMod> mapOpt = customMapModService.findByIdWorkShop(idWorkShop);
        if (mapOpt.isPresent()) {
            return (CustomMapModDto) mapDtoFactory.newDto(mapOpt.get());
        }
        return null;
    }

    @Override
    public void addProfilesToMap(List<String> platformNameList, String mapName, String strUrlMapImage, List<String> profileNameList, StringBuffer success, StringBuffer errors) throws Exception {

        List<Profile> profileList = getProfileListByNames(profileNameList, success, errors);
        List<AbstractPlatform> platformList = getPlatformListByNames(platformNameList, success, errors);

        AbstractMap map = null;
        Optional officialMapOptional = officialMapService.findMapByCode(mapName);
        Optional customMapModOptional = customMapModService.findMapByCode(mapName);
        if (officialMapOptional.isPresent()) {
            map = (AbstractMap) officialMapOptional.get();
        } else {
            if (customMapModOptional.isPresent()) {
                map = (AbstractMap) customMapModOptional.get();
            }
        }

        List<PlatformProfileMap> platformProfileMapListToAdd = new ArrayList<PlatformProfileMap>();
        for (Profile profile: profileList) {
            for (AbstractPlatform platform: platformList) {
                platformProfileMapListToAdd.add(new PlatformProfileMap(platform, profile, map, map.getReleaseDate(), map.getUrlInfo(), map.getUrlPhoto(), officialMapOptional.isPresent()));
            }
        }

        if (officialMapOptional.isPresent()) {
            officialMapService.addPlatformProfileMapList(
                    platformProfileMapListToAdd,
                    success,
                    errors);
        }

        if (customMapModOptional.isPresent()) {
            customMapModService.addPlatformProfileMapList(
                    platformProfileMapListToAdd,
                    success,
                    errors);
        }

        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

        for (AbstractPlatform platform: platformList) {
            String absoluteTargetFolder = platform.getInstallationFolder() + customMapLocalFolder;
            Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        }
    }

    @Override
    public List<PlatformProfile> getPlatformProfileListWithoutMap(Long idWorkShop) throws SQLException {
        List<AbstractPlatform> validPlatformList = new ArrayList<AbstractPlatform>();
        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        if (steamPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(steamPlatformOptional.get()).isValidInstallationFolder()) {
                validPlatformList.add(steamPlatformOptional.get());
            }
        }
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        if (epicPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(epicPlatformOptional.get()).isValidInstallationFolder()) {
                validPlatformList.add(epicPlatformOptional.get());
            }
        }

        List<Profile> fullProfileList = profileService.listAllProfiles();
        List<PlatformProfile> platformProfileListWithoutMap = new ArrayList<PlatformProfile>();

        for (Profile profile: fullProfileList) {
            for (AbstractPlatform platform: validPlatformList) {
                Optional<PlatformProfileMap> platformProfileMapOptional = platformProfileMapService.listPlatformProfileMaps(platform, profile).stream().
                        filter(ppm -> {
                            try {
                                Optional<AbstractMap> customMapModOptional = customMapModService.findByCode(ppm.getMap().getCode());
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

    @Override
    public List<ProfileDto> getAllProfileList() throws SQLException {
        return profileDtoFactory.newDtos(profileService.listAllProfiles());
    }

    @Override
    public int countPlatformsProfilesForMap(String customMapName) {
        try {
            Optional<AbstractMap> mapOptional = customMapModService.findMapByCode(customMapName);
            if (mapOptional.isPresent()) {
                List<PlatformProfileMap> platformProfileMapList = platformProfileMapService.listPlatformProfileMaps(mapOptional.get());
                if (platformProfileMapList != null && !platformProfileMapList.isEmpty()) {
                    return platformProfileMapList.size();
                }
            }
            return 0;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return 0;
        }
    }
}
