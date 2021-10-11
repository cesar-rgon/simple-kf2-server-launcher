package stories.mapsedition;

import daos.MapDao;
import daos.ProfileDao;
import dtos.MapDto;
import dtos.ProfileDto;
import dtos.factories.MapDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Map;
import entities.Profile;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ProfileToDisplay;
import services.*;
import stories.AbstractFacade;
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

public class MapsEditionFacadeImpl extends AbstractFacade implements MapsEditionFacade {

    private static final Logger logger = LogManager.getLogger(MapsEditionFacadeImpl.class);

    private final MapDtoFactory mapDtoFactory;
    private final PropertyService propertyService;
    private final ProfileDtoFactory profileDtoFactory;
    private final MapService mapService;

    public MapsEditionFacadeImpl() {
        super();
        this.mapDtoFactory = new MapDtoFactory();
        this.propertyService = new PropertyServiceImpl();
        this.profileDtoFactory = new ProfileDtoFactory();
        this.mapService = new MapServiceImpl();
    }

    private Map createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto, boolean downloaded, Boolean isMod, List<Profile> profileList) throws Exception {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        Map customMap = new Map(mapName, false, urlInfo, idWorkShop, urlPhoto, downloaded, isMod, profileList);
        return mapService.createMap(customMap);
    }

    @Override
    public MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String installationFolder, boolean downloaded, Boolean isMod, List<String> profileNameList) throws Exception {
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
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

        Map newMap = createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, downloaded, isMod, profileList);
        return mapDtoFactory.newDto(newMap);
    }

    @Override
    public MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder, boolean downloaded, Boolean isMod, List<String> profileNameList) throws Exception {
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
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        File localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

        Map newMap = createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, downloaded, isMod, profileList);
        return mapDtoFactory.newDto(newMap);
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
    public MapDto insertOfficialMap(String mapName, List<String> profileNameList) throws Exception {
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
        Map newOfficialMap = new Map(mapName, true, null, null, "/KFGame/Web/images/maps/" + mapName + ".jpg", true, false, profileList);
        Map insertedMap = mapService.createMap(newOfficialMap);
        return insertedMap != null ? mapDtoFactory.newDto(insertedMap): null;
    }

    @Override
    public MapDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException {
        Optional<Map> mapOpt = MapDao.getInstance().findByIdWorkShop(idWorkShop);
        if (mapOpt.isPresent()) {
            return mapDtoFactory.newDto(mapOpt.get());
        }
        return null;
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = ProfileDao.getInstance().listAll();
        return profileDtoFactory.newDtos(profiles);
    }

    @Override
    public List<MapDto> getMapsFromProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByCode(profileName);
        if (profileOpt.isPresent()) {
            return mapDtoFactory.newDtos(profileOpt.get().getMapList());
        }
        return new ArrayList<MapDto>();
    }

    @Override
    public Boolean addProfilesToMap(String mapName, List<String> profileNameList) throws SQLException {
        Optional<Map> mapOptional = MapDao.getInstance().findByCode(mapName);
        if (!mapOptional.isPresent()) {
            return false;
        }
        List<Profile> profileList = profileNameList.stream().map(pn -> {
                    try {
                        return findProfileByCode(pn);
                    } catch (SQLException e) {
                        logger.error("Error finding a profile by name " + pn, e);
                        return null;
                    }
                })
                .filter(profile -> !profile.getMapList().contains(mapOptional.get()))
                .collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            return null;
        }
        return mapService.addProfilesToMap(mapOptional.get(), profileList);
    }

    @Override
    public MapDto deleteMapFromProfile(String mapName, String profileName, String installationFolder) throws Exception {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByCode(profileName);
        if (profileOpt.isPresent()) {
            Optional<Map> mapOpt = profileOpt.get().getMapList().stream().filter(m -> m.getCode().equalsIgnoreCase(mapName)).findFirst();
            if (mapOpt.isPresent()) {
                Map deletedMap = mapService.deleteMap(mapOpt.get(), profileOpt.get(), installationFolder);
                return mapDtoFactory.newDto(deletedMap);
            }
        }
        return null;
    }

    @Override
    public MapDto findMapByName(String mapName) throws SQLException {
        Optional<Map> mapOpt = MapDao.getInstance().findByCode(mapName);
        if (mapOpt.isPresent()) {
            return mapDtoFactory.newDto(mapOpt.get());
        }
        return null;
    }

    @Override
    public void unselectProfileMap(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setMap(null);
            ProfileDao.getInstance().update(profileOpt.get());
        }
    }

    @Override
    public List<String> selectProfilesToImport(String defaultSelectedProfileName) throws Exception {
        List<Profile> allProfiles = ProfileDao.getInstance().listAll();
        if (allProfiles == null || allProfiles.isEmpty()) {
            return new ArrayList<String>();
        }
        List<ProfileToDisplay> selectedProfiles = mapService.selectProfilesToImport(allProfiles, defaultSelectedProfileName);
        return selectedProfiles.stream().map(p -> p.getProfileName()).collect(Collectors.toList());
    }
}
