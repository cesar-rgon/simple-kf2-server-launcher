package pojos;

import daos.*;
import entities.*;
import pojos.enums.EnumPlatform;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public abstract class AbstractPopulateDatabase {

    private final PropertyService propertyService;

    protected AbstractPopulateDatabase() {
        super();
        this.propertyService = new PropertyServiceImpl();
    }

    public abstract void start() throws Exception;
    protected abstract void populateLanguages() throws SQLException;
    protected abstract void populateDifficulties() throws Exception;
    protected abstract void populateGameTypes() throws Exception;
    protected abstract void populateLengths() throws Exception;
    protected abstract void polulateMaximunPlayersList() throws Exception;
    protected abstract void populateOfficialMaps() throws Exception;
    protected abstract void populateProfiles() throws Exception;
    protected abstract void populatePlatforms() throws SQLException;

    protected void populateLanguage(String code, String description) throws SQLException {
        Language language = new Language(code, description);
        LanguageDao.getInstance().insert(language);
    }

    protected void populateDifficulty(String code, String englishDescription, String spanishDescription, String frenchDescription) throws Exception {
        Difficulty difficulty = new Difficulty(code);
        DifficultyDao.getInstance().insert(difficulty);
        propertyService.setProperty("properties/languages/en.properties", "prop.difficulty." + code, englishDescription);
        propertyService.setProperty("properties/languages/es.properties", "prop.difficulty." + code, spanishDescription);
        propertyService.setProperty("properties/languages/fr.properties", "prop.difficulty." + code, frenchDescription);
    }

    protected void populateGameType(String code, boolean difficultyEnabled, boolean lengthEnabled, String englishDescription, String spanishDescription, String frenchDescription) throws Exception {
        GameType gameType = new GameType(code);
        gameType.setDifficultyEnabled(difficultyEnabled);;
        gameType.setLengthEnabled(lengthEnabled);
        GameTypeDao.getInstance().insert(gameType);
        propertyService.setProperty("properties/languages/en.properties", "prop.gametype." + code, englishDescription);
        propertyService.setProperty("properties/languages/es.properties", "prop.gametype." + code, spanishDescription);
        propertyService.setProperty("properties/languages/fr.properties", "prop.gametype." + code, frenchDescription);
    }

    protected void populateLength(String code, String englishDescription, String spanishDescription, String frenchDescription) throws Exception {
        Length length = new Length(code);
        LengthDao.getInstance().insert(length);
        propertyService.setProperty("properties/languages/en.properties", "prop.length." + code, englishDescription);
        propertyService.setProperty("properties/languages/es.properties", "prop.length." + code, spanishDescription);
        propertyService.setProperty("properties/languages/fr.properties", "prop.length." + code, frenchDescription);
    }

    protected void polulateMaximunPlayers(String code, String englishDescription, String spanishDescription, String frenchDescription) throws Exception {
        MaxPlayers maxPlayers = new MaxPlayers(code);
        MaxPlayersDao.getInstance().insert(maxPlayers);
        propertyService.setProperty("properties/languages/en.properties", "prop.maxplayers." + code, englishDescription);
        propertyService.setProperty("properties/languages/es.properties", "prop.maxplayers." + code, spanishDescription);
        propertyService.setProperty("properties/languages/fr.properties", "prop.maxplayers." + code, frenchDescription);
    }

    protected void populateProfile(String name, Language language, GameType gametype, AbstractMap map, Difficulty difficulty, Length length, MaxPlayers maxPlayers,
                                   String serverName, String serverPassword, Boolean webPage, String webPassword, Integer webPort, Integer gamePort, Integer queryPort,
                                   String yourClan, String yourWebLink, String urlImageServer, String welcomeMessage, String customParameters, List<PlatformProfileMap> platformProfileMapList, Boolean takeover,
                                   Boolean teamCollision, Boolean adminCanPause, Boolean announceAdminLogin, Boolean mapVoting, Double mapVotingTime,
                                   Boolean kickVoting, Double kickPercentage, Boolean publicTextChat, Boolean spectatorsOnlyChatToOtherSpectators, Boolean voip,
                                   Boolean chatLogging, String chatLoggingFile, Boolean chatLoggingFileTimestamp, Double timeBetweenKicks, Double maxIdleTime, Boolean deadPlayersCanTalk,
                                   Integer readyUpDelay, Integer gameStartDelay, Integer maxSpectators, Boolean mapObjetives, Boolean pickupItems, Double friendlyFirePercentage) throws SQLException {

        Profile profile = new Profile(name, language, gametype, map, difficulty, length, maxPlayers,
                serverName, serverPassword, webPage, webPassword, webPort, gamePort, queryPort,
                yourClan, yourWebLink, urlImageServer, welcomeMessage, customParameters, platformProfileMapList, takeover,
                teamCollision, adminCanPause, announceAdminLogin, mapVoting, mapVotingTime,
                kickVoting, kickPercentage, publicTextChat, spectatorsOnlyChatToOtherSpectators, voip,
                chatLogging, chatLoggingFile, chatLoggingFileTimestamp, timeBetweenKicks, maxIdleTime, deadPlayersCanTalk,
                readyUpDelay, gameStartDelay, maxSpectators, mapObjetives, pickupItems, friendlyFirePercentage);

        ProfileDao.getInstance().insert(profile);
    }

    protected void populateOfficialMap(String code, String urlInfo, String urlPhoto, Date releaseDate) throws Exception {
        OfficialMap officialMap = new OfficialMap(code, urlInfo, urlPhoto, releaseDate);

        Optional<Profile> profileOptional = ProfileDao.getInstance().findByCode("Default");
        if (!profileOptional.isPresent()) {
            throw new RuntimeException("The relation between the profile <NOT FOUND> and the map '" + officialMap.getDescription() + "' could not be persisted to database in populate process");
        }

        Optional<SteamPlatform> steamPlatformOptional = SteamPlatformDao.getInstance().findByCode(EnumPlatform.STEAM.name());
        Optional<EpicPlatform> epicPlatformOptional = EpicPlatformDao.getInstance().findByCode(EnumPlatform.EPIC.name());

        OfficialMapDao.getInstance().insert(officialMap);
        prepareAndPopulatePlatformProfileMap(profileOptional.get(), officialMap, steamPlatformOptional, epicPlatformOptional);
    }

    protected void populatePlatformProfileMap(AbstractPlatform platform, Profile profile, AbstractMap map) throws SQLException {
        PlatformProfileMap platformProfileMap = new PlatformProfileMap(platform, profile, map, map.getReleaseDate(), map.getUrlInfo(), map.getUrlPhoto(), true);
        PlatformProfileMapDao.getInstance().insert(platformProfileMap);
    }

    protected void populatePlatform(EnumPlatform platform) throws SQLException {
        if (EnumPlatform.STEAM.name().equals(platform.name())) {
            SteamPlatform newPlatform = new SteamPlatform(platform, false, false, "preview");
            SteamPlatformDao.getInstance().insert(newPlatform);
        }

        if (EnumPlatform.EPIC.name().equals(platform.name())) {
            EpicPlatform newPlatform = new EpicPlatform(platform);
            EpicPlatformDao.getInstance().insert(newPlatform);
        }
    }

    private void prepareAndPopulatePlatformProfileMap(
            Profile profile,
            OfficialMap officialMap,
            Optional<SteamPlatform> steamPlatformOptional,
            Optional<EpicPlatform> epicPlatformOptional
    ) throws Exception {

        if (steamPlatformOptional.isPresent()) {
            populatePlatformProfileMap(steamPlatformOptional.get(), profile, officialMap);
        }
        if (epicPlatformOptional.isPresent()) {
            populatePlatformProfileMap(epicPlatformOptional.get(), profile, officialMap);
        }
    }
}
