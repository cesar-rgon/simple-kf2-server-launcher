package stories.mapwebinfo;

import daos.CustomMapModDao;
import daos.OfficialMapDao;
import daos.ProfileDao;
import dtos.CustomMapModDto;
import dtos.factories.MapDtoFactory;
import entities.AbstractMap;
import entities.CustomMapMod;
import entities.OfficialMap;
import entities.Profile;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ProfileToDisplay;
import pojos.ProfileToDisplayFactory;
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
    private final ProfileToDisplayFactory profileToDisplayFactory;
    private final ProfileService profileService;

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
    public CustomMapModDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String strUrlMapImage, String installationFolder, List<String> profileNameList) throws Exception {
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
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();
        CustomMapMod insertedMap = createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, false, null, profileList);
        return insertedMap != null ? (CustomMapModDto) mapDtoFactory.newDto(insertedMap): null;
    }

    private CustomMapMod createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto, boolean downloaded, Boolean mod, List<Profile> profileList) throws Exception {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, profileList, idWorkShop, downloaded);
        return (CustomMapMod) customMapModService.createMap(customMap);
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
    public boolean addProfilesToMap(String mapName, List<String> profileNameList) throws SQLException {

        List<Profile> profileList = profileNameList.stream().map(pn -> {
            try {
                return findProfileByCode(pn);
            } catch (SQLException e) {
                logger.error("Error finding a profile by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            return false;
        }

        Optional<OfficialMap> officialMapOptional = OfficialMapDao.getInstance().findByCode(mapName);
        if (officialMapOptional.isPresent()) {
            return officialMapService.addProfilesToMap(officialMapOptional.get(), profileList);
        }

        Optional<CustomMapMod> customMapModOptional = CustomMapModDao.getInstance().findByCode(mapName);
        if (customMapModOptional.isPresent()) {
            return customMapModService.addProfilesToMap(customMapModOptional.get(),profileList);
        }

        return false;
    }

    private boolean isMapInProfile(Long idWorkShop, Profile profile) {
        Optional<AbstractMap> mapOpt = profile.getMapList().stream().filter(m -> idWorkShop.equals(((CustomMapMod) m).getIdWorkShop())).findFirst();
        return mapOpt.isPresent();
    }

    @Override
    public List<ProfileToDisplay> getProfilesWithoutMap(Long idWorkShop) throws SQLException {
        List<Profile> allProfiles = profileService.listAllProfiles();
        List<Profile> profilesWithoutMap = allProfiles.stream().filter(p -> !isMapInProfile(idWorkShop, p)).collect(Collectors.toList());
        return profileToDisplayFactory.newOnes(profilesWithoutMap);
    }
}
