package stories.mapsedition;

import daos.CustomMapModDao;
import daos.ProfileDao;
import dtos.AbstractMapDto;
import dtos.CustomMapModDto;
import dtos.OfficialMapDto;
import dtos.ProfileDto;
import dtos.factories.MapDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.*;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ProfileToDisplay;
import pojos.ProfileToDisplayFactory;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.*;
import stories.AbstractFacade;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MapsEditionFacadeImpl extends AbstractFacade implements MapsEditionFacade {

    private static final Logger logger = LogManager.getLogger(MapsEditionFacadeImpl.class);

    private final MapDtoFactory mapDtoFactory;
    private final PropertyService propertyService;
    private final ProfileDtoFactory profileDtoFactory;
    private final OfficialMapServiceImpl officialMapService;
    private final CustomMapModServiceImpl customMapModService;
    private final ProfileToDisplayFactory profileToDisplayFactory;
    private final ProfileService profileService;

    public MapsEditionFacadeImpl() {
        super();
        this.mapDtoFactory = new MapDtoFactory();
        this.propertyService = new PropertyServiceImpl();
        this.profileDtoFactory = new ProfileDtoFactory();
        this.officialMapService = new OfficialMapServiceImpl();
        this.customMapModService = new CustomMapModServiceImpl();
        this.profileToDisplayFactory = new ProfileToDisplayFactory();
        this.profileService = new ProfileServiceImpl();
    }

    private CustomMapMod createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto, boolean downloaded, List<Profile> profileList) throws Exception {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        List<ProfileMap> profileMapList = new ArrayList<ProfileMap>();
        profileList.forEach(p -> {
            profileMapList.addAll(p.getProfileMapList());
        });

        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, profileMapList, idWorkShop, downloaded);
        return (CustomMapMod) customMapModService.createMap(customMap);
    }

    @Override
    public CustomMapModDto createNewCustomMapFromWorkshop(Long idWorkShop, String installationFolder, boolean downloaded, List<String> profileNameList) throws Exception {
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

        CustomMapMod newCustomMapMod = createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, downloaded, profileList);
        return (CustomMapModDto) mapDtoFactory.newDto(newCustomMapMod);
    }

    @Override
    public CustomMapModDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder, boolean downloaded, List<String> profileNameList) throws Exception {
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

        CustomMapMod newMap = createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, downloaded, profileList);
        return (CustomMapModDto) mapDtoFactory.newDto(newMap);
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
    public OfficialMapDto insertOfficialMap(String mapName, List<String> profileNameList) throws Exception {
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
        List<ProfileMap> profileMapList = new ArrayList<ProfileMap>();
        profileList.stream().forEach(p -> {
            profileMapList.addAll(p.getProfileMapList());
        });

        OfficialMap newOfficialMap = new OfficialMap(mapName, "", "/KFGame/Web/images/maps/" + mapName + ".jpg", profileMapList, null);
        OfficialMap insertedMap = (OfficialMap) officialMapService.createMap(newOfficialMap);

        if (insertedMap != null) {
            insertedMap.getProfileMapList().forEach(profileMap -> {
                try {
                    profileMap.setImportedDate(new Date());
                    officialMapService.updateItem(insertedMap);
                } catch (SQLException e) {
                    logger.error("Error setting the imported date of the map " + insertedMap.getCode() + " for profile " + profileMap.getProfile().getName(), e);
                }
            });
            return (OfficialMapDto) mapDtoFactory.newDto(insertedMap);
        }
        return null;
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
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = profileService.listAllProfiles();
        return profileDtoFactory.newDtos(profiles);
    }


    @Override
    public List<AbstractMapDto> getMapsFromProfile(String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            return mapDtoFactory.newDtos(
                    profileOpt.get().getMapList()
            );
        }
        return new ArrayList<AbstractMapDto>();
    }


    @Override
    public Boolean addProfilesToMap(String mapName, List<String> profileNameList) throws SQLException {

        List<Profile> profileList = profileNameList.stream().map(pn -> {
                    try {
                        return findProfileByCode(pn);
                    } catch (SQLException e) {
                        logger.error("Error finding a profile by name " + pn, e);
                        return null;
                    }
                })
                .collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            return null;
        }

        Optional officialMapOptional = officialMapService.findMapByCode(mapName);
        if (officialMapOptional.isPresent()) {
            OfficialMap officialMap = (OfficialMap) officialMapOptional.get();
            List<Profile> profilesNotContainingMap =  profileList.stream().
                    filter(profile -> !profile.getMapList().contains(officialMap)).
                    collect(Collectors.toList());

            if (officialMapService.addProfilesToMap(officialMap, profilesNotContainingMap)) {
                officialMap.getProfileMapList().stream().
                        filter(profileMap -> profilesNotContainingMap.contains(profileMap)).
                        forEach(profileMap -> {
                            try {
                                profileMap.setImportedDate(new Date());
                                officialMapService.updateItem(officialMap);
                            } catch (SQLException e) {
                                logger.error("Error setting the imported date of the map " + officialMap.getCode() + " for profile " + profileMap.getProfile().getName(), e);
                            }
                        });
                return true;
            }
            return false;
        }

        Optional customMapModOptional = customMapModService.findMapByCode(mapName);
        if (customMapModOptional.isPresent()) {
            CustomMapMod customMapMod = (CustomMapMod) customMapModOptional.get();
            List<Profile> profilesNotContainingMap =  profileList.stream().
                    filter(profile -> !profile.getMapList().contains(customMapMod)).
                    collect(Collectors.toList());

            return customMapModService.addProfilesToMap(customMapMod,profilesNotContainingMap);
        }
        return null;
    }



    @Override
    public AbstractMapDto deleteMapFromProfile(String mapName, String profileName, String installationFolder) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Optional<AbstractMap> mapOpt = profileOpt.get().getMapList().stream().filter(m -> m.getCode().equalsIgnoreCase(mapName)).findFirst();

            if (mapOpt.isPresent()) {

                Optional officialMapOptional = officialMapService.findMapByCode(mapName);
                if (officialMapOptional.isPresent()) {
                    OfficialMap deletedMap = (OfficialMap) officialMapService.deleteMap((OfficialMap) officialMapOptional.get(), profileOpt.get());
                    return mapDtoFactory.newDto(deletedMap);
                }

                Optional customMapModOptional = customMapModService.findMapByCode(mapName);
                if (customMapModOptional.isPresent()) {
                    CustomMapMod deletedMap = (CustomMapMod) customMapModService.deleteMap((CustomMapMod) customMapModOptional.get(), profileOpt.get(), installationFolder);
                    return mapDtoFactory.newDto(deletedMap);
                }
            }
        }
        return null;
    }

    @Override
    public AbstractMapDto findMapByName(String mapName) throws SQLException {

        Optional officialMapOptional = officialMapService.findMapByCode(mapName);
        if (officialMapOptional.isPresent()) {
            return mapDtoFactory.newDto((OfficialMap) officialMapOptional.get());
        }
        Optional customMapModOptional = customMapModService.findMapByCode(mapName);
        if (customMapModOptional.isPresent()) {
            return mapDtoFactory.newDto((CustomMapMod) customMapModOptional.get());
        }
        return null;
    }

    @Override
    public void unselectProfileMap(String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setMap(null);
            ProfileDao.getInstance().update(profileOpt.get());
        }
    }

    @Override
    public List<String> selectProfilesToImport(String defaultSelectedProfileName) throws Exception {
        List<Profile> allProfiles = profileService.listAllProfiles();
        if (allProfiles == null || allProfiles.isEmpty()) {
            return new ArrayList<String>();
        }
        List<ProfileToDisplay> selectedProfiles = selectProfilesToImport(allProfiles, defaultSelectedProfileName);
        return selectedProfiles.stream().map(p -> p.getProfileName()).collect(Collectors.toList());
    }


    public List<ProfileToDisplay> selectProfilesToImport(List<Profile> allProfiles, String defaultSelectedProfileName) throws Exception {
        List<ProfileToDisplay> allProfilesToDisplay = profileToDisplayFactory.newOnes(allProfiles);
        allProfilesToDisplay.stream().filter(p -> p.getProfileName().equalsIgnoreCase(defaultSelectedProfileName)).forEach(profile -> profile.setSelected(true));
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfiles");
        return Utils.selectProfilesDialog(headerText + ":", allProfilesToDisplay);
    }

    @Override
    public String runServer(String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        Kf2Common kf2Common = Kf2Factory.getInstance();
        return kf2Common.runServer(profileOpt.isPresent()? profileOpt.get(): null);
    }
}
