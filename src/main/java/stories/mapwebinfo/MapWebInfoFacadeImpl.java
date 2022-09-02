package stories.mapwebinfo;

import daos.CustomMapModDao;
import daos.OfficialMapDao;
import daos.PlatformDao;
import daos.ProfileDao;
import dtos.CustomMapModDto;
import dtos.factories.MapDtoFactory;
import entities.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ProfileToDisplay;
import pojos.ProfileToDisplayFactory;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.*;
import stories.AbstractFacade;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapWebInfoFacadeImpl extends AbstractFacade implements MapWebInfoFacade {

    private static final Logger logger = LogManager.getLogger(MapWebInfoFacadeImpl.class);

    private final PropertyService propertyService;
    private final MapDtoFactory mapDtoFactory;
    private final OfficialMapServiceImpl officialMapService;
    private final CustomMapModServiceImpl customMapModService;
    private final ProfileToDisplayFactory profileToDisplayFactory;
    private final ProfileService profileService;
    private PlatformDao platformDao;

    public MapWebInfoFacadeImpl() {
        super();
        this.propertyService = new PropertyServiceImpl();
        this.mapDtoFactory = new MapDtoFactory();
        this.officialMapService = new OfficialMapServiceImpl();
        this.customMapModService = new CustomMapModServiceImpl();
        this.profileToDisplayFactory = new ProfileToDisplayFactory();
        this.profileService = new ProfileServiceImpl();
    }

    @Override
    public boolean isCorrectInstallationFolder(String installationFolder) {
        Kf2Common kf2Common = Kf2Factory.getInstance(
                Session.getInstance().getPlatform().getKey()
        );
        return kf2Common.isValid(installationFolder);
    }

    @Override
    public CustomMapModDto createNewCustomMapFromWorkshop(String platformName, Long idWorkShop, String mapName, String strUrlMapImage, List<String> profileNameList) throws Exception {
        List<Profile> profileList = profileNameList.stream().map(pn -> {
            try {
                return findProfileByCode(pn);
            } catch (SQLException e) {
                logger.error("Error finding a profile by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            return null;
        }
        String steamInstallationFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.steamInstallationFolder");;
        String epicInstallationFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.epicInstallationFolder");;
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");
        File localfile = null;
        if (StringUtils.isNotBlank(steamInstallationFolder)) {
            String absoluteTargetFolder = steamInstallationFolder + customMapLocalFolder;
            localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        }
        if (StringUtils.isNotBlank(epicInstallationFolder)) {
            String absoluteTargetFolder = epicInstallationFolder + customMapLocalFolder;
            localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        }
        if (localfile != null) {
            String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();
            CustomMapMod insertedMap = createNewCustomMap(platformName, mapName, idWorkShop, relativeTargetFolder, false, profileList);
            return insertedMap != null ? (CustomMapModDto) mapDtoFactory.newDto(insertedMap): null;
        }
        return null;
    }

    private CustomMapMod createNewCustomMap(String platformName, String mapName, Long idWorkShop, String urlPhoto, boolean downloaded, List<Profile> profileList) throws Exception {
        Optional<Platform> platformOptional = platformDao.getInstance().findByCode(platformName);
        if (!platformOptional.isPresent() || StringUtils.isBlank(mapName) || idWorkShop == null) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, idWorkShop, downloaded);

        return (CustomMapMod) customMapModService.createMap(platformOptional.get(), customMap, profileList, null);
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
    public void addProfilesToMap(String platformName, String mapName, List<String> profileNameList) throws SQLException {
        Optional<Platform> platformOptional = platformDao.getInstance().findByCode(platformName);
        if (!platformOptional.isPresent()) {
            return;
        }
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

        Optional officialMapOptional = officialMapService.findMapByCode(mapName);
        if (officialMapOptional.isPresent()) {
            officialMapService.addProfilesToMap(
                    platformOptional.get(),
                    (OfficialMap) officialMapOptional.get(),
                    profileList,
                    null);
        }

        Optional customMapModOptional = customMapModService.findMapByCode(mapName);
        if (customMapModOptional.isPresent()) {
            customMapModService.addProfilesToMap(
                    platformOptional.get(),
                    (CustomMapMod) customMapModOptional.get(),
                    profileList,
                    null);
        }
    }

    private boolean isMapInProfile(Long idWorkShop, Profile profile) {
        Optional<AbstractMap> mapOpt = profile.getMapList()
                .stream()
                .filter(m -> !m.isOfficial())
                .filter(m -> idWorkShop.equals(((CustomMapMod) m).getIdWorkShop()))
                .findFirst();
        return mapOpt.isPresent();
    }

    @Override
    public List<ProfileToDisplay> getProfilesWithoutMap(Long idWorkShop) throws SQLException {
        List<Profile> allProfiles = profileService.listAllProfiles();
        List<Profile> profilesWithoutMap = allProfiles.stream().filter(p -> !isMapInProfile(idWorkShop, p)).collect(Collectors.toList());
        return profileToDisplayFactory.newOnes(profilesWithoutMap);
    }
}
