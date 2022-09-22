package stories.mapwebinfo;

import daos.CustomMapModDao;
import daos.EpicPlatformDao;
import daos.PlatformProfileMapDao;
import daos.SteamPlatformDao;
import dtos.CustomMapModDto;
import dtos.factories.MapDtoFactory;
import entities.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.PlatformProfileToDisplay;
import pojos.PlatformProfileToDisplayFactory;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.*;
import stories.AbstractFacade;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapWebInfoFacadeImpl extends AbstractFacade implements MapWebInfoFacade {

    private static final Logger logger = LogManager.getLogger(MapWebInfoFacadeImpl.class);

    private final PropertyService propertyService;
    private final MapDtoFactory mapDtoFactory;
    private final OfficialMapServiceImpl officialMapService;
    private final CustomMapModServiceImpl customMapModService;
    private final PlatformProfileToDisplayFactory platformProfileToDisplayFactory;
    private final ProfileService profileService;

    public MapWebInfoFacadeImpl() {
        super();
        this.propertyService = new PropertyServiceImpl();
        this.mapDtoFactory = new MapDtoFactory();
        this.officialMapService = new OfficialMapServiceImpl();
        this.customMapModService = new CustomMapModServiceImpl();
        this.platformProfileToDisplayFactory = new PlatformProfileToDisplayFactory();
        this.profileService = new ProfileServiceImpl();
    }

    @Override
    public boolean isCorrectInstallationFolder(String platformName) {
        try {
            if (EnumPlatform.STEAM.name().equals(platformName)) {
                Optional<SteamPlatform> steamPlatformOptional = SteamPlatformDao.getInstance().findByCode(EnumPlatform.STEAM.name());
                if (!steamPlatformOptional.isPresent()) {
                    return false;
                }

                Kf2Common steamKf2Common = Kf2Factory.getInstance(
                        steamPlatformOptional.get().getCode()
                );

                return steamKf2Common.isValid(steamPlatformOptional.get().getInstallationFolder());
            }

            if (EnumPlatform.EPIC.name().equals(platformName)) {
                Optional<EpicPlatform> epicPlatformOptional = EpicPlatformDao.getInstance().findByCode(EnumPlatform.EPIC.name());
                if (!epicPlatformOptional.isPresent()) {
                    return false;
                }

                Kf2Common epicKf2Common = Kf2Factory.getInstance(
                        epicPlatformOptional.get().getCode()
                );

                return epicKf2Common.isValid(epicPlatformOptional.get().getInstallationFolder());
            }

            return false;
        } catch (SQLException e) {
            logger.error("Error validating the installation folder", e);
            return false;
        }
    }

    @Override
    public CustomMapModDto createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, String mapName, String strUrlMapImage, List<String> profileNameList) throws Exception {
        List<Profile> profileList = profileNameList.stream().map(pn -> {
            try {
                return findProfileByCode(pn);
            } catch (SQLException e) {
                logger.error("Error finding a profile by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            throw new RuntimeException("No profiles were found.");
        }
        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            try {
                return findPlatformByCode(pn);
            } catch (SQLException e) {
                logger.error("Error finding a platform by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());
        if (platformList == null || platformList.isEmpty()) {
            throw new RuntimeException("No platforms were found.");
        }

        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

        File localfile = null;
        for (AbstractPlatform platform: platformList) {
            String absoluteTargetFolder = platform.getInstallationFolder() + customMapLocalFolder;
            localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        }

        if (localfile != null) {
            String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();
            CustomMapMod insertedMap = createNewCustomMap(platformList, mapName, idWorkShop, relativeTargetFolder, profileList);
            return insertedMap != null ? (CustomMapModDto) mapDtoFactory.newDto(insertedMap): null;
        }
        return null;
    }

    private CustomMapMod createNewCustomMap(List<AbstractPlatform> platformList, String mapName, Long idWorkShop, String urlPhoto, List<Profile> profileList) throws Exception {
        if (platformList.isEmpty() || StringUtils.isBlank(mapName) || idWorkShop == null) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, idWorkShop);

        return (CustomMapMod) customMapModService.createMap(platformList, customMap, profileList, null);
    }

    @Override
    public CustomMapModDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException {
        Optional<CustomMapMod> mapOpt = CustomMapModDao.getInstance().findByIdWorkShop(idWorkShop);
        if (mapOpt.isPresent()) {
            return (CustomMapModDto) mapDtoFactory.newDto(mapOpt.get());
        }
        return null;
    }

    @Override
    public void addProfilesToMap(List<String> platformNameList, String mapName, List<String> profileNameList) throws SQLException {
        List<Profile> profileList = profileNameList.stream().map(pn -> {
            try {
                return findProfileByCode(pn);
            } catch (SQLException e) {
                logger.error("Error finding a profile by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            return;
        }

        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            try {
                return findPlatformByCode(pn);
            } catch (SQLException e) {
                logger.error("Error finding a platform by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());
        if (platformList == null || platformList.isEmpty()) {
            return;
        }

        /*
        Optional officialMapOptional = officialMapService.findMapByCode(mapName);
        if (officialMapOptional.isPresent()) {
            officialMapService.addPlatformProfileMapList(
                    platformList,
                    (OfficialMap) officialMapOptional.get(),
                    profileList,
                    null);
        }

        Optional customMapModOptional = customMapModService.findMapByCode(mapName);
        if (customMapModOptional.isPresent()) {
            customMapModService.addPlatformProfileMapList(
                    platformList,
                    (CustomMapMod) customMapModOptional.get(),
                    profileList,
                    null);
        }
         */
    }

    @Override
    public List<PlatformProfileToDisplay> getPlatformProfilesWithoutMap(Long idWorkShop) throws SQLException {

        List<PlatformProfileMap> platformProfileMapList = PlatformProfileMapDao.getInstance().listPlatformProfileMaps();

        List<PlatformProfileMap> platformProfileMapListWithIdWorkshop = platformProfileMapList.stream().
                filter(ppm -> !ppm.getMap().isOfficial()).
                filter(ppm -> idWorkShop.equals(((CustomMapMod) ppm.getMap()).getIdWorkShop())).
                collect(Collectors.toList());

        platformProfileMapList.removeAll(platformProfileMapListWithIdWorkshop);
        List<PlatformProfileMap> platformProfileMapListWithoutIdWorkshop = platformProfileMapList;

        return platformProfileToDisplayFactory.newOnes(platformProfileMapListWithoutIdWorkshop);
    }
}
