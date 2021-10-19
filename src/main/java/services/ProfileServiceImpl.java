package services;

import daos.*;
import entities.*;
import entities.AbstractMap;
import javafx.scene.control.ButtonType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.MapToDisplay;
import pojos.ProfileToDisplay;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LogManager.getLogger(ProfileServiceImpl.class);
    private final PropertyService propertyService;
    private final AbstractMapService officialMapService;
    private final AbstractMapService customMapModService;

    public ProfileServiceImpl() {
        super();
        this.propertyService = new PropertyServiceImpl();
        this.officialMapService = new OfficialMapServiceImpl();
        this.customMapModService = new CustomMapModServiceImpl();
    }

    @Override
    public Optional<Profile> findProfileByCode(String profileName) throws SQLException {
        Optional<Profile> profileOpt = ProfileDao.getInstance().findByCode(profileName);
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());

        if (profileOpt.isPresent()) {
            profileOpt.get().getMapList().forEach(m -> {
                if (idsMapasOficiales.contains(m.getId())) {
                    m.setOfficial(true);
                } else {
                    m.setOfficial(false);
                }
            });
        }
        return profileOpt;
    }

    @Override
    public List<Profile> listAllProfiles() throws SQLException {
        List<Profile> profiles = ProfileDao.getInstance().listAll();
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());

        profiles.stream().forEach(p -> {
            p.getMapList().forEach(m -> {
                if (idsMapasOficiales.contains(m.getId())) {
                    m.setOfficial(true);
                } else {
                    m.setOfficial(false);
                }
            });
        });
        return profiles;
    }

    @Override
    public Profile createItem(Profile profile) throws Exception {
        List<Integer> idsMapasOficiales = OfficialMapDao.getInstance().listAll().stream().map(OfficialMap::getId).collect(Collectors.toList());
        profile.getMapList().forEach(m -> {
            if (idsMapasOficiales.contains(m.getId())) {
                m.setOfficial(true);
            } else {
                m.setOfficial(false);
            }
        });
        return ProfileDao.getInstance().insert(profile);
    }

    @Override
    public boolean updateItemCode(Profile profile, String oldCode) throws Exception {
        return false;
    }

    @Override
    public void updateItemDescription(Profile entity) throws Exception {
    }

    @Override
    public boolean deleteItem(Profile profile) throws Exception {
        return ProfileDao.getInstance().remove(profile);
    }

    @Override
    public boolean deleteProfile(Profile profile, String installationFolder) throws Exception {
        List<AbstractMap> mapList = new ArrayList<AbstractMap>(profile.getMapList());
        profile.setMap(null);
        profile.getMapList().clear();
        ProfileDao.getInstance().update(profile);
        Optional<Profile> profileOpt = findProfileByCode(profile.getCode());
        for (AbstractMap map: mapList) {
            map.getProfileList().remove(profileOpt.get());
            if (map.isOfficial()) {
                OfficialMapDao.getInstance().update((OfficialMap) map);
            } else {
                CustomMapModDao.getInstance().update((CustomMapMod) map);
                if (map.getProfileList().isEmpty()) {
                    if (CustomMapModDao.getInstance().remove((CustomMapMod) map)) {
                        File photo = new File(installationFolder + map.getUrlPhoto());
                        photo.delete();
                        File cacheFolder = new File(installationFolder + "/KFGame/Cache/" + ((CustomMapMod) map).getIdWorkShop());
                        FileUtils.deleteDirectory(cacheFolder);
                    }
                }
            }
        }
        return deleteItem(profileOpt.get());
    }

    @Override
    public Profile cloneProfile(Profile profileToBeCloned, String newProfileName) throws Exception {
        Profile newProfile = new Profile(
                newProfileName,
                profileToBeCloned.getLanguage(),
                profileToBeCloned.getGametype(),
                profileToBeCloned.getMap(),
                profileToBeCloned.getDifficulty(),
                profileToBeCloned.getLength(),
                profileToBeCloned.getMaxPlayers(),
                profileToBeCloned.getServerName(),
                profileToBeCloned.getServerPassword(),
                profileToBeCloned.getWebPage(),
                profileToBeCloned.getWebPassword(),
                profileToBeCloned.getWebPort(),
                profileToBeCloned.getGamePort(),
                profileToBeCloned.getQueryPort(),
                profileToBeCloned.getYourClan(),
                profileToBeCloned.getYourWebLink(),
                profileToBeCloned.getUrlImageServer(),
                profileToBeCloned.getWelcomeMessage(),
                profileToBeCloned.getCustomParameters(),
                profileToBeCloned.getProfileMapList(),
                profileToBeCloned.getTakeover(),
                profileToBeCloned.getTeamCollision(),
                profileToBeCloned.getAdminCanPause(),
                profileToBeCloned.getAnnounceAdminLogin(),
                profileToBeCloned.getMapVoting(),
                profileToBeCloned.getMapVotingTime(),
                profileToBeCloned.getKickVoting(),
                profileToBeCloned.getKickPercentage(),
                profileToBeCloned.getPublicTextChat(),
                profileToBeCloned.getSpectatorsOnlyChatToOtherSpectators(),
                profileToBeCloned.getVoip(),
                profileToBeCloned.getChatLogging(),
                profileToBeCloned.getChatLoggingFile(),
                profileToBeCloned.getChatLoggingFileTimestamp(),
                profileToBeCloned.getTimeBetweenKicks(),
                profileToBeCloned.getMaxIdleTime(),
                profileToBeCloned.getDeadPlayersCanTalk(),
                profileToBeCloned.getReadyUpDelay(),
                profileToBeCloned.getGameStartDelay(),
                profileToBeCloned.getMaxSpectators(),
                profileToBeCloned.getMapObjetives(),
                profileToBeCloned.getPickupItems(),
                profileToBeCloned.getFriendlyFirePercentage()
        );
        return createItem(newProfile);
    }

    @Override
    public void exportProfilesToFile(List<Profile> profilesToExport, File file) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("exported.profiles.number", String.valueOf(profilesToExport.size()));
        int profileIndex = 1;
        for (Profile profile: profilesToExport) {
            properties.setProperty("exported.profile" + profileIndex + ".name", profile.getCode());
            properties.setProperty("exported.profile" + profileIndex + ".language", profile.getLanguage().getCode());
            properties.setProperty("exported.profile" + profileIndex + ".gameType", profile.getGametype().getCode());
            properties.setProperty("exported.profile" + profileIndex + ".map", profile.getMap().getCode());
            properties.setProperty("exported.profile" + profileIndex + ".difficulty", profile.getDifficulty().getCode());
            properties.setProperty("exported.profile" + profileIndex + ".length", profile.getLength().getCode());
            properties.setProperty("exported.profile" + profileIndex + ".maxPlayers", profile.getMaxPlayers().getCode());
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
            properties.setProperty("exported.profile" + profileIndex + ".takeover", profile.getTakeover()!=null? String.valueOf(profile.getTakeover()): "false");

            properties.setProperty("exported.profile" + profileIndex + ".mapVoting", profile.getMapVoting()? String.valueOf(profile.getMapVoting()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".mapVotingTime", profile.getMapVotingTime() != null? String.valueOf(profile.getMapVotingTime()): "");
            properties.setProperty("exported.profile" + profileIndex + ".kickVoting", profile.getKickVoting()? String.valueOf(profile.getKickVoting()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".kickPercentage", profile.getKickPercentage() != null? String.valueOf(profile.getKickPercentage()): "");
            properties.setProperty("exported.profile" + profileIndex + ".timeBetweenKicks", profile.getTimeBetweenKicks() != null? String.valueOf(profile.getTimeBetweenKicks()): "");
            properties.setProperty("exported.profile" + profileIndex + ".maxIdleTime", profile.getMaxIdleTime() != null? String.valueOf(profile.getMaxIdleTime()): "");
            properties.setProperty("exported.profile" + profileIndex + ".publicTextChat", profile.getPublicTextChat()? String.valueOf(profile.getPublicTextChat()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".spectatorsChat", profile.getSpectatorsOnlyChatToOtherSpectators()? String.valueOf(profile.getSpectatorsOnlyChatToOtherSpectators()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".voip", profile.getVoip()? String.valueOf(profile.getVoip()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".deadPlayersCanTalk", profile.getDeadPlayersCanTalk()? String.valueOf(profile.getDeadPlayersCanTalk()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".chatLogging", profile.getChatLogging()? String.valueOf(profile.getChatLogging()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".chatLoggingFile", StringUtils.isNotBlank(profile.getChatLoggingFile())? profile.getChatLoggingFile(): "");
            properties.setProperty("exported.profile" + profileIndex + ".chatLoggingFileTimestamp", profile.getChatLoggingFileTimestamp()? String.valueOf(profile.getChatLoggingFileTimestamp()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".teamCollision", profile.getTeamCollision()!=null? String.valueOf(profile.getTeamCollision()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".adminPause", profile.getAdminCanPause()!=null? String.valueOf(profile.getAdminCanPause()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".adminLogin", profile.getAnnounceAdminLogin()!=null? String.valueOf(profile.getAnnounceAdminLogin()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".readyUpDelay", profile.getReadyUpDelay() != null? String.valueOf(profile.getReadyUpDelay()): "");
            properties.setProperty("exported.profile" + profileIndex + ".gameStartDelay", profile.getGameStartDelay() != null? String.valueOf(profile.getGameStartDelay()): "");
            properties.setProperty("exported.profile" + profileIndex + ".maxSpectators", profile.getMaxSpectators() != null? String.valueOf(profile.getMaxSpectators()): "");
            properties.setProperty("exported.profile" + profileIndex + ".mapObjetives", profile.getMapObjetives()? String.valueOf(profile.getMapObjetives()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".pickupItems", profile.getPickupItems()? String.valueOf(profile.getPickupItems()): "false");
            properties.setProperty("exported.profile" + profileIndex + ".friendlyFirePercentage", profile.getFriendlyFirePercentage() != null? String.valueOf(profile.getFriendlyFirePercentage()): "");

            if (profile.getMapList() != null && !profile.getMapList().isEmpty()) {
                int mapIndex = 1;
                for (AbstractMap map: profile.getMapList()) {
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".name", map.getCode());
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlInfo", StringUtils.isNotBlank(map.getUrlInfo())? map.getUrlInfo(): "");
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlPhoto", StringUtils.isNotBlank(map.getUrlPhoto())? map.getUrlPhoto(): "");
                    properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".official", String.valueOf(map.isOfficial()));
                    if (!map.isOfficial()) {
                        properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".idWorkShop", ((CustomMapMod) map).getIdWorkShop()!=null? String.valueOf(((CustomMapMod) map).getIdWorkShop()): "");
                        properties.setProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".downloaded", String.valueOf(((CustomMapMod) map).isDownloaded()));
                    }

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

    @Override
    public List<Profile> importProfilesFromFile(File file, String message, StringBuffer errorMessage) throws Exception {
        Properties properties = propertyService.loadPropertiesFromFile(file);

        List<Language> languageList = LanguageDao.getInstance().listAll();

        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.message.proceed");
        String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.message.importItems");

        String gameTypesText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.menu.configuration.gameTypes");
        String difficultiesText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.menu.configuration.difficulties");
        String lengthText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.menu.configuration.length");
        String maxPlayersText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties",
                "prop.menu.configuration.maxPlayers");

        Optional<ButtonType> result = Utils.questionDialog(headerText, contentText + ":\n\n" + gameTypesText + "\n" + difficultiesText + "\n" + lengthText + "\n" + maxPlayersText);
        if (result.isPresent() && result.get().equals(ButtonType.CANCEL)) {
            return null;
        }

        importGameTypesFromFile(properties, languageList);
        importDifficultiesFromFile(properties, languageList);
        importLengthsFromFile(properties, languageList);
        importMaxPlayersFromFile(properties, languageList);

        List<Profile> selectedProfileList = selectProfilesToBeImported(properties, message);

        List<Profile> savedProfileList = new ArrayList<Profile>();
        if (selectedProfileList != null & !selectedProfileList.isEmpty()) {
            savedProfileList = saveProfilesToDatabase(selectedProfileList, properties);

            if (savedProfileList.size() < selectedProfileList.size()) {
                List<String> selectedProfileNameList = selectedProfileList.stream().map(p -> p.getCode()).collect(Collectors.toList());
                List<String> savedProfileNameList = savedProfileList.stream().map(profile -> profile.getCode()).collect(Collectors.toList());

                for (String selectedProfileName : selectedProfileNameList) {
                    if (!savedProfileNameList.contains(selectedProfileName)) {
                        errorMessage.append(selectedProfileName + "\n");
                    }
                }
            }
        }
        return savedProfileList;
    }

    private void importGameTypesFromFile(Properties properties, List<Language> languageList) throws SQLException {
        List<GameType> gameTypeListInDataBase = GameTypeDao.getInstance().listAll();
        String strSize = properties.getProperty("exported.gameTypes.number");
        int size = StringUtils.isNotBlank(strSize) ? Integer.valueOf(strSize): 0;

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
        String strSize = properties.getProperty("exported.difficulties.number");
        int size = StringUtils.isNotBlank(strSize) ? Integer.valueOf(strSize): 0;

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
        String strSize = properties.getProperty("exported.lengths.number");
        int size = StringUtils.isNotBlank(strSize) ? Integer.valueOf(strSize): 0;

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
        String strSize = properties.getProperty("exported.maxPlayers.number");
        int size = StringUtils.isNotBlank(strSize) ? Integer.valueOf(strSize): 0;

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

    private List<Profile> selectProfilesToBeImported(Properties properties, String message) throws Exception {
        int numberOfProfiles = Integer.parseInt(properties.getProperty("exported.profiles.number"));
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        List<ProfileToDisplay> profileToDisplayList = new ArrayList<ProfileToDisplay>();

        for (int profileIndex = 1; profileIndex <= numberOfProfiles; profileIndex++) {
            String profileName = properties.getProperty("exported.profile" + profileIndex + ".name");
            try {
                String gameTypeCode = properties.getProperty("exported.profile" + profileIndex + ".gameType");
                String gameTypeDescription = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.gametype." + gameTypeCode);
                String difficultyCode = properties.getProperty("exported.profile" + profileIndex + ".difficulty");
                String difficultyDescription = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.difficulty." + difficultyCode);
                String lengthCode = properties.getProperty("exported.profile" + profileIndex + ".length");
                String lengthDescription = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.length." + lengthCode);
                String mapName = properties.getProperty("exported.profile" + profileIndex + ".map");

                ProfileToDisplay profileToDisplay = new ProfileToDisplay(profileIndex, profileName, gameTypeDescription, mapName, difficultyDescription, lengthDescription);
                profileToDisplay.setSelected(true);
                profileToDisplayList.add(profileToDisplay);
            } catch (Exception e) {
                logger.error("Error reading the profile " + profileName + " from exported file", e);
            }
        }
        return Utils.selectProfilesDialog(message + ":", profileToDisplayList)
                .stream()
                .map(ptd -> getProfileFromFile(ptd.getProfileFileIndex(), properties))
                .collect(Collectors.toList());
    }


    private Profile getProfileFromFile(int profileIndex, Properties properties) {
        String profileName = properties.getProperty("exported.profile" + profileIndex + ".name");
        try {
            Optional<Language> languageOpt = LanguageDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".language"));
            Optional<GameType> gameTypeOpt = GameTypeDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".gameType"));
            Optional<Difficulty> difficultyOpt = DifficultyDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".difficulty"));
            Optional<Length> lengthOpt = LengthDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".length"));
            Optional<MaxPlayers> maxPlayersOpt = MaxPlayersDao.getInstance().findByCode(properties.getProperty("exported.profile" + profileIndex + ".maxPlayers"));
            String webPortStr = properties.getProperty("exported.profile" + profileIndex + ".webPort");
            String gamePortStr = properties.getProperty("exported.profile" + profileIndex + ".gamePort");
            String queryPortStr = properties.getProperty("exported.profile" + profileIndex + ".queryPort");
            String mapVotingTimeStr = properties.getProperty("exported.profile" + profileIndex + ".mapVotingTime");
            String kickPercentageStr = properties.getProperty("exported.profile" + profileIndex + ".kickPercentage");
            String timeBetweenKicksStr = properties.getProperty("exported.profile" + profileIndex + ".timeBetweenKicks");
            String maxIdleTimeStr = properties.getProperty("exported.profile" + profileIndex + ".maxIdleTime");
            String readyUpDelayStr = properties.getProperty("exported.profile" + profileIndex + ".readyUpDelay");
            String gameStartDelayStr = properties.getProperty("exported.profile" + profileIndex + ".gameStartDelay");
            String maxSpectatorsStr = properties.getProperty("exported.profile" + profileIndex + ".maxSpectators");
            String friendlyFirePercentageStr = properties.getProperty("exported.profile" + profileIndex + ".friendlyFirePercentage");
            String webPageStr = properties.getProperty("exported.profile" + profileIndex + ".webPage");
            String takeoverStr = properties.getProperty("exported.profile" + profileIndex + ".takeover");
            String teamCollisionStr = properties.getProperty("exported.profile" + profileIndex + ".teamCollision");
            String adminPauseStr = properties.getProperty("exported.profile" + profileIndex + ".adminPause");
            String adminLoginStr = properties.getProperty("exported.profile" + profileIndex + ".adminLogin");
            String mapVotingStr = properties.getProperty("exported.profile" + profileIndex + ".mapVoting");
            String kickVotingStr = properties.getProperty("exported.profile" + profileIndex + ".kickVoting");
            String publicTextChatStr = properties.getProperty("exported.profile" + profileIndex + ".publicTextChat");
            String spectatorsChatStr = properties.getProperty("exported.profile" + profileIndex + ".spectatorsChat");
            String voipStr = properties.getProperty("exported.profile" + profileIndex + ".voip");
            String chatLoggingStr = properties.getProperty("exported.profile" + profileIndex + ".chatLogging");
            String chatLoggingFileTimestampStr = properties.getProperty("exported.profile" + profileIndex + ".chatLoggingFileTimestamp");
            String deadPlayersCanTalkStr = properties.getProperty("exported.profile" + profileIndex + ".deadPlayersCanTalk");
            String mapObjetivesStr = properties.getProperty("exported.profile" + profileIndex + ".mapObjetives");
            String pickupItemsStr = properties.getProperty("exported.profile" + profileIndex + ".pickupItems");

            return new Profile(
                    profileName,
                    languageOpt.isPresent() ? languageOpt.get() : null,
                    gameTypeOpt.isPresent() ? gameTypeOpt.get() : null,
                    null,
                    difficultyOpt.isPresent() ? difficultyOpt.get() : null,
                    lengthOpt.isPresent() ? lengthOpt.get() : null,
                    maxPlayersOpt.isPresent() ? maxPlayersOpt.get() : null,
                    properties.getProperty("exported.profile" + profileIndex + ".serverName"),
                    properties.getProperty("exported.profile" + profileIndex + ".serverPassword"),
                    StringUtils.isNotBlank(webPageStr) ? Boolean.parseBoolean(webPageStr): true,
                    properties.getProperty("exported.profile" + profileIndex + ".webPassword"),
                    StringUtils.isNotBlank(webPortStr) ? Integer.parseInt(webPortStr): 8080,
                    StringUtils.isNotBlank(gamePortStr) ? Integer.parseInt(gamePortStr): 7777,
                    StringUtils.isNotBlank(queryPortStr) ? Integer.parseInt(queryPortStr): 27015,
                    properties.getProperty("exported.profile" + profileIndex + ".yourClan"),
                    properties.getProperty("exported.profile" + profileIndex + ".yourWebLink"),
                    properties.getProperty("exported.profile" + profileIndex + ".urlImageServer"),
                    properties.getProperty("exported.profile" + profileIndex + ".welcomeMessage"),
                    properties.getProperty("exported.profile" + profileIndex + ".customParameters"),
                    new ArrayList<ProfileMap>(),
                    StringUtils.isNotBlank(takeoverStr) ? Boolean.parseBoolean(takeoverStr): false,
                    StringUtils.isNotBlank(teamCollisionStr) ? Boolean.parseBoolean(teamCollisionStr): true,
                    StringUtils.isNotBlank(adminPauseStr) ? Boolean.parseBoolean(adminPauseStr): false,
                    StringUtils.isNotBlank(adminLoginStr) ? Boolean.parseBoolean(adminLoginStr): true,
                    StringUtils.isNotBlank(mapVotingStr) ? Boolean.parseBoolean(mapVotingStr): true,
                    StringUtils.isNotBlank(mapVotingTimeStr) ? Double.parseDouble(mapVotingTimeStr): 60,
                    StringUtils.isNotBlank(kickVotingStr) ? Boolean.parseBoolean(kickVotingStr): true,
                    StringUtils.isNotBlank(kickPercentageStr) ? Double.parseDouble(kickPercentageStr): 0.66,
                    StringUtils.isNotBlank(publicTextChatStr) ? Boolean.parseBoolean(publicTextChatStr): true,
                    StringUtils.isNotBlank(spectatorsChatStr) ? Boolean.parseBoolean(spectatorsChatStr): false,
                    StringUtils.isNotBlank(voipStr) ? Boolean.parseBoolean(voipStr): true,
                    StringUtils.isNotBlank(chatLoggingStr) ? Boolean.parseBoolean(chatLoggingStr): false,
                    properties.getProperty("exported.profile" + profileIndex + ".chatLoggingFile"),
                    StringUtils.isNotBlank(chatLoggingFileTimestampStr) ? Boolean.parseBoolean(chatLoggingFileTimestampStr): true,
                    StringUtils.isNotBlank(timeBetweenKicksStr) ? Double.parseDouble(timeBetweenKicksStr): 10,
                    StringUtils.isNotBlank(maxIdleTimeStr) ? Double.parseDouble(maxIdleTimeStr): 0,
                    StringUtils.isNotBlank(deadPlayersCanTalkStr) ? Boolean.parseBoolean(deadPlayersCanTalkStr): true,
                    StringUtils.isNotBlank(readyUpDelayStr) ? Integer.parseInt(readyUpDelayStr): 90,
                    StringUtils.isNotBlank(gameStartDelayStr) ? Integer.parseInt(gameStartDelayStr): 4,
                    StringUtils.isNotBlank(maxSpectatorsStr) ? Integer.parseInt(maxSpectatorsStr): 2,
                    StringUtils.isNotBlank(mapObjetivesStr) ? Boolean.parseBoolean(mapObjetivesStr): true,
                    StringUtils.isNotBlank(pickupItemsStr) ? Boolean.parseBoolean(pickupItemsStr): true,
                    StringUtils.isNotBlank(friendlyFirePercentageStr) ? Double.parseDouble(friendlyFirePercentageStr): 0
            );
        } catch (SQLException e) {
            logger.error("Error getting the profile " + profileName + " from file", e);
            return null;
        }
    }

    private List<Profile> saveProfilesToDatabase(List<Profile> selectedProfileList, Properties properties) {
        List<Profile> savedProfileList = new ArrayList<Profile>();

        int profileIndex = 1;
        for (Profile profile: selectedProfileList) {
            try {
                Profile savedProfile = (Profile) ProfileDao.getInstance().insert(profile);

                List<AbstractMap> mapList = importProfileMapsFromFile(profileIndex, savedProfile, properties);
                savedProfile.getMapList().addAll(mapList);

                String mapName = properties.getProperty("exported.profile" + profileIndex + ".map");
                Optional<AbstractMap> mapOpt = mapList.stream().filter(m -> m.getCode().equalsIgnoreCase(mapName)).findFirst();
                savedProfile.setMap(mapOpt.isPresent()? mapOpt.get(): null);

                ProfileDao.getInstance().update(savedProfile);

                savedProfileList.add(savedProfile);
                profileIndex++;
            } catch (Exception e) {
                logger.error("Error saving the profile " + profile.getCode() + " to database", e);
            }
        }
        return savedProfileList;
    }

    private List<AbstractMap> importProfileMapsFromFile(int profileIndex, Profile profile, Properties properties) throws Exception {
        List<AbstractMap> mapList = new ArrayList<AbstractMap>();
        java.util.Map<String,Integer> officialMapsIndex = new HashMap<String, Integer>();
        List<MapToDisplay> customMapListToDisplay = new ArrayList<MapToDisplay>();
        java.util.Map<Long,Integer> customMapsIndex = new HashMap<Long, Integer>();
        int numberOfMaps = Integer.parseInt(properties.getProperty("exported.profile" + profileIndex + ".mapListSize"));

        for (int mapIndex = 1; mapIndex <= numberOfMaps; mapIndex++) {
            String mapName = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".name");
            try {
                boolean official = Boolean.parseBoolean(properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".official"));
                if (official) {
                    officialMapsIndex.put(mapName, mapIndex);
                } else {
                    String strIdWorkShop = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".idWorkShop");
                    Long idWorkShop = StringUtils.isNotBlank(strIdWorkShop) ? Long.parseLong(strIdWorkShop) : 0;
                    customMapListToDisplay.add(new MapToDisplay(idWorkShop, mapName));
                    customMapsIndex.put(idWorkShop, mapIndex);
                }
            } catch (Exception e) {
                logger.error("Error importing the map " + mapName + " of profile index " + profileIndex + " from file", e);
            }
        }

        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectMapsModsForProfile");
        List<MapToDisplay> selectedCustomMapList = Utils.selectMapsDialog(headerText + " " + profile.getCode(), customMapListToDisplay);

        for (java.util.Map.Entry<String, Integer> entry : officialMapsIndex.entrySet()) {
            String mapName = entry.getKey();
            Integer mapIndex = entry.getValue();
            try {
                Optional mapInDataBaseOpt = officialMapService.findMapByCode(mapName);
                AbstractMap map = getImportedMap(mapInDataBaseOpt, profile, properties, profileIndex, mapIndex, mapName, null, true);
                if (map != null) {
                    mapList.add(map);
                }
            } catch (Exception e) {
                logger.error("Error importing the official map " + mapName + " of profile index " + profileIndex + " from file", e);
            }
        }

        for (MapToDisplay customMap: selectedCustomMapList) {
            Integer mapIndex = customMapsIndex.get(customMap.getIdWorkShop());
            try {
                Optional mapInDataBaseOpt = CustomMapModDao.getInstance().findByIdWorkShop(customMap.getIdWorkShop());
                AbstractMap map = getImportedMap(mapInDataBaseOpt, profile, properties, profileIndex, mapIndex, customMap.getCommentary(), customMap.getIdWorkShop(), false);
                if (map != null) {
                    mapList.add(map);
                }
            } catch (Exception e) {
                logger.error("Error importing the official map " + customMap.getCommentary() + " of profile index " + profileIndex + " from file", e);
            }
        }

        return mapList;
    }

    private AbstractMap getImportedMap(Optional<AbstractMap> mapInDataBaseOpt, Profile profile, Properties properties, int profileIndex, Integer mapIndex, String mapName, Long idWorkShop, boolean official) throws Exception {
        if (mapInDataBaseOpt.isPresent()) {
            mapInDataBaseOpt.get().getProfileList().add(profile);
            if (mapInDataBaseOpt.get().isOfficial()) {
                if (OfficialMapDao.getInstance().update((OfficialMap) mapInDataBaseOpt.get())) {
                    return mapInDataBaseOpt.get();
                }
            } else {
                if (CustomMapModDao.getInstance().update((CustomMapMod) mapInDataBaseOpt.get())) {
                    return mapInDataBaseOpt.get();
                }
            }
        } else {
            String urlInfo = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlInfo");
            String urlPhoto = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlPhoto");

            AbstractMap map = null;
            if (official) {
                map = new OfficialMap(mapName, urlInfo, urlPhoto, profile.getProfileMapList(), null);
                return officialMapService.createItem(map);
            } else {
                boolean downloaded = Boolean.parseBoolean(properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".downloaded"));
                map = new CustomMapMod(mapName, urlInfo, urlPhoto, profile.getProfileMapList(), idWorkShop, downloaded);
                if (createNewCustomMapFromWorkshop((CustomMapMod) map)) {
                    return map;
                }
            }
        }
        return null;
    }

    private boolean createNewCustomMapFromWorkshop(CustomMapMod map) throws Exception {
        String installationFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.installationFolder");
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
        Utils.downloadImageFromUrlToFile(strUrlMapImage, installationFolder + map.getUrlPhoto());
        map.setDownloaded(false);
        AbstractMap insertedMap = customMapModService.createItem(map);;
        return (insertedMap != null);
    }
}
