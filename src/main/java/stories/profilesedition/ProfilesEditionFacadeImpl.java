package stories.profilesedition;

import daos.*;
import dtos.MapDto;
import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.*;
import javafx.collections.ObservableList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;


public class ProfilesEditionFacadeImpl implements ProfilesEditionFacade {

    private static final Logger logger = LogManager.getLogger(ProfilesEditionFacadeImpl.class);
    private final ProfileDtoFactory profileDtoFactory;
    private final PropertyService propertyService;

    public ProfilesEditionFacadeImpl() {
        profileDtoFactory = new ProfileDtoFactory();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public ObservableList<ProfileDto> listAllProfiles() throws SQLException {
        List<Profile> profiles = ProfileDao.getInstance().listAll();
        return profileDtoFactory.newDtos(profiles);
    }

    @Override
    public ProfileDto createNewProfile(String profileName) throws Exception {
        String defaultServername = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultServername");
        String defaultWebPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultWebPort");
        String defaultGamePort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultGamePort");
        String defaultQueryPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.defaultQueryPort");

        List<Map> officialMaps = MapDao.getInstance().listOfficialMaps();
        Map firstOfficialMap = null;
        if (officialMaps != null && !officialMaps.isEmpty()) {
            firstOfficialMap = officialMaps.get(0);
        }

        Profile newProfile = new Profile(
                profileName,
                LanguageDao.getInstance().listAll().get(0),
                GameTypeDao.getInstance().listAll().get(0),
                firstOfficialMap,
                DifficultyDao.getInstance().listAll().get(0),
                LengthDao.getInstance().listAll().get(0),
                MaxPlayersDao.getInstance().listAll().get(0),
                StringUtils.isNotBlank(defaultServername) ? defaultServername: "Killing Floor 2 Server",
                Integer.parseInt(defaultWebPort),
                Integer.parseInt(defaultGamePort),
                Integer.parseInt(defaultQueryPort),
                officialMaps
        );
        ProfileDao.getInstance().insert(newProfile);
        return profileDtoFactory.newDto(newProfile);
    }

    @Override
    public boolean deleteSelectedProfile(String profileName, String installationFolder) throws Exception {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profileName);
        if (profileOpt.isPresent()) {

            List<Map> mapList = new ArrayList<Map>(profileOpt.get().getMapList());
            profileOpt.get().getMapList().clear();
            ProfileDao.getInstance().update(profileOpt.get());
            profileOpt = ProfileDao.getInstance().findByName(profileName);
            for (Map map: mapList) {
                map.getProfileList().remove(profileOpt.get());
                MapDao.getInstance().update(map);
                if (!map.isOfficial() && map.getProfileList().isEmpty()) {
                    if (MapDao.getInstance().remove(map)) {
                        File photo = new File(installationFolder + map.getUrlPhoto());
                        photo.delete();
                        File cacheFolder = new File(installationFolder + "/KFGame/Cache/" + map.getIdWorkShop());
                        FileUtils.deleteDirectory(cacheFolder);
                    }
                }
            }

            return ProfileDao.getInstance().remove(profileOpt.get());
        }
        return false;
    }

    @Override
    public ProfileDto updateChangedProfile(String oldProfileName, String newProfileName) throws SQLException {
        if (StringUtils.isBlank(newProfileName) || newProfileName.equalsIgnoreCase(oldProfileName)) {
            return null;
        }
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(oldProfileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setName(newProfileName);
            if (ProfileDao.getInstance().update(profileOpt.get())) {
                return profileDtoFactory.newDto(profileOpt.get());
            }
        }
        return null;
    }

    @Override
    public String findPropertyValue(String key) throws Exception {
        return propertyService.getPropertyValue("properties/config.properties", key);
    }

    @Override
    public ProfileDto cloneSelectedProfile(String profileName, String newProfileName) throws SQLException {
        Optional<Profile> profileToBeClonedOpt = ProfileDao.getInstance().findByName(profileName);

        if (profileToBeClonedOpt.isPresent()) {
            Profile newProfile = new Profile(
                    newProfileName,
                    profileToBeClonedOpt.get().getLanguage(),
                    profileToBeClonedOpt.get().getGametype(),
                    profileToBeClonedOpt.get().getMap(),
                    profileToBeClonedOpt.get().getDifficulty(),
                    profileToBeClonedOpt.get().getLength(),
                    profileToBeClonedOpt.get().getMaxPlayers(),
                    profileToBeClonedOpt.get().getServerName(),
                    profileToBeClonedOpt.get().getServerPassword(),
                    profileToBeClonedOpt.get().getWebPage(),
                    profileToBeClonedOpt.get().getWebPassword(),
                    profileToBeClonedOpt.get().getWebPort(),
                    profileToBeClonedOpt.get().getGamePort(),
                    profileToBeClonedOpt.get().getQueryPort(),
                    profileToBeClonedOpt.get().getYourClan(),
                    profileToBeClonedOpt.get().getYourWebLink(),
                    profileToBeClonedOpt.get().getUrlImageServer(),
                    profileToBeClonedOpt.get().getWelcomeMessage(),
                    profileToBeClonedOpt.get().getCustomParameters(),
                    new ArrayList<Map>(profileToBeClonedOpt.get().getMapList())
            );
            ProfileDao.getInstance().insert(newProfile);
            return profileDtoFactory.newDto(newProfile);
        } else {
            return null;
        }
    }

    @Override
    public void exportProfilesToFile(List<ProfileDto> profiles, File file) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("exported.profiles.number", String.valueOf(profiles.size()));
        int profileIndex = 1;
        for (ProfileDto profile: profiles) {
            properties.setProperty("exported.profile" + profileIndex + ".name", profile.getName());
            properties.setProperty("exported.profile" + profileIndex + ".language", profile.getLanguage().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".gameType", profile.getGametype().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".map", profile.getMap().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".difficulty", profile.getDifficulty().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".length", profile.getLength().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".maxPlayers", profile.getMaxPlayers().getKey());
            properties.setProperty("exported.profile" + profileIndex + ".serverName", profile.getServerName());
            properties.setProperty("exported.profile" + profileIndex + ".serverPassword", StringUtils.isNotBlank(profile.getServerPassword())? profile.getServerPassword(): "");
            properties.setProperty("exported.profile" + profileIndex + ".webPage", profile.getWebPage()!=null? String.valueOf(profile.getWebPage()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".webPassword", StringUtils.isNotBlank(profile.getWebPassword())? profile.getWebPassword(): "");
            properties.setProperty("exported.profile" + profileIndex + ".webPort", profile.getWebPort()!=null? String.valueOf(profile.getWebPort()): "");
            properties.setProperty("exported.profile" + profileIndex + ".gamePort", profile.getGamePort()!=null?String.valueOf(profile.getGamePort()): "");
            properties.setProperty("exported.profile" + profileIndex + ".queryPort", profile.getQueryPort()!=null?String.valueOf(profile.getQueryPort()): "");
            properties.setProperty("exported.profile" + profileIndex + ".yourClan", StringUtils.isNotBlank(profile.getYourClan())? profile.getYourClan(): "");
            properties.setProperty("exported.profile" + profileIndex + ".yourWebLink", StringUtils.isNotBlank(profile.getYourWebLink())? profile.getYourWebLink():"");
            properties.setProperty("exported.profile" + profileIndex + ".urlImageServer", StringUtils.isNotBlank(profile.getUrlImageServer())? profile.getUrlImageServer(): "");
            properties.setProperty("exported.profile" + profileIndex + ".welcomeMessage", StringUtils.isNotBlank(profile.getWelcomeMessage())? profile.getWelcomeMessage(): "");
            properties.setProperty("exported.profile" + profileIndex + ".customParameters", StringUtils.isNotBlank(profile.getCustomParameters())? profile.getCustomParameters(): "");
            properties.setProperty("exported.profile" + profileIndex + ".mapListSize", profile.getMapList()!=null? String.valueOf(profile.getMapList().size()): "0");
            if (profile.getMapList() != null && !profile.getMapList().isEmpty()) {
                int mapIndex = 1;
                for (MapDto map: profile.getMapList()) {
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".name", map.getKey());
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".official", String.valueOf(map.isOfficial()));
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".downloaded", String.valueOf(map.isDownloaded()));
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlInfo", StringUtils.isNotBlank(map.getUrlInfo())? map.getUrlInfo(): "");
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".idWorkShop", map.getIdWorkShop()!=null? String.valueOf(map.getIdWorkShop()): "");
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlPhoto", StringUtils.isNotBlank(map.getUrlPhoto())? map.getUrlPhoto(): "");
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".mod", map.getMod()!=null? String.valueOf(map.getMod()): "");
                    mapIndex++;
                }
            }
            profileIndex ++;
        }

        List<Language> languageList = LanguageDao.getInstance().listAll();
        exportGameTypesToFile(properties, languageList);
        exportDifficultiesToFile(properties, languageList);
        exportLengthsToFile(properties, languageList);
        exportMaxPlayersToFile(properties, languageList);

        propertyService.savePropertiesToFile(properties, file);
    }

    private void exportGameTypesToFile(Properties properties, List<Language> languageList) throws Exception {
        List<GameType> gameTypeList = GameTypeDao.getInstance().listAll();
        properties.setProperty("exported.gameTypes.number", String.valueOf(gameTypeList.size()));
        int gameTypeIndex = 1;

        for (GameType gameType: gameTypeList) {
            properties.setProperty("exported.gameType" + gameTypeIndex + ".code", gameType.getCode());
            properties.setProperty("exported.gameType" + gameTypeIndex + ".difficultyEnabled", String.valueOf(gameType.isDifficultyEnabled()));
            properties.setProperty("exported.gameType" + gameTypeIndex + ".lengthEnabled", String.valueOf(gameType.isLengthEnabled()));
            for (Language language: languageList) {
                String description = propertyService.getPropertyValue("properties/languages/" + language.getCode() + ".properties",
                        "prop.gametype." + gameType.getCode());

                properties.setProperty("exported.gameType" + gameTypeIndex + ".description." + language.getCode(), StringUtils.isNotBlank(description)? description: "");
            }
            gameTypeIndex++;
        }
    }

    private void exportDifficultiesToFile(Properties properties, List<Language> languageList) throws Exception {
        List<Difficulty> difficultyList = DifficultyDao.getInstance().listAll();
        properties.setProperty("exported.difficulties.number", String.valueOf(difficultyList.size()));
        int difficultyIndex = 1;

        for (Difficulty difficulty: difficultyList) {
            properties.setProperty("exported.difficulty" + difficultyIndex + ".code", difficulty.getCode());
            for (Language language: languageList) {
                String description = propertyService.getPropertyValue("properties/languages/" + language.getCode() + ".properties",
                        "prop.difficulty." + difficulty.getCode());

                properties.setProperty("exported.difficulty" + difficultyIndex + ".description." + language.getCode(), StringUtils.isNotBlank(description)? description: "");
            }
            difficultyIndex++;
        }
    }

    private void exportLengthsToFile(Properties properties, List<Language> languageList) throws Exception {
        List<Length> lengthList = LengthDao.getInstance().listAll();
        properties.setProperty("exported.lengths.number", String.valueOf(lengthList.size()));
        int lengthIndex = 1;

        for (Length length: lengthList) {
            properties.setProperty("exported.length" + lengthIndex + ".code", length.getCode());
            for (Language language: languageList) {
                String description = propertyService.getPropertyValue("properties/languages/" + language.getCode() + ".properties",
                        "prop.length." + length.getCode());

                properties.setProperty("exported.length" + lengthIndex + ".description." + language.getCode(), StringUtils.isNotBlank(description)? description: "");
            }
            lengthIndex++;
        }
    }

    private void exportMaxPlayersToFile(Properties properties, List<Language> languageList) throws Exception {
        List<MaxPlayers> maxPlayersList = MaxPlayersDao.getInstance().listAll();
        properties.setProperty("exported.maxPlayers.number", String.valueOf(maxPlayersList.size()));
        int maxPlayersIndex = 1;

        for (MaxPlayers maxPlayers: maxPlayersList) {
            properties.setProperty("exported.maxPlayers" + maxPlayersIndex + ".code", maxPlayers.getCode());
            for (Language language: languageList) {
                String description = propertyService.getPropertyValue("properties/languages/" + language.getCode() + ".properties",
                        "prop.maxplayers." + maxPlayers.getCode());

                properties.setProperty("exported.maxPlayers" + maxPlayersIndex + ".description." + language.getCode(), StringUtils.isNotBlank(description)? description: "");
            }
            maxPlayersIndex++;
        }
    }

    private void importGameTypesFromFile(Properties properties, List<Language> languageList) throws SQLException {
        List<GameType> gameTypeListInDataBase = GameTypeDao.getInstance().listAll();
        int size = Integer.valueOf(properties.getProperty("exported.gameTypes.number"));

        for (int index = 1; index <= size; index++) {
            String code = properties.getProperty("exported.gameType" + index + ".code");

            try {
                Optional<GameType> gameTypeOpt = gameTypeListInDataBase.stream().filter(gt -> gt.getCode().equals(code)).findFirst();
                if (gameTypeOpt.isPresent()) {
                    logger.info("The game type " + gameTypeOpt.get().getCode() + " is already in database. It can not be imported!");
                    continue;
                }

                for (Language language: languageList) {
                    String description = properties.getProperty("exported.gameType" + index + ".description." + language.getCode());
                    propertyService.setProperty("properties/languages/" + language.getCode() + ".properties",
                            "prop.gametype." + code, description);
                }

                GameType newGameType = new GameType(code);
                newGameType.setDifficultyEnabled(Boolean.parseBoolean(properties.getProperty("exported.gameType" + index + ".difficultyEnabled")));
                newGameType.setLengthEnabled(Boolean.parseBoolean(properties.getProperty("exported.gameType" + index + ".lengthEnabled")));
                GameTypeDao.getInstance().insert(newGameType);

            } catch (Exception e) {
                logger.error("Error saving the Game Type " + code + " to database.");
            }
        }
    }

    private void importDifficultiesFromFile(Properties properties, List<Language> languageList) throws SQLException {
        List<Difficulty> difficultyListInDataBase = DifficultyDao.getInstance().listAll();
        int size = Integer.valueOf(properties.getProperty("exported.difficulties.number"));

        for (int index = 1; index <= size; index++) {
            String code = properties.getProperty("exported.difficulty" + index + ".code");

            try {
                Optional<Difficulty> difficultyOpt = difficultyListInDataBase.stream().filter(gt -> gt.getCode().equals(code)).findFirst();
                if (difficultyOpt.isPresent()) {
                    logger.info("The difficulty " + difficultyOpt.get().getCode() + " is already in database. It can not be imported!");
                    continue;
                }

                for (Language language: languageList) {
                    String description = properties.getProperty("exported.difficulty" + index + ".description." + language.getCode());
                    propertyService.setProperty("properties/languages/" + language.getCode() + ".properties",
                            "prop.difficulty." + code, description);
                }

                Difficulty newDifficulty = new Difficulty(code);
                DifficultyDao.getInstance().insert(newDifficulty);

            } catch (Exception e) {
                logger.error("Error saving the Difficulty " + code + " to database.");
            }
        }
    }


    private void importLengthsFromFile(Properties properties, List<Language> languageList) throws SQLException {
        List<Length> lengthListInDataBase = LengthDao.getInstance().listAll();
        int size = Integer.valueOf(properties.getProperty("exported.lengths.number"));

        for (int index = 1; index <= size; index++) {
            String code = properties.getProperty("exported.length" + index + ".code");

            try {
                Optional<Length> lengthOpt = lengthListInDataBase.stream().filter(gt -> gt.getCode().equals(code)).findFirst();
                if (lengthOpt.isPresent()) {
                    logger.info("The length " + lengthOpt.get().getCode() + " is already in database. It can not be imported!");
                    continue;
                }

                for (Language language: languageList) {
                    String description = properties.getProperty("exported.length" + index + ".description." + language.getCode());
                    propertyService.setProperty("properties/languages/" + language.getCode() + ".properties",
                            "prop.length." + code, description);
                }

                Length newLength = new Length(code);
                LengthDao.getInstance().insert(newLength);

            } catch (Exception e) {
                logger.error("Error saving the Length " + code + " to database.");
            }
        }
    }

    private void importMaxPlayersFromFile(Properties properties, List<Language> languageList) throws SQLException {
        List<MaxPlayers> maxPlayersListInDataBase = MaxPlayersDao.getInstance().listAll();
        int size = Integer.valueOf(properties.getProperty("exported.maxPlayers.number"));

        for (int index = 1; index <= size; index++) {
            String code = properties.getProperty("exported.maxPlayers" + index + ".code");

            try {
                Optional<MaxPlayers> maxPlayersOpt = maxPlayersListInDataBase.stream().filter(gt -> gt.getCode().equals(code)).findFirst();
                if (maxPlayersOpt.isPresent()) {
                    logger.info("The max.players with code " + maxPlayersOpt.get().getCode() + " is already in database. It can not be imported!");
                    continue;
                }

                for (Language language: languageList) {
                    String description = properties.getProperty("exported.maxPlayers" + index + ".description." + language.getCode());
                    propertyService.setProperty("properties/languages/" + language.getCode() + ".properties",
                            "prop.maxplayers." + code, description);
                }

                MaxPlayers newMaxPlayers = new MaxPlayers(code);
                MaxPlayersDao.getInstance().insert(newMaxPlayers);

            } catch (Exception e) {
                logger.error("Error saving the Max.Players code " + code + " to database.");
            }
        }
    }

    @Override
    public ObservableList<ProfileDto> addProfilesToBeImportedFromFile(File file, String message, StringBuffer errorMessage, String installationFolder) throws Exception {
        Properties properties = propertyService.loadPropertiesFromFile(file);
        List<Language> languageList = LanguageDao.getInstance().listAll();

        importGameTypesFromFile(properties, languageList);
        importDifficultiesFromFile(properties, languageList);
        importLengthsFromFile(properties, languageList);
        importMaxPlayersFromFile(properties, languageList);

        int numberOfProfiles = Integer.parseInt(properties.getProperty("exported.profiles.number"));
        List<Profile> profileToBeImportedList = new ArrayList<Profile>();

        for (int profileIndex = 1; profileIndex <= numberOfProfiles; profileIndex++) {
            Optional<Language> languageOpt = LanguageDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".language"));
            Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".gameType"));
            Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".difficulty"));
            Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".length"));
            Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".maxPlayers"));

            Profile profileToBeImported = new Profile(
                    properties.getProperty("exported.profile" + profileIndex + ".name"),
                    languageOpt.isPresent()? languageOpt.get(): null,
                    gameTypeOpt.isPresent()? gameTypeOpt.get(): null,
                    null,
                    difficultyOpt.isPresent()? difficultyOpt.get(): null,
                    lengthOpt.isPresent()? lengthOpt.get(): null,
                    maxPlayersOpt.isPresent()? maxPlayersOpt.get(): null,
                    properties.getProperty("exported.profile" + profileIndex + ".serverName"),
                    properties.getProperty("exported.profile" + profileIndex + ".serverPassword"),
                    Boolean.parseBoolean(properties.getProperty("exported.profile" + profileIndex + ".webPage")),
                    properties.getProperty("exported.profile" + profileIndex + ".webPassword"),
                    Integer.parseInt(properties.getProperty("exported.profile" + profileIndex + ".webPort")),
                    Integer.parseInt(properties.getProperty("exported.profile" + profileIndex + ".gamePort")),
                    Integer.parseInt(properties.getProperty("exported.profile" + profileIndex + ".queryPort")),
                    properties.getProperty("exported.profile" + profileIndex + ".yourClan"),
                    properties.getProperty("exported.profile" + profileIndex + ".yourWebLink"),
                    properties.getProperty("exported.profile" + profileIndex + ".urlImageServer"),
                    properties.getProperty("exported.profile" + profileIndex + ".welcomeMessage"),
                    properties.getProperty("exported.profile" + profileIndex + ".customParameters"),
                    new ArrayList<Map>()
            );

            int numberOfMaps = Integer.parseInt(properties.getProperty("exported.profile" + profileIndex + ".mapListSize"));

            for (int mapIndex = 1; mapIndex <= numberOfMaps; mapIndex++) {
                String mapName = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".name");
                boolean official = Boolean.parseBoolean(properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".official"));
                Optional<Map> mapInDataBaseOpt;
                Long idWorkShop = null;
                if (official) {
                    mapInDataBaseOpt = MapDao.getInstance().findByCode(mapName);
                } else {
                    String strIdWorkShop = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".idWorkShop");
                    idWorkShop = StringUtils.isNotBlank(strIdWorkShop)? Long.parseLong(strIdWorkShop): 0;
                    mapInDataBaseOpt = MapDao.getInstance().findByIdWorkShop(idWorkShop);
                }
                if (mapInDataBaseOpt.isPresent()) {
                    mapInDataBaseOpt.get().getProfileList().add(profileToBeImported);
                    profileToBeImported.getMapList().add(mapInDataBaseOpt.get());
                } else {
                    boolean downloaded = Boolean.parseBoolean(properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".downloaded"));
                    String urlInfo = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlInfo");
                    String urlPhoto = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlPhoto");
                    String strMod = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".mod");
                    Boolean mod = StringUtils.isNotBlank(strMod)? Boolean.parseBoolean(strMod): null;
                    List<Profile> profileList = new ArrayList<Profile>();
                    profileList.add(profileToBeImported);
                    Map mapToImport = new Map(mapName, official, urlInfo, idWorkShop, urlPhoto, downloaded, mod, profileList);
                    profileToBeImported.getMapList().add(mapToImport);
                }
            }

            String mapName = properties.getProperty("exported.profile" + profileIndex + ".map");
            Optional<Map> mapOpt = profileToBeImported.getMapList().stream()
                    .filter(m -> m.getCode().equalsIgnoreCase(mapName) && m.isOfficial()).findFirst();
            Optional<Map> firstOfficialMap = profileToBeImported.getMapList().stream()
                    .filter(m -> m.isOfficial()).findFirst();
            profileToBeImported.setMap(mapOpt.isPresent()? mapOpt.get(): firstOfficialMap.isPresent()? firstOfficialMap.get(): null);

            profileToBeImportedList.add(profileToBeImported);
        }

        List<Profile> selectedProfiles = Utils.selectProfilesDialog(message + ":", profileToBeImportedList, profileToBeImportedList);
        List<Profile> insertedProfiles = new ArrayList<Profile>();
        if (selectedProfiles != null & !selectedProfiles.isEmpty()) {
            insertedProfiles = insertProfiles(selectedProfiles, installationFolder);
        }

        if (insertedProfiles.size() < selectedProfiles.size()) {
            for (Profile selectedProfile: selectedProfiles) {
                if (!insertedProfiles.contains(selectedProfile)) {
                    errorMessage.append(selectedProfile.getName() + "\n");
                }
            }
        }

        return profileDtoFactory.newDtos(insertedProfiles);
    }


    private List<Profile> insertProfiles(List<Profile> profileList, String installationFolder) {

        List<Profile> insertedProfileList = new ArrayList<Profile>();
        for (Profile profile: profileList) {
            try {
                Optional<Profile> profileOpt = ProfileDao.getInstance().findByName(profile.getName());
                if (profileOpt.isPresent()) {
                    logger.error("The profile " + profile.getName() + " is already in database. It could not be imported from file");
                } else {
                    for (Map map: profile.getMapList()) {
                        try {
                            Optional<Map> mapInDataBase;
                            if (map.isOfficial()) {
                                mapInDataBase = MapDao.getInstance().findByCode(map.getCode());
                            } else {
                                 mapInDataBase = MapDao.getInstance().findByIdWorkShop(map.getIdWorkShop());
                            }

                            if (mapInDataBase.isPresent()) {
                                MapDao.getInstance().update(map);
                            } else {
                                if (!createNewCustomMapFromWorkshop(map, installationFolder)) {
                                    logger.error("Error importing the map " + map.getCode() + " to database");
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Error importing the map " + map.getCode() + " to database", e);
                        }
                    }

                    Profile insertedProfile = ProfileDao.getInstance().insert(profile);
                    if (insertedProfile != null) {
                        insertedProfileList.add(profile);
                    } else {
                        logger.error("The profile " + profile.getName() + " could not be imported from file");
                    }
                }
            } catch (SQLException e) {
                logger.error("The profile " + profile.getName() + " could not be imported from file", e);
            }
        }
        return insertedProfileList;
    }

    private boolean createNewCustomMapFromWorkshop(Map map, String installationFolder) throws Exception {

        URL urlWorkShop = new URL(map.getUrlInfo());
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlWorkShop.openStream()));
        String strUrlMapImage = null;
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("image_src")) {
                String[] array = line.split("\"");
                strUrlMapImage = array[3];
                break;
            }
        }
        reader.close();
        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");
        String absoluteTargetFolder = installationFolder + customMapLocalFolder;
        Utils.downloadImageFromUrlToFile(strUrlMapImage, absoluteTargetFolder, map.getCode());
        map.setMod(null);
        map.setDownloaded(false);
        Map insertedMap = MapDao.getInstance().insert(map);
        return (insertedMap != null);
    }

    @Override
    public List<ProfileDto> selectProfiles(String message) throws SQLException {
        List<Profile> allProfiles = ProfileDao.getInstance().listAll();
        List<Profile> selectedProfiles = Utils.selectProfilesDialog(message + ":", allProfiles, allProfiles);
        return profileDtoFactory.newDtos(selectedProfiles);
    }
}
