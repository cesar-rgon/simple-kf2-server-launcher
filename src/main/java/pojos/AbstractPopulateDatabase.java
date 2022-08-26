package pojos;

import daos.*;
import entities.*;
import pojos.enums.EnumPlatform;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

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
    protected abstract void populateProfileMapList() throws Exception;

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

    protected void populateOfficialMap(String code, String urlInfo, String urlPhoto, Date releaseDate) throws SQLException {
        OfficialMap officialMap = new OfficialMap(code, urlInfo, urlPhoto, releaseDate);
        OfficialMapDao.getInstance().insert(officialMap);
    }

    protected void populateProfile(String name, Language language, GameType gametype, AbstractMap map, Difficulty difficulty, Length length, MaxPlayers maxPlayers,
                                   String serverName, String serverPassword, Boolean webPage, String webPassword, Integer webPort, Integer gamePort, Integer queryPort,
                                   String yourClan, String yourWebLink, String urlImageServer, String welcomeMessage, String customParameters, List<ProfileMap> profileMapList, Boolean takeover,
                                   Boolean teamCollision, Boolean adminCanPause, Boolean announceAdminLogin, Boolean mapVoting, Double mapVotingTime,
                                   Boolean kickVoting, Double kickPercentage, Boolean publicTextChat, Boolean spectatorsOnlyChatToOtherSpectators, Boolean voip,
                                   Boolean chatLogging, String chatLoggingFile, Boolean chatLoggingFileTimestamp, Double timeBetweenKicks, Double maxIdleTime, Boolean deadPlayersCanTalk,
                                   Integer readyUpDelay, Integer gameStartDelay, Integer maxSpectators, Boolean mapObjetives, Boolean pickupItems, Double friendlyFirePercentage) throws SQLException {

        Profile profile = new Profile(name, language, gametype, map, difficulty, length, maxPlayers,
                serverName, serverPassword, webPage, webPassword, webPort, gamePort, queryPort,
                yourClan, yourWebLink, urlImageServer, welcomeMessage, customParameters, profileMapList, takeover,
                teamCollision, adminCanPause, announceAdminLogin, mapVoting, mapVotingTime,
                kickVoting, kickPercentage, publicTextChat, spectatorsOnlyChatToOtherSpectators, voip,
                chatLogging, chatLoggingFile, chatLoggingFileTimestamp, timeBetweenKicks, maxIdleTime, deadPlayersCanTalk,
                readyUpDelay, gameStartDelay, maxSpectators, mapObjetives, pickupItems, friendlyFirePercentage, EnumPlatform.STEAM);

        ProfileDao.getInstance().insert(profile);
    }

    protected void populateProfileMap(Profile profile, AbstractMap map, Date releaseDate, String urlInfo, String urlPhoto) throws SQLException {
        ProfileMap profileMap = new ProfileMap(profile, map, releaseDate, urlInfo, urlPhoto);
        ProfileMapDao.getInstance().insert(profileMap);
    }
}
