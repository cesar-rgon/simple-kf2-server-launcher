package stories.mapsedition;

import daos.*;
import dtos.*;
import dtos.factories.MapDtoFactory;
import dtos.factories.PlatformDtoFactory;
import dtos.factories.PlatformProfileMapDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.*;
import entities.AbstractMap;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.*;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.*;
import stories.AbstractFacade;
import utils.Utils;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class MapsEditionFacadeImpl extends AbstractFacade implements MapsEditionFacade {

    private static final Logger logger = LogManager.getLogger(MapsEditionFacadeImpl.class);

    private final MapDtoFactory mapDtoFactory;
    private final PropertyService propertyService;
    private final ProfileDtoFactory profileDtoFactory;
    private final OfficialMapServiceImpl officialMapService;
    private final CustomMapModServiceImpl customMapModService;
    private final PlatformProfileToDisplayFactory platformProfileToDisplayFactory;
    private final ProfileService profileService;
    private final PlatformProfileMapDtoFactory platformProfileMapDtoFactory;
    private final PlatformProfileMapService platformProfileMapService;
    private final PlatformDtoFactory platformDtoFactory;
    private final CustomMapModServiceImpl customMapService;

    public MapsEditionFacadeImpl() {
        super();
        this.mapDtoFactory = new MapDtoFactory();
        this.propertyService = new PropertyServiceImpl();
        this.profileDtoFactory = new ProfileDtoFactory();
        this.officialMapService = new OfficialMapServiceImpl();
        this.customMapModService = new CustomMapModServiceImpl();
        this.platformProfileToDisplayFactory = new PlatformProfileToDisplayFactory();
        this.profileService = new ProfileServiceImpl();
        this.platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory();
        this.platformProfileMapService = new PlatformProfileMapServiceImpl();
        this.platformDtoFactory = new PlatformDtoFactory();
        this.customMapService = new CustomMapModServiceImpl();
    }

    private CustomMapMod createNewCustomMap(List<AbstractPlatform> platformList, String mapName, Long idWorkShop, String urlPhoto, List<Profile> profileList, StringBuffer success, StringBuffer errors) throws Exception {
        if ((StringUtils.isBlank(mapName) || idWorkShop == null)) {
            return null;
        }
        String baseUrlWorkshop = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapBaseUrlWorkshop");
        String urlInfo = baseUrlWorkshop + idWorkShop;
        CustomMapMod customMap = new CustomMapMod(mapName, urlInfo, urlPhoto, idWorkShop);
        return (CustomMapMod) customMapModService.createMap(platformList, customMap, profileList, success, errors);
    }


    @Override
    public String[] getMapNameAndUrlImage(Long idWorkShop) throws Exception {
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

    private CustomMapMod createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, List<String> profileNameList, String mapName, String strUrlMapImage, StringBuffer success, StringBuffer errors) throws Exception {
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
        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            try {
                return findPlatformByCode(pn);
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

        return createNewCustomMap(platformList, mapName, idWorkShop, relativeTargetFolder, profileList, success, errors);
    }

    private CustomMapMod createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, String mapName, List<String> profileNameList, StringBuffer success, StringBuffer errors) throws Exception {
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
        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            try {
                return findPlatformByCode(pn);
            } catch (SQLException e) {
                logger.error("Error finding a platform by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());

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

        File localfile = new File(StringUtils.EMPTY);
        for (AbstractPlatform platform: platformList) {
            String absoluteTargetFolder = platform.getInstallationFolder() + customMapLocalFolder;
            localfile = Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapName);
        }
        String relativeTargetFolder = customMapLocalFolder + "/" + localfile.getName();

        return createNewCustomMap(platformList, mapName, idWorkShop, relativeTargetFolder, profileList, success, errors);
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


    private OfficialMap insertOfficialMap(List<String> platformNameList, String mapName, List<String> profileNameList) throws Exception {

        List<Profile> profileList = profileNameList.stream().map(pn -> {
            try {
                return findProfileByCode(pn);
            } catch (SQLException e) {
                logger.error("Error finding a profile by name " + pn, e);
                return null;
            }
        }).collect(Collectors.toList());

        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            try {
                return findPlatformByCode(pn);
            } catch (SQLException e) {
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

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = profileService.listAllProfiles();
        return profileDtoFactory.newDtos(profiles);
    }


    @Override
    public void addPlatformProfileMapList(List<PlatformProfileMap> platformProfileMapListToAdd, StringBuffer success, StringBuffer errors) throws SQLException {
        customMapModService.addPlatformProfileMapList(platformProfileMapListToAdd, success, errors);
    }



    @Override
    public AbstractMapDto deleteMapFromPlatformProfile(String platformName, String mapName, String profileName) throws Exception {

        Optional<PlatformProfileMap> platformProfileMapOptional = PlatformProfileMapDao.getInstance().findByPlatformNameProfileNameMapName(platformName, profileName, mapName);
        if (!platformProfileMapOptional.isPresent()) {
            return null;
        }

        Optional officialMapOptional = officialMapService.findMapByCode(mapName);
        if (officialMapOptional.isPresent()) {
            OfficialMap officialMap = (OfficialMap) Hibernate.unproxy(platformProfileMapOptional.get().getMap());
            OfficialMap deletedMap = (OfficialMap) officialMapService.deleteMap(platformProfileMapOptional.get().getPlatform(), officialMap, platformProfileMapOptional.get().getProfile());
            return mapDtoFactory.newDto(deletedMap);
        }

        Optional customMapModOptional = customMapModService.findMapByCode(mapName);
        if (customMapModOptional.isPresent()) {
            CustomMapMod customMapMod = (CustomMapMod) Hibernate.unproxy(platformProfileMapOptional.get().getMap());
            CustomMapMod deletedMap = customMapModService.deleteMap(platformProfileMapOptional.get().getPlatform(), customMapMod, platformProfileMapOptional.get().getProfile());
            return mapDtoFactory.newDto(deletedMap);
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
    public List<PlatformProfileToDisplay> selectProfilesToImport(String defaultSelectedProfileName) throws Exception {
        List<Profile> allProfiles = profileService.listAllProfiles();
        if (allProfiles == null || allProfiles.isEmpty()) {
            return new ArrayList<PlatformProfileToDisplay>();
        }
        return selectProfilesToImport(allProfiles, defaultSelectedProfileName);
    }


    public List<PlatformProfileToDisplay> selectProfilesToImport(List<Profile> allProfiles, String defaultSelectedProfileName) throws Exception {
        List<AbstractPlatform> allPlatformList = AbstractPlatformDao.getInstance().listAll();
        List<Profile> allProfileList = profileService.listAllProfiles();
        List<PlatformProfile> platformProfileList = new ArrayList<PlatformProfile>();
        for (Profile profile: allProfileList) {
            for (AbstractPlatform platform: allPlatformList) {
                platformProfileList.add(new PlatformProfile(platform, profile));
            }
        }
        List<String> fullProfileNameList = EnumPlatform.listAll().stream().map(EnumPlatform::name).collect(Collectors.toList());

        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectProfiles");
        return Utils.selectPlatformProfilesDialog(headerText + ":", platformProfileList, fullProfileNameList);
    }

    @Override
    public String runServer(String profileName) throws SQLException {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        Kf2Common kf2Common = Kf2Factory.getInstance(
                Session.getInstance().getPlatform().getKey()
        );
        return kf2Common.runServer(profileOpt.isPresent()? profileOpt.get(): null);
    }

    private Profile getProfileByName(String profileName, StringBuffer success, StringBuffer errors) {
        Profile profile = null;
        try {
            profile = findProfileByCode(profileName);
        } catch (SQLException e) {
            String message = "Error finding the profile with name" + profileName;
            errors.append(message + "\n");
            logger.error(message, e);
            throw new RuntimeException(message, e);
        }
        if (profile == null) {
            String message = "No profiles were found";
            errors.append(message + "\n");
            throw new RuntimeException(message);
        }
        return profile;
    }

    private List<AbstractPlatform> getPlatformListByNames(List<String> platformNameList, StringBuffer success, StringBuffer errors) {
        List<AbstractPlatform> platformList = platformNameList.stream().map(pn -> {
            String message = "Error finding the platform with name" + pn;
            try {
                Optional<SteamPlatform> steamPlatformOptional = SteamPlatformDao.getInstance().findByCode(pn);
                if (steamPlatformOptional.isPresent()) {
                    return steamPlatformOptional.get();
                }
                Optional<EpicPlatform> epicPlatformOptional = EpicPlatformDao.getInstance().findByCode(pn);
                if (epicPlatformOptional.isPresent()) {
                    return epicPlatformOptional.get();
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

    private List<PlatformProfile> getPlatformProfileListWithoutMap(Long idWorkShop) throws SQLException {

        List<AbstractPlatform> fullPlatformList = AbstractPlatformDao.getInstance().listAll();
        List<Profile> fullProfileList = profileService.listAllProfiles();
        List<PlatformProfile> platformProfileListWithoutMap = new ArrayList<PlatformProfile>();

        for (Profile profile: fullProfileList) {
            for (AbstractPlatform platform: fullPlatformList) {
                Optional<PlatformProfileMap> platformProfileMapOptional = PlatformProfileMapDao.getInstance().listPlatformProfileMaps(platform, profile).stream().
                        filter(ppm -> {
                            try {
                                Optional<CustomMapMod> customMapModOptional = CustomMapModDao.getInstance().findByCode(ppm.getMap().getCode());
                                if (customMapModOptional.isPresent()) {
                                    return idWorkShop.equals(customMapModOptional.get().getIdWorkShop());
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
    public List<PlatformProfileMapDto> addCustomMapsToProfile(List<String> platformNameList, String profileName, String mapNameList, String languageCode, String actualSelectedProfile, StringBuffer success, StringBuffer errors) {

        Profile profile = getProfileByName(profileName, success, errors);
        List<AbstractPlatform> platformList = getPlatformListByNames(platformNameList, success, errors);

        List<PlatformProfileMapDto> mapAddedList = new ArrayList<PlatformProfileMapDto>();
        if (StringUtils.isNotBlank(mapNameList)) {
            String[] idUrlWorkShopArray = mapNameList.replaceAll(" ", "").split(",");

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

                    Optional<CustomMapMod> mapModInDataBase = CustomMapModDao.getInstance().findByIdWorkShop(idWorkShop);
                    if (!mapModInDataBase.isPresent()) {
                        // The map is not in dabatabase, create a new map for actual profile
                        List<String> profileNameList = new ArrayList<String>();
                        profileNameList.add(profileName);

                        CustomMapMod customMap = createNewCustomMapFromWorkshop(platformNameList, idWorkShop, profileNameList, mapName, strUrlMapImage, success, errors);
                        if (customMap != null) {
                            if (profileName.equalsIgnoreCase(actualSelectedProfile)) {
                                Optional<Profile> profileOptional = ProfileDao.getInstance().findByCode(profileName);
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
                        List<AbstractPlatform> platformListForProfileWithoutMap = getPlatformProfileListWithoutMap(idWorkShop).stream().map(PlatformProfile::getPlatform).collect(Collectors.toList());
                        List<PlatformProfileMap> ppmList = new ArrayList<PlatformProfileMap>();
                        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

                        for (AbstractPlatform platform: platformList) {
                            if (platformListForProfileWithoutMap.contains(platform)) {
                                PlatformProfileMap ppm = new PlatformProfileMap(platform, profile, mapModInDataBase.get(), mapModInDataBase.get().getReleaseDate(), mapModInDataBase.get().getUrlInfo(), mapModInDataBase.get().getUrlPhoto(), false);
                                ppmList.add(ppm);

                                String absoluteTargetFolder = ppm.getPlatform().getInstallationFolder() + customMapLocalFolder;
                                Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, mapModInDataBase.get().getCode());
                            } else {
                                String errorMessage = "Error creating the relation between the map " + mapName + ", the profile " + profile.getCode() + " and the platform " + platform.getDescription();
                                errors.append(errorMessage + "\n");
                            }
                        }

                        addPlatformProfileMapList(ppmList, success, errors);
                        mapAddedList.addAll(platformProfileMapDtoFactory.newDtos(ppmList));
                    }

                } catch (Exception e) {
                    String message = "Error adding map/mod to the launcher";
                    logger.error(message, e);
                }
            }
        }
        return mapAddedList;
    }

    @Override
    public PlatformProfileMapToImport importCustomMapModFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName) throws Exception {

        Optional<AbstractPlatform> platformOptional = AbstractPlatformDao.getInstance().findByCode(ppmToImport.getPlatformName());
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
        List<String> selectedProfileNameList = new ArrayList<String>();
        selectedProfileNameList.add(selectedProfileName);


        Optional mapInDataBase = customMapModService.findByIdWorkShop(ppmToImport.getMapToDisplay().getIdWorkShop());
        if (!mapInDataBase.isPresent()) {
            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();

            CustomMapMod customMap = createNewCustomMapFromWorkshop(
                    platformNameList,
                    ppmToImport.getMapToDisplay().getIdWorkShop(),
                    ppmToImport.getMapToDisplay().getCommentary(),
                    selectedProfileNameList,
                    success,
                    errors
            );
            if (customMap == null) {
                throw new RuntimeException(errors.toString());
            }

        } else {
            CustomMapMod customMap = (CustomMapMod) mapInDataBase.get();
            List<PlatformProfileMap> platformProfileMapListToAdd = new ArrayList<PlatformProfileMap>();
            platformProfileMapListToAdd.add(
                new PlatformProfileMap(platformOptional.get(), profileOptional.get(), customMap, customMap.getReleaseDate(), customMap.getUrlInfo(), customMap.getUrlPhoto(), false)
            );

            StringBuffer success = new StringBuffer();
            StringBuffer errors = new StringBuffer();
            addPlatformProfileMapList(platformProfileMapListToAdd, success, errors);

            if (StringUtils.isNotBlank(errors)) {
                throw new RuntimeException(errors.toString());
            }
        }

        return ppmToImport;
    }

    @Override
    public PlatformProfileMapToImport importOfficialMapFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName) throws Exception {

        Optional<AbstractPlatform> platformOptional = AbstractPlatformDao.getInstance().findByCode(ppmToImport.getPlatformName());
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

        Optional<AbstractMap> mapInDataBase = findMapByName(officialMapName);
        if (!mapInDataBase.isPresent()) {

            OfficialMap insertedMap = insertOfficialMap(
                    platformNameList,
                    officialMapName,
                    selectedProfileNameList
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
            addPlatformProfileMapList(platformProfileMapListToAdd, success, errors);

            if (StringUtils.isNotBlank(errors)) {
                throw new RuntimeException(errors.toString());
            }
        }

        return ppmToImport;
    }

    public Optional<PlatformProfileMap> findPlatformProfileMapByNames(String platformName, String profileName, String mapName) throws SQLException {
        return platformProfileMapService.findPlatformProfileMapByNames(platformName, profileName, mapName);
    }

    @Override
    public Optional<PlatformProfileMapDto> findPlatformProfileMapDtoByNames(String platformName, String profileName, String mapName) throws SQLException {
        Optional<PlatformProfileMap> profileMapOptional = findPlatformProfileMapByNames(platformName, profileName, mapName);
        if (profileMapOptional.isPresent()) {
            return Optional.ofNullable(platformProfileMapDtoFactory.newDto(profileMapOptional.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<PlatformProfileMapDto> listPlatformProfileMaps(String platformName, String profileName) throws SQLException {
        Optional<AbstractPlatform> platformOpt = AbstractPlatformDao.getInstance().findByCode(platformName);
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (!platformOpt.isPresent() || !profileOpt.isPresent()) {
            return new ArrayList<PlatformProfileMapDto>();
        }

        Profile profile = profileOpt.get();
        List<PlatformProfileMap> platformProfileMapList = platformProfileMapService.listPlatformProfileMaps(platformOpt.get(), profile);
        return platformProfileMapDtoFactory.newDtos(platformProfileMapList);
    }

    @Override
    public List<PlatformDto> listAllPlatforms() throws SQLException {
        List<AbstractPlatform> allPlatforms = new ArrayList<AbstractPlatform>();
        Optional<SteamPlatform> steamPlatformOptional = SteamPlatformDao.getInstance().findByCode(EnumPlatform.STEAM.name());
        if (steamPlatformOptional.isPresent()) {
            allPlatforms.add(steamPlatformOptional.get());
        }
        Optional<EpicPlatform> epicPlatformOptional = EpicPlatformDao.getInstance().findByCode(EnumPlatform.EPIC.name());
        if (epicPlatformOptional.isPresent()) {
            allPlatforms.add(epicPlatformOptional.get());
        }
        return platformDtoFactory.newDtos(allPlatforms);
    }

    @Override
    public String getPropertyValue(String propFileRelativePath, String propKey, String profileParam, String platformParam) throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./" + propFileRelativePath);
        } catch (FileNotFoundException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileRelativePath);
        }
        prop.load(inputStream);
        inputStream.close();

        return MessageFormat.format(prop.getProperty(propKey), profileParam, platformParam);
    }

    @Override
    public AbstractMapDto getMapByIdWorkShop(Long idWorkShop) throws SQLException {
        Optional<AbstractMap> mapOptional = customMapService.findByIdWorkShop(idWorkShop);
        if (mapOptional.isPresent()) {
            return mapDtoFactory.newDto(mapOptional.get());
        }
        return null;
    }

    public AbstractMapDto getOfficialMapByName(String mapName) throws SQLException {
        Optional<AbstractMap> mapOptional = officialMapService.findMapByCode(mapName);
        if (mapOptional.isPresent()) {
            return mapDtoFactory.newDto(mapOptional.get());
        }
        return null;
    }

    @Override
    public List<MapToDisplay> getNotPresentOfficialMapList(List<String> officialMapNameList, String platformName, String profileName) throws Exception {
        AbstractPlatform platform = findPlatformByCode(platformName);
        if (platform == null) {
            throw new RuntimeException("No platform was found: " + platformName);
        }
        Profile profile = findProfileByCode(profileName);
        if (profile == null) {
            throw new RuntimeException("No profile was found: " + profileName);
        }

        List<String> mapListInPlatformProfile = PlatformProfileMapDao.getInstance().listPlatformProfileMaps(platform, profile).stream().
                map(ppm -> ppm.getMap().getCode()).
                collect(Collectors.toList());

        List<String> result = new ArrayList<>(officialMapNameList);
        result.removeAll(mapListInPlatformProfile);

        List<MapToDisplay> mapListNotInPlatformProfile = result.stream().
                map(mapName -> {
                    return new MapToDisplay(mapName);
                }).
                collect(Collectors.toList());

        return mapListNotInPlatformProfile;
    }
}
