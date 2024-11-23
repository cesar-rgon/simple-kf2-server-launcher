package pojos;

import entities.*;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumPlatform;
import services.*;
import start.MainApplication;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public abstract class AbstractPopulateDatabase {

    protected final PlatformService platformService;
    protected final DifficultyServiceImpl difficultyService;
    protected final GameTypeServiceImpl gameTypeService;
    protected final LanguageServiceImpl languageService;
    protected final LengthServiceImpl lengthService;
    protected final MaxPlayersServiceImpl maxPlayersService;
    protected final AbstractMapService officialMapService;
    protected final PlatformProfileMapService platformProfileMapService;
    protected final ProfileService profileService;
    protected final EntityManager em;

    protected AbstractPopulateDatabase(EntityManager em) {
        super();
        this.em = em;
        this.platformService = new PlatformServiceImpl(em);
        this.difficultyService = new DifficultyServiceImpl(em);
        this.gameTypeService = new GameTypeServiceImpl(em);
        this.languageService = new LanguageServiceImpl(em);
        this.lengthService = new LengthServiceImpl(em);
        this.maxPlayersService = new MaxPlayersServiceImpl(em);
        this.officialMapService = new OfficialMapServiceImpl(em);
        this.platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        this.profileService = new ProfileServiceImpl(em);
    }

    public abstract void start() throws Exception;
    protected abstract void populateLanguages() throws Exception;
    protected abstract List<Difficulty> populateDifficulties() throws Exception;
    protected abstract List<GameType> populateGameTypes() throws Exception;
    protected abstract List<Length> populateLengths() throws Exception;
    protected abstract List<MaxPlayers> polulateMaximunPlayersList() throws Exception;
    protected abstract void populateOfficialMaps() throws Exception;
    protected abstract void populateProfiles() throws Exception;
    protected abstract void populatePlatforms() throws SQLException;

    protected void populateLanguage(String code, String description) throws Exception {
        Language language = new Language(code, description);
        languageService.createItem(language);
    }

    protected Difficulty populateDifficulty(String code, String englishDescription, String spanishDescription, String frenchDescription, String russianDescription) throws Exception {
        Description description = new Description(englishDescription, spanishDescription, frenchDescription, russianDescription);
        Difficulty difficulty = new Difficulty(code);
        difficulty.setDescription(description);
        return difficultyService.createItem(difficulty);
    }

    protected GameType populateGameType(String code, boolean difficultyEnabled, boolean lengthEnabled, String englishDescription, String spanishDescription, String frenchDescription, String russianDescription) throws Exception {
        Description description = new Description(englishDescription, spanishDescription, frenchDescription, russianDescription);
        GameType gameType = new GameType(code);
        gameType.setDifficultyEnabled(difficultyEnabled);;
        gameType.setLengthEnabled(lengthEnabled);
        gameType.setDescription(description);
        return gameTypeService.createItem(gameType);
    }

    protected Length populateLength(String code, String englishDescription, String spanishDescription, String frenchDescription, String russianDescription) throws Exception {
        Description description = new Description(englishDescription, spanishDescription, frenchDescription, russianDescription);
        Length length = new Length(code);
        length.setDescription(description);
        return lengthService.createItem(length);
    }

    protected MaxPlayers polulateMaximunPlayers(String code, String englishDescription, String spanishDescription, String frenchDescription, String russianDescription) throws Exception {
        Description description = new Description(englishDescription, spanishDescription, frenchDescription, russianDescription);
        MaxPlayers maxPlayers = new MaxPlayers(code);
        maxPlayers.setDescription(description);
        return maxPlayersService.createItem(maxPlayers);
    }

    protected void populateProfile(String name, Language language, GameType gametype, AbstractMap map, Difficulty difficulty, Length length, MaxPlayers maxPlayers,
                                   String serverName, String serverPassword, Boolean webPage, String webPassword, Integer webPort, Integer gamePort, Integer queryPort,
                                   String yourClan, String yourWebLink, String urlImageServer, String welcomeMessage, String customParameters, Boolean takeover,
                                   Boolean teamCollision, Boolean adminCanPause, Boolean announceAdminLogin, Boolean mapVoting, Double mapVotingTime,
                                   Boolean kickVoting, Double kickPercentage, Boolean publicTextChat, Boolean spectatorsOnlyChatToOtherSpectators, Boolean voip,
                                   Boolean chatLogging, String chatLoggingFile, Boolean chatLoggingFileTimestamp, Double timeBetweenKicks, Double maxIdleTime, Boolean deadPlayersCanTalk,
                                   Integer readyUpDelay, Integer gameStartDelay, Integer maxSpectators, Boolean mapObjetives, Boolean pickupItems, Double friendlyFirePercentage,
                                   Integer netTickrateStr, Integer lanTickrateStr, Integer lanMaxClientRateStr, Integer internetMaxClientRateStr) throws Exception {

        Profile profile = new Profile(name, language, gametype, map, difficulty, length, maxPlayers,
                serverName, serverPassword, webPage, webPassword, webPort, gamePort, queryPort,
                yourClan, yourWebLink, urlImageServer, welcomeMessage, customParameters, takeover,
                teamCollision, adminCanPause, announceAdminLogin, mapVoting, mapVotingTime,
                kickVoting, kickPercentage, publicTextChat, spectatorsOnlyChatToOtherSpectators, voip,
                chatLogging, chatLoggingFile, chatLoggingFileTimestamp, timeBetweenKicks, maxIdleTime, deadPlayersCanTalk,
                readyUpDelay, gameStartDelay, maxSpectators, mapObjetives, pickupItems, friendlyFirePercentage,
                netTickrateStr, lanTickrateStr, lanMaxClientRateStr, internetMaxClientRateStr);

        profileService.createItem(profile);

        URL sourceUrl = getClass().getClassLoader().getResource("images/default-banner.png");
        assert sourceUrl != null;
        File undertowFolder = new File(MainApplication.getAppData().getAbsolutePath() + "/.undertow");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        String timestampStr = StringUtils.replace(timestamp.toString(), " ", "_");
        timestampStr = StringUtils.replace(timestampStr, ":", "_");
        timestampStr = StringUtils.replace(timestampStr, ".", "_");
        File targetFile = new File(undertowFolder.getAbsolutePath() + "/" + name.toLowerCase() + "_" + timestampStr + ".png");
        FileUtils.copyURLToFile(sourceUrl, targetFile);
    }

    protected void populateOfficialMap(String code, String urlInfo, String urlPhoto, Date releaseDate) throws Exception {
        OfficialMap officialMap = new OfficialMap(code, urlInfo, urlPhoto, releaseDate);

        Optional<Profile> profileOptional = profileService.findByCode("Default");
        if (!profileOptional.isPresent()) {
            throw new RuntimeException("The relation between the profile <NOT FOUND> and the map '" + officialMap.getDescription() + "' could not be persisted to database in populate process");
        }

        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();

        officialMapService.createItem(officialMap);
        prepareAndPopulatePlatformProfileMap(profileOptional.get(), officialMap, steamPlatformOptional, epicPlatformOptional);
    }

    protected void populatePlatformProfileMap(AbstractPlatform platform, Profile profile, AbstractMap map) throws Exception {
        PlatformProfileMap platformProfileMap = new PlatformProfileMap(platform, profile, map, map.getReleaseDate(), map.getUrlInfo(), map.getUrlPhoto(), true, true);
        platformProfileMapService.createItem(platformProfileMap);
    }

    protected void populatePlatform(EnumPlatform platform) throws SQLException {
        if (EnumPlatform.STEAM.name().equals(platform.name())) {
            SteamPlatform newPlatform = new SteamPlatform(platform, false, false, "preview");
            platformService.createSteamPlatform(newPlatform);
        }

        if (EnumPlatform.EPIC.name().equals(platform.name())) {
            EpicPlatform newPlatform = new EpicPlatform(platform);
            platformService.createEpicPlatform(newPlatform);
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
