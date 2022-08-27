package stories.mapsedition;

import daos.CustomMapModDao;
import daos.ProfileDao;
import daos.ProfileMapDao;
import dtos.*;
import dtos.factories.MapDtoFactory;
import dtos.factories.ProfileDtoFactory;
import dtos.factories.ProfileMapDtoFactory;
import entities.*;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ImportMapResultToDisplay;
import pojos.ProfileToDisplay;
import pojos.ProfileToDisplayFactory;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
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
    private final OfficialMapServiceImpl officialMapService;
    private final CustomMapModServiceImpl customMapModService;
    private final ProfileToDisplayFactory profileToDisplayFactory;
    private final ProfileService profileService;
    private final ProfileMapDtoFactory profileMapDtoFactory;
    private ProfileMapService profileMapService;

    public MapsEditionFacadeImpl() {
        super();
        this.mapDtoFactory = new MapDtoFactory();
        this.propertyService = new PropertyServiceImpl();
        this.profileDtoFactory = new ProfileDtoFactory();
        this.officialMapService = new OfficialMapServiceImpl();
        this.customMapModService = new CustomMapModServiceImpl();
        this.profileToDisplayFactory = new ProfileToDisplayFactory();
        this.profileService = new ProfileServiceImpl();
        this.profileMapDtoFactory = new ProfileMapDtoFactory();
        this.profileMapService = new ProfileMapServiceImpl();
    }

    private CustomMapMod createNewCustomMap(String mapName, Long idWorkShop, String urlPhoto, boolean downloaded, List<Profile> profileList, List<ImportMapResultToDisplay> importMapResultToDisplayList) throws Exception {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, idWorkShop, downloaded);
        return (CustomMapMod) customMapModService.createMap(customMap, profileList, importMapResultToDisplayList);
    }


    private CustomMapMod createNewCustomMapFromWorkshop(Long idWorkShop, String installationFolder, boolean downloaded, List<String> profileNameList) throws Exception {
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

        return createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, downloaded, profileList, null);
    }

    private CustomMapMod createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder, boolean downloaded, List<String> profileNameList, List<ImportMapResultToDisplay> importMapResultToDisplayList) throws Exception {
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

        return createNewCustomMap(mapName, idWorkShop, relativeTargetFolder, downloaded, profileList, importMapResultToDisplayList);
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


    private OfficialMap insertOfficialMap(String mapName, List<String> profileNameList, List<ImportMapResultToDisplay> importMapResultToDisplayList) {
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

        OfficialMap newOfficialMap = new OfficialMap(mapName, "", "/KFGame/Web/images/maps/" + mapName + ".jpg", null);
        OfficialMap insertedMap = (OfficialMap) officialMapService.createMap(newOfficialMap, profileList, importMapResultToDisplayList);

        return insertedMap;


    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = profileService.listAllProfiles();
        return profileDtoFactory.newDtos(profiles);
    }


    @Override
    public AbstractMapDto addProfilesToMap(String mapName, List<String> profileNameList, List<ImportMapResultToDisplay> importMapResultToDisplayList) throws SQLException {

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

            return mapDtoFactory.newDto(
                    officialMapService.addProfilesToMap(officialMap, profilesNotContainingMap, importMapResultToDisplayList)
            );
        }

        Optional customMapModOptional = customMapModService.findMapByCode(mapName);
        if (customMapModOptional.isPresent()) {
            CustomMapMod customMapMod = (CustomMapMod) customMapModOptional.get();
            List<Profile> profilesNotContainingMap =  profileList.stream().
                    filter(profile -> !profile.getMapList().contains(customMapMod)).
                    collect(Collectors.toList());

            return mapDtoFactory.newDto(
                    customMapModService.addProfilesToMap(customMapMod,profilesNotContainingMap,importMapResultToDisplayList)
            );
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
    public Optional<AbstractMap> findMapByName(String mapName) throws SQLException {
        Optional<AbstractMap> officialMapOptional = officialMapService.findMapByCode(mapName);
        if (officialMapOptional.isPresent()) {
            return officialMapOptional;
        }
        Optional<AbstractMap> customMapModOptional = customMapModService.findMapByCode(mapName);
        return customMapModOptional;
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
        Kf2Common kf2Common = Kf2Factory.getInstance(
                profileOpt.isPresent() ? profileOpt.get().getPlatform(): null
        );
        return kf2Common.runServer(profileOpt.isPresent()? profileOpt.get(): null);
    }

    @Override
    public List<AbstractMapDto> addCustomMapsToProfile(String profileName, String mapNameList, String languageCode, String installationFolder, String actualSelectedProfile, StringBuffer success, StringBuffer errors) {

        List<AbstractMapDto> mapAddedList = new ArrayList<AbstractMapDto>();

        if (StringUtils.isNotBlank(mapNameList)) {
            String[] idUrlWorkShopArray = mapNameList.replaceAll(" ", "").split(",");

            for (int i = 0; i < idUrlWorkShopArray.length; i++) {
                String profileNameMessage = StringUtils.EMPTY;
                try {
                    profileNameMessage = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.label.profileName");
                    Long idWorkShop = null;
                    if (idUrlWorkShopArray[i].contains("http")) {
                        String[] array = idUrlWorkShopArray[i].split("=");
                        idWorkShop = Long.parseLong(array[1]);
                    } else {
                        idWorkShop = Long.parseLong(idUrlWorkShopArray[i]);
                    }

                    Optional<CustomMapMod> mapModInDataBase = CustomMapModDao.getInstance().findByIdWorkShop(idWorkShop);
                    if (!mapModInDataBase.isPresent()) {
                        // The map is not in dabatabase, create a new map for actual profile
                        List<String> profileNameList = new ArrayList<String>();
                        profileNameList.add(profileName);

                        CustomMapMod customMap = createNewCustomMapFromWorkshop(idWorkShop, installationFolder, false, profileNameList);
                        if (customMap != null) {
                            if (profileName.equalsIgnoreCase(actualSelectedProfile)) {
                                mapAddedList.add(mapDtoFactory.newDto(customMap));

                            }
                            String mapNameMessage = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
                            success.append(profileNameMessage + ": ").append(profileName).append(" - " + mapNameMessage + ": ").append(customMap.getCode()).append(" - id WorkShop: ").append(customMap.getIdWorkShop()).append("\n");

                        } else {
                            errors.append(profileNameMessage + ": ").append(profileName).append(" - url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                        }
                    } else {
                        if (mapModInDataBase.get().getProfileList().stream().filter(profile -> profile.getCode().equals(profileName)).findFirst().isPresent()) {
                            errors.append(profileNameMessage + ": ").append(profileName).append(" - url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");

                        } else {
                            List<String> profileList = new ArrayList<String>();
                            profileList.add(profileName);
                            addProfilesToMap(mapModInDataBase.get().getCode(), profileList, null);
                            mapAddedList.add(mapDtoFactory.newDto(mapModInDataBase.get()));
                            String mapNameMessage = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapName");
                            success.append(profileNameMessage + ": ").append(profileName).append(" - " + mapNameMessage + ": ").append(mapModInDataBase.get().getCode()).append(" - id WorkShop: ").append(mapModInDataBase.get().getIdWorkShop()).append("\n");
                        }
                    }

                } catch (Exception e) {
                    errors.append(profileNameMessage + ": ").append(profileName).append(" - url/id WorkShop: ").append(idUrlWorkShopArray[i]).append("\n");
                }
            }
        }
        return mapAddedList;
    }

    @Override
    public CustomMapModDto importCustomMapModFromServer(String mapNameLabel, Long idWorkShop, String commentary, String installationFolder, List<String> selectedProfileNameList, String actualSelectedProfile, List<ImportMapResultToDisplay> importMapResultToDisplayList) {

        try {
            Optional<CustomMapMod> mapInDataBase = CustomMapModDao.getInstance().findByIdWorkShop(idWorkShop);
            if (!mapInDataBase.isPresent()) {
                CustomMapMod customMap = createNewCustomMapFromWorkshop(
                        idWorkShop,
                        commentary,
                        installationFolder,
                        false,
                        selectedProfileNameList,
                        importMapResultToDisplayList
                );
                return (CustomMapModDto) mapDtoFactory.newDto(customMap);

            } else {
                return (CustomMapModDto) addProfilesToMap(mapInDataBase.get().getCode(), selectedProfileNameList, importMapResultToDisplayList);
            }
        } catch (Exception e) {
            logger.error("Error importing the custom map/mod with idWorkShop: " + idWorkShop + " from server", e);
        }
        return null;
    }

    @Override
    public OfficialMapDto importOfficialMapFromServer(String officialMapName, List<String> selectedProfileNameList, String actualSelectedProfile, String mapNameLabel, List<ImportMapResultToDisplay> importMapResultToDisplayList) {

        try {
            Optional<AbstractMap> mapInDataBase = findMapByName(officialMapName);
            if (!mapInDataBase.isPresent()) {
                OfficialMap insertedMap = insertOfficialMap(officialMapName, selectedProfileNameList, importMapResultToDisplayList);
                return (OfficialMapDto) mapDtoFactory.newDto(insertedMap);

             } else {
                return (OfficialMapDto) addProfilesToMap(mapInDataBase.get().getCode(), selectedProfileNameList, importMapResultToDisplayList);
            }
        } catch (Exception e) {
            logger.error("Error importing the official map with name: " + officialMapName + " from server", e);
        }
        return null;
    }

    @Override
    public Optional<ProfileMap> findProfileMapByNames(String profileName, String mapName) throws SQLException {
        return profileMapService.findProfileMapByNames(profileName, mapName);
    }

    @Override
    public Optional<ProfileMapDto> findProfileMapDtoByNames(String profileName, String mapName) throws SQLException {
        Optional<ProfileMap> profileMapOptional = findProfileMapByNames(profileName, mapName);
        if (profileMapOptional.isPresent()) {
            return Optional.ofNullable(profileMapDtoFactory.newDto(profileMapOptional.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<ProfileMapDto> listProfileMaps(String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            Profile profile = profileOpt.get();
            List<ProfileMap> profileMapList = profileMapService.listProfileMaps(profile);
            return profileMapDtoFactory.newDtos(profileMapList);
        }
        return new ArrayList<ProfileMapDto>();
    }
}
