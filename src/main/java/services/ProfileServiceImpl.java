package services;

import daos.*;
import entities.*;
import entities.AbstractMap;
import jakarta.persistence.EntityManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.CustomMapIndex;
import pojos.MapToDisplay;
import pojos.ProfileToDisplay;
import pojos.enums.EnumLanguage;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Factory;
import start.MainApplication;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ProfileServiceImpl extends AbstractService<Profile> implements ProfileService {

    private static final Logger logger = LogManager.getLogger(ProfileServiceImpl.class);

    public ProfileServiceImpl() {
        super();
    }

    public ProfileServiceImpl(EntityManager em) {
        super(em);
    }

    @Override
    public Optional<Profile> findProfileByCode(String profileName) throws Exception {
        return findByCode(profileName);
    }

    @Override
    public List<Profile> listAllProfiles() throws SQLException {
        return new ProfileDao(em).listAll();
    }

    @Override
    public Profile createItem(Profile profile) throws Exception {
        return new ProfileDao(em).insert(profile);
    }

    @Override
    public boolean updateItem(Profile profile) throws Exception {
        return new ProfileDao(em).update(profile);
    }

    @Override
    public List<Profile> listAll() throws Exception {
        return new ProfileDao(em).listAll();
    }

    @Override
    public Optional<Profile> findByCode(String code) throws Exception {
        return new ProfileDao(em).findByCode(code);
    }

    public boolean updateItemCode(Profile profile, String oldCode) throws Exception {
        return false;
    }

    public void updateItemDescription(Profile entity) throws Exception {
    }


    @Override
    public boolean deleteProfile(Profile profile) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        CustomMapModServiceImpl customMapService = new CustomMapModServiceImpl(em);
        ProfileDao profileDao = new ProfileDao(em);

        List<PlatformProfileMap> platformProfileMapList = platformProfileMapService.listPlatformProfileMaps(profile);
        platformProfileMapList.stream().forEach(ppm -> {
            try {
                platformProfileMapService.deleteItem(ppm);

                if (customMapService.findByCode(ppm.getMap().getCode()).isPresent()) {
                    CustomMapMod customMapMod = (CustomMapMod) Hibernate.unproxy(ppm.getMap());

                    List<PlatformProfileMap> platformProfileMapListForMap = platformProfileMapService.listPlatformProfileMaps(ppm.getPlatform(), customMapMod);
                    if (platformProfileMapListForMap.isEmpty()) {

                        if (customMapService.deleteItem(customMapMod)) {
                            File mapPhoto = new File(ppm.getPlatform().getInstallationFolder() + customMapMod.getUrlPhoto());
                            if (mapPhoto.exists()) {
                                mapPhoto.delete();
                            }
                            File mapCacheFolder = new File(ppm.getPlatform().getInstallationFolder() + "/KFGame/Cache/" + customMapMod.getIdWorkShop());
                            if (mapCacheFolder.exists()) {
                                FileUtils.deleteDirectory(mapCacheFolder);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Error removing the relation between the profile: " + profile.getName() + " and the map: " + ppm.getMap().getCode());
            }
        });

        File undertowFolder = new File(MainApplication.getAppData().getAbsolutePath() + "/.undertow");
        Files.walk(Paths.get(undertowFolder.getAbsolutePath()))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().startsWith(profile.getName().toLowerCase()))
                .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".png"))
                .forEach(path -> {
                    try {
                        FileUtils.delete(new File(path.toUri()));
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                });

        Profile updatedProfile = profileDao.get(profile.getId());
        return profileDao.remove(updatedProfile);
    }

    @Override
    public Profile cloneProfile(Profile profileToBeCloned, String newProfileName) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        PropertyService propertyService = new PropertyServiceImpl();
        String webServerPort = propertyService.getPropertyValue("properties/config.properties", "prop.config.webServerPort");

        if (StringUtils.isNotBlank(profileToBeCloned.getUrlImageServer())) {
            File undertowFolder = new File(MainApplication.getAppData().getAbsolutePath() + "/.undertow");
            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());
            String timestampStr = StringUtils.replace(timestamp.toString(), " ", "_");
            timestampStr = StringUtils.replace(timestampStr, ":", "_");
            timestampStr = StringUtils.replace(timestampStr, ".", "_");
            File sourceFile = new File(undertowFolder.getAbsolutePath() + "/" + lastTimeStamp(profileToBeCloned.getName().toLowerCase()));
            File targetFile = new File(undertowFolder.getAbsolutePath() + "/" + newProfileName.toLowerCase() + "_" + timestampStr + ".png");
            FileUtils.copyFile(sourceFile, targetFile);
        }

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
                StringUtils.isNotBlank(profileToBeCloned.getUrlImageServer()) ? "http://" + Utils.getPublicIp() + ":" + webServerPort + "/" + newProfileName.toLowerCase() + ".png": StringUtils.EMPTY,
                profileToBeCloned.getWelcomeMessage(),
                profileToBeCloned.getCustomParameters(),
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
                profileToBeCloned.getFriendlyFirePercentage(),
                profileToBeCloned.getNetTickrate(),
                profileToBeCloned.getLanTickrate(),
                profileToBeCloned.getLanMaxClientRate(),
                profileToBeCloned.getInternetMaxClientRate()
        );

        Profile savedProfile = createItem(newProfile);

        List<PlatformProfileMap> platformProfileMapListForProfileToBeCloned = platformProfileMapService.listPlatformProfileMaps(profileToBeCloned);

        platformProfileMapListForProfileToBeCloned.stream().forEach(ppm -> {
            try {
                PlatformProfileMap newPlatformProfileMap = new PlatformProfileMap(ppm.getPlatform(), savedProfile, ppm.getMap(), ppm.getReleaseDate(), ppm.getUrlInfo(), ppm.getUrlPhoto(), ppm.isDownloaded(), ppm.isInMapsCycle());
                newPlatformProfileMap.setAlias(ppm.getAlias());
                platformProfileMapService.createItem(newPlatformProfileMap);
            } catch (Exception e) {
                logger.error("Error creating the relation between the platform: " + ppm.getPlatform().getDescription() + ", the profile: " + savedProfile.getName() + " and the map: " + ppm.getMap().getCode());
            }
        });

        return savedProfile;
    }

    private String lastTimeStamp(String profileName) throws Exception {
        File undertowFolder = new File(MainApplication.getAppData().getAbsolutePath() + "/.undertow");
        Optional<String> newerProfileFileName = Files.walk(Paths.get(undertowFolder.getAbsolutePath()))
                .filter(Files::isRegularFile)
                .filter(path -> path.getFileName().toString().startsWith(profileName))
                .filter(path -> path.getFileName().toString().toLowerCase().endsWith(".png"))
                .max((o1, o2) -> o1.getFileName().toString().compareTo(o2.getFileName().toString()))
                .map(path -> path.getFileName().toString());

        return newerProfileFileName.orElse(StringUtils.EMPTY);
    }

    private void exportMapValues(PlatformProfileMap ppm, int profileIndex, int mapIndex, List<Integer> idsOfficialMapList, Properties properties) {
        // Save original map values
        properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".map" + mapIndex + ".name", ppm.getMap().getCode());
        if (ppm.getMap().getReleaseDate() != null) {
            properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".map" + mapIndex + ".releaseDate", new SimpleDateFormat("yyyy/MM/dd").format(ppm.getMap().getReleaseDate()));
        }
        properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".map" + mapIndex + ".urlInfo", StringUtils.isNotBlank(ppm.getMap().getUrlInfo())? ppm.getMap().getUrlInfo(): "");
        properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".map" + mapIndex + ".urlPhoto", StringUtils.isNotBlank(ppm.getMap().getUrlPhoto())? ppm.getMap().getUrlPhoto(): "");

        boolean isOfficialMap = idsOfficialMapList.contains(ppm.getMap().getId());
        properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".map" + mapIndex + ".official", String.valueOf(isOfficialMap));
        if (!isOfficialMap) {
            CustomMapMod customMapMod = (CustomMapMod) Hibernate.unproxy(ppm.getMap());
            properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".map" + mapIndex + ".idWorkShop", customMapMod.getIdWorkShop()!=null? String.valueOf(customMapMod.getIdWorkShop()): "");
            properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".map" + mapIndex + ".isMap", customMapMod.getMap()!=null? String.valueOf(customMapMod.getMap()): "");
        }

        // Save copied map values (they could be edited and changed)
        properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".profilemap" + mapIndex + ".alias", ppm.getAlias());
        if (ppm.getReleaseDate() != null) {
            properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".profilemap" + mapIndex + ".releaseDate", new SimpleDateFormat("yyyy/MM/dd").format(ppm.getReleaseDate()));
        }
        properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".profilemap" + mapIndex + ".urlInfo", ppm.getUrlInfo());
        properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".profilemap" + mapIndex + ".urlPhoto", ppm.getUrlPhoto());
        properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".profilemap" + mapIndex + ".isInMapsCycle", String.valueOf(ppm.isInMapsCycle()));
        properties.setProperty("exported.profile" + profileIndex + ".platform" + ppm.getPlatform().getCode() + ".profilemap" + mapIndex + ".isDownloaded", String.valueOf(ppm.isDownloaded()));
    }

    @Override
    public void exportProfilesToFile(List<Profile> profilesToExport, File file) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        OfficialMapServiceImpl officialMapService = new OfficialMapServiceImpl(em);
        LanguageServiceImpl languageService = new LanguageServiceImpl(em);
        PropertyService propertyService = new PropertyServiceImpl();

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
            properties.setProperty("exported.profile" + profileIndex + ".netTickrate", profile.getNetTickrate()!=null? String.valueOf(profile.getNetTickrate()): "");
            properties.setProperty("exported.profile" + profileIndex + ".lanTickrate", profile.getLanTickrate()!=null? String.valueOf(profile.getLanTickrate()): "");
            properties.setProperty("exported.profile" + profileIndex + ".lanMaxClientRate", profile.getLanMaxClientRate()!=null? String.valueOf(profile.getLanMaxClientRate()): "");
            properties.setProperty("exported.profile" + profileIndex + ".internetMaxClientRate", profile.getInternetMaxClientRate()!=null? String.valueOf(profile.getInternetMaxClientRate()): "");

            List<PlatformProfileMap> ppmList = platformProfileMapService.listPlatformProfileMaps(profile);
            List<PlatformProfileMap> steamPpmList = ppmList.stream().filter(ppm -> ppm.getPlatform().getCode().equals(EnumPlatform.STEAM.name())).collect(Collectors.toList());
            List<PlatformProfileMap> epicPpmList = ppmList.stream().filter(ppm -> ppm.getPlatform().getCode().equals(EnumPlatform.EPIC.name())).collect(Collectors.toList());
            List<Integer> idsOfficialMapList = officialMapService.listAllMaps().stream().map(AbstractMap::getId).collect(Collectors.toList());

            if (steamPpmList != null && !steamPpmList.isEmpty()) {
                properties.setProperty("exported.profile" + profileIndex + ".platform" + EnumPlatform.STEAM.name() + ".mapListSize", String.valueOf(steamPpmList.size()));
                int mapIndex = 1;
                for (PlatformProfileMap ppm : steamPpmList) {
                    exportMapValues(ppm, profileIndex, mapIndex, idsOfficialMapList, properties);
                    mapIndex++;
                }
            }

            if (epicPpmList != null && !epicPpmList.isEmpty()) {
                properties.setProperty("exported.profile" + profileIndex + ".platform" + EnumPlatform.EPIC.name() + ".mapListSize", String.valueOf(epicPpmList.size()));
                int mapIndex = 1;
                for (PlatformProfileMap ppm : epicPpmList) {
                    exportMapValues(ppm, profileIndex, mapIndex, idsOfficialMapList, properties);
                    mapIndex++;
                }
            }

            profileIndex ++;
        }

        List<Language> languageList = languageService.listAll();
        exportGameTypesToFile(properties, languageList);
        exportDifficultiesToFile(properties, languageList);
        exportLengthsToFile(properties, languageList);
        exportMaxPlayersToFile(properties, languageList);

        propertyService.savePropertiesToFile(properties, file);
    }

    private void exportGameTypesToFile(Properties properties, List<Language> languageList) throws Exception {
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);

        List<GameType> gameTypeList = gameTypeService.listAll();
        properties.setProperty("exported.gameTypes.number", String.valueOf(gameTypeList.size()));
        int gameTypeIndex = 1;

        for (GameType gameType: gameTypeList) {
            properties.setProperty("exported.gameType" + gameTypeIndex + ".code", gameType.getCode());
            properties.setProperty("exported.gameType" + gameTypeIndex + ".difficultyEnabled", String.valueOf(gameType.isDifficultyEnabled()));
            properties.setProperty("exported.gameType" + gameTypeIndex + ".lengthEnabled", String.valueOf(gameType.isLengthEnabled()));
            for (Language language: languageList) {
                properties.setProperty("exported.gameType" + gameTypeIndex + ".description." + language.getCode(),
                        gameType.getDescription() != null && StringUtils.isNotBlank(gameType.getDescription().getValue(language.getCode())) ? gameType.getDescription().getValue(language.getCode()): StringUtils.EMPTY
                );
            }
            gameTypeIndex++;
        }
    }

    private void exportDifficultiesToFile(Properties properties, List<Language> languageList) throws Exception {
        DifficultyServiceImpl difficultyService = new DifficultyServiceImpl(em);

        List<Difficulty> difficultyList = difficultyService.listAll();
        properties.setProperty("exported.difficulties.number", String.valueOf(difficultyList.size()));
        int difficultyIndex = 1;

        for (Difficulty difficulty: difficultyList) {
            properties.setProperty("exported.difficulty" + difficultyIndex + ".code", difficulty.getCode());
            for (Language language: languageList) {
                properties.setProperty("exported.difficulty" + difficultyIndex + ".description." + language.getCode(),
                        difficulty.getDescription() != null && StringUtils.isNotBlank(difficulty.getDescription().getValue(language.getCode())) ? difficulty.getDescription().getValue(language.getCode()): StringUtils.EMPTY
                );
            }
            difficultyIndex++;
        }
    }

    private void exportLengthsToFile(Properties properties, List<Language> languageList) throws Exception {
        LengthServiceImpl lengthService = new LengthServiceImpl(em);

        List<Length> lengthList = lengthService.listAll();
        properties.setProperty("exported.lengths.number", String.valueOf(lengthList.size()));
        int lengthIndex = 1;

        for (Length length: lengthList) {
            properties.setProperty("exported.length" + lengthIndex + ".code", length.getCode());
            for (Language language: languageList) {
                properties.setProperty("exported.length" + lengthIndex + ".description." + language.getCode(),
                        length.getDescription() != null && StringUtils.isNotBlank(length.getDescription().getValue(language.getCode())) ? length.getDescription().getValue(language.getCode()): StringUtils.EMPTY
                );
            }
            lengthIndex++;
        }
    }

    private void exportMaxPlayersToFile(Properties properties, List<Language> languageList) throws Exception {
        MaxPlayersServiceImpl maxPlayersService = new MaxPlayersServiceImpl(em);

        List<MaxPlayers> maxPlayersList = maxPlayersService.listAll();
        properties.setProperty("exported.maxPlayers.number", String.valueOf(maxPlayersList.size()));
        int maxPlayersIndex = 1;

        for (MaxPlayers maxPlayers: maxPlayersList) {
            properties.setProperty("exported.maxPlayers" + maxPlayersIndex + ".code", maxPlayers.getCode());
            for (Language language: languageList) {
                properties.setProperty("exported.maxPlayers" + maxPlayersIndex + ".description." + language.getCode(),
                        maxPlayers.getDescription() != null && StringUtils.isNotBlank(maxPlayers.getDescription().getValue(language.getCode())) ? maxPlayers.getDescription().getValue(language.getCode()): StringUtils.EMPTY
                );
            }
            maxPlayersIndex++;
        }
    }

    @Override
    public List<Profile> prepareProfilesFromFile(Properties entitiesProperties) throws Exception {

        PropertyService propertyService = new PropertyServiceImpl();
        LanguageServiceImpl languageService = new LanguageServiceImpl(em);
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);
        DifficultyServiceImpl difficultyService = new DifficultyServiceImpl(em);
        LengthServiceImpl lengthService = new LengthServiceImpl(em);
        MaxPlayersServiceImpl maxPlayersService = new MaxPlayersServiceImpl(em);

        int numberOfProfiles = Integer.parseInt(entitiesProperties.getProperty("exported.profiles.number"));
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        List<ProfileToDisplay> profileToDisplayList = new ArrayList<ProfileToDisplay>();

        for (int profileIndex = 1; profileIndex <= numberOfProfiles; profileIndex++) {
            String profileName = entitiesProperties.getProperty("exported.profile" + profileIndex + ".name");
            try {
                String gameTypeCode = entitiesProperties.getProperty("exported.profile" + profileIndex + ".gameType");
                String difficultyCode = entitiesProperties.getProperty("exported.profile" + profileIndex + ".difficulty");
                String lengthCode = entitiesProperties.getProperty("exported.profile" + profileIndex + ".length");

                Optional<GameType> gameTypeOptional = gameTypeService.findByCode(gameTypeCode);
                String gameTypeDescription = gameTypeOptional.isPresent() ?
                        EnumLanguage.en.name().equals(languageCode) ? gameTypeOptional.get().getDescription().getEnglishValue():
                                EnumLanguage.es.name().equals(languageCode) ? gameTypeOptional.get().getDescription().getSpanishValue():
                                        EnumLanguage.fr.name().equals(languageCode) ? gameTypeOptional.get().getDescription().getFrenchValue():
                                                EnumLanguage.ru.name().equals(languageCode) ? gameTypeOptional.get().getDescription().getRussianValue():
                                                        StringUtils.EMPTY:
                        StringUtils.EMPTY;

                Optional<Difficulty> difficultyOptional = difficultyService.findByCode(difficultyCode);
                String difficultyDescription = difficultyOptional.isPresent() ?
                        EnumLanguage.en.name().equals(languageCode) ? difficultyOptional.get().getDescription().getEnglishValue():
                                EnumLanguage.es.name().equals(languageCode) ? difficultyOptional.get().getDescription().getSpanishValue():
                                        EnumLanguage.fr.name().equals(languageCode) ? difficultyOptional.get().getDescription().getFrenchValue():
                                                EnumLanguage.ru.name().equals(languageCode) ? difficultyOptional.get().getDescription().getRussianValue():
                                                        StringUtils.EMPTY:
                        StringUtils.EMPTY;

                Optional<Length> lengthOptional = lengthService.findByCode(lengthCode);
                String lengthDescription = lengthOptional.isPresent() ?
                        EnumLanguage.en.name().equals(languageCode) ? lengthOptional.get().getDescription().getEnglishValue():
                                EnumLanguage.es.name().equals(languageCode) ? lengthOptional.get().getDescription().getSpanishValue():
                                        EnumLanguage.fr.name().equals(languageCode) ? lengthOptional.get().getDescription().getFrenchValue():
                                                EnumLanguage.ru.name().equals(languageCode) ? lengthOptional.get().getDescription().getRussianValue():
                                                        StringUtils.EMPTY:
                        StringUtils.EMPTY;


                String mapName = entitiesProperties.getProperty("exported.profile" + profileIndex + ".map");
                ProfileToDisplay profileToDisplay = new ProfileToDisplay(profileIndex, profileName, gameTypeDescription, mapName, difficultyDescription, lengthDescription);
                profileToDisplay.setSelected(true);
                profileToDisplayList.add(profileToDisplay);

            } catch (Exception e) {
                logger.error("Error reading the profile " + profileName + " from exported file", e);
            }
        }

        return profileToDisplayList
                .stream()
                .map(ptd -> {
                    try {
                        int profileIndex = ptd.getProfileFileIndex();
                        String profileName = entitiesProperties.getProperty("exported.profile" + profileIndex + ".name");
                        Optional<Language> languageOpt = languageService.findByCode(entitiesProperties.getProperty("exported.profile" + profileIndex + ".language"));
                        Optional<GameType> gameTypeOpt = gameTypeService.findByCode(entitiesProperties.getProperty("exported.profile" + profileIndex + ".gameType"));
                        Optional<Difficulty> difficultyOpt = difficultyService.findByCode(entitiesProperties.getProperty("exported.profile" + profileIndex + ".difficulty"));
                        Optional<Length> lengthOpt = lengthService.findByCode(entitiesProperties.getProperty("exported.profile" + profileIndex + ".length"));
                        Optional<MaxPlayers> maxPlayersOpt = maxPlayersService.findByCode(entitiesProperties.getProperty("exported.profile" + profileIndex + ".maxPlayers"));
                        String webPortStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".webPort");
                        String gamePortStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".gamePort");
                        String queryPortStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".queryPort");
                        String mapVotingTimeStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".mapVotingTime");
                        String kickPercentageStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".kickPercentage");
                        String timeBetweenKicksStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".timeBetweenKicks");
                        String maxIdleTimeStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".maxIdleTime");
                        String readyUpDelayStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".readyUpDelay");
                        String gameStartDelayStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".gameStartDelay");
                        String maxSpectatorsStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".maxSpectators");
                        String friendlyFirePercentageStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".friendlyFirePercentage");
                        String webPageStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".webPage");
                        String takeoverStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".takeover");
                        String teamCollisionStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".teamCollision");
                        String adminPauseStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".adminPause");
                        String adminLoginStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".adminLogin");
                        String mapVotingStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".mapVoting");
                        String kickVotingStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".kickVoting");
                        String publicTextChatStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".publicTextChat");
                        String spectatorsChatStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".spectatorsChat");
                        String voipStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".voip");
                        String chatLoggingStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".chatLogging");
                        String chatLoggingFileTimestampStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".chatLoggingFileTimestamp");
                        String deadPlayersCanTalkStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".deadPlayersCanTalk");
                        String mapObjetivesStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".mapObjetives");
                        String pickupItemsStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".pickupItems");
                        String platformName = entitiesProperties.getProperty("exported.profile" + profileIndex + ".platform");
                        String netTickrateStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".netTickrate");
                        String lanTickrateStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".lanTickrate");
                        String lanMaxClientRateStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".lanMaxClientRate");
                        String internetMaxClientRateStr = entitiesProperties.getProperty("exported.profile" + profileIndex + ".internetMaxClientRate");

                        Profile profileFromFile = new Profile(
                                profileName,
                                languageOpt.isPresent() ? languageOpt.get() : null,
                                gameTypeOpt.isPresent() ? gameTypeOpt.get() : null,
                                null,
                                difficultyOpt.isPresent() ? difficultyOpt.get() : null,
                                lengthOpt.isPresent() ? lengthOpt.get() : null,
                                maxPlayersOpt.isPresent() ? maxPlayersOpt.get() : null,
                                entitiesProperties.getProperty("exported.profile" + profileIndex + ".serverName"),
                                entitiesProperties.getProperty("exported.profile" + profileIndex + ".serverPassword"),
                                StringUtils.isNotBlank(webPageStr) ? Boolean.valueOf(webPageStr): true,
                                entitiesProperties.getProperty("exported.profile" + profileIndex + ".webPassword"),
                                StringUtils.isNotBlank(webPortStr) ? Integer.parseInt(webPortStr): 8080,
                                StringUtils.isNotBlank(gamePortStr) ? Integer.parseInt(gamePortStr): 7777,
                                StringUtils.isNotBlank(queryPortStr) ? Integer.parseInt(queryPortStr): 27015,
                                entitiesProperties.getProperty("exported.profile" + profileIndex + ".yourClan"),
                                entitiesProperties.getProperty("exported.profile" + profileIndex + ".yourWebLink"),
                                entitiesProperties.getProperty("exported.profile" + profileIndex + ".urlImageServer"),
                                entitiesProperties.getProperty("exported.profile" + profileIndex + ".welcomeMessage"),
                                entitiesProperties.getProperty("exported.profile" + profileIndex + ".customParameters"),
                                StringUtils.isNotBlank(takeoverStr) ? Boolean.valueOf(takeoverStr): false,
                                StringUtils.isNotBlank(teamCollisionStr) ? Boolean.valueOf(teamCollisionStr): true,
                                StringUtils.isNotBlank(adminPauseStr) ? Boolean.valueOf(adminPauseStr): false,
                                StringUtils.isNotBlank(adminLoginStr) ? Boolean.valueOf(adminLoginStr): true,
                                StringUtils.isNotBlank(mapVotingStr) ? Boolean.valueOf(mapVotingStr): true,
                                StringUtils.isNotBlank(mapVotingTimeStr) ? Double.parseDouble(mapVotingTimeStr): 60,
                                StringUtils.isNotBlank(kickVotingStr) ? Boolean.valueOf(kickVotingStr): true,
                                StringUtils.isNotBlank(kickPercentageStr) ? Double.parseDouble(kickPercentageStr): 0.66,
                                StringUtils.isNotBlank(publicTextChatStr) ? Boolean.valueOf(publicTextChatStr): true,
                                StringUtils.isNotBlank(spectatorsChatStr) ? Boolean.valueOf(spectatorsChatStr): false,
                                StringUtils.isNotBlank(voipStr) ? Boolean.valueOf(voipStr): true,
                                StringUtils.isNotBlank(chatLoggingStr) ? Boolean.valueOf(chatLoggingStr): false,
                                entitiesProperties.getProperty("exported.profile" + profileIndex + ".chatLoggingFile"),
                                StringUtils.isNotBlank(chatLoggingFileTimestampStr) ? Boolean.valueOf(chatLoggingFileTimestampStr): true,
                                StringUtils.isNotBlank(timeBetweenKicksStr) ? Double.parseDouble(timeBetweenKicksStr): 10,
                                StringUtils.isNotBlank(maxIdleTimeStr) ? Double.parseDouble(maxIdleTimeStr): 0,
                                StringUtils.isNotBlank(deadPlayersCanTalkStr) ? Boolean.valueOf(deadPlayersCanTalkStr): true,
                                StringUtils.isNotBlank(readyUpDelayStr) ? Integer.parseInt(readyUpDelayStr): 90,
                                StringUtils.isNotBlank(gameStartDelayStr) ? Integer.parseInt(gameStartDelayStr): 4,
                                StringUtils.isNotBlank(maxSpectatorsStr) ? Integer.parseInt(maxSpectatorsStr): 2,
                                StringUtils.isNotBlank(mapObjetivesStr) ? Boolean.valueOf(mapObjetivesStr): true,
                                StringUtils.isNotBlank(pickupItemsStr) ? Boolean.valueOf(pickupItemsStr): true,
                                StringUtils.isNotBlank(friendlyFirePercentageStr) ? Double.parseDouble(friendlyFirePercentageStr): 0,
                                StringUtils.isNotBlank(netTickrateStr) ? Integer.parseInt(netTickrateStr): 30,
                                StringUtils.isNotBlank(lanTickrateStr) ? Integer.parseInt(lanTickrateStr): 35,
                                StringUtils.isNotBlank(lanMaxClientRateStr) ? Integer.parseInt(lanMaxClientRateStr): 15000,
                                StringUtils.isNotBlank(internetMaxClientRateStr) ? Integer.parseInt(internetMaxClientRateStr): 10000
                        );
                        return profileFromFile;
                    } catch (Exception e) {
                        logger.error("Error getting the profile " + ptd.getProfileName() + " from file", e);
                    }
                    return null;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Profile> importProfilesFromFile(List<Profile> selectedProfileList, Properties properties, StringBuffer errorMessage, boolean hasToSelectMaps) {
        List<Profile> savedProfileList = saveProfilesToDatabase(selectedProfileList, properties, hasToSelectMaps);

        if (savedProfileList.size() < selectedProfileList.size()) {
            List<String> selectedProfileNameList = selectedProfileList.stream().map(p -> p.getCode()).collect(Collectors.toList());
            List<String> savedProfileNameList = savedProfileList.stream().map(profile -> profile.getCode()).collect(Collectors.toList());

            for (String selectedProfileName : selectedProfileNameList) {
                if (!savedProfileNameList.contains(selectedProfileName)) {
                    errorMessage.append(selectedProfileName + "\n");
                }
            }
        }

        return savedProfileList;
    }

    @Override
    public void importGameTypesFromFile(Properties properties, List<Language> languageList) throws Exception {
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);

        List<GameType> gameTypeListInDataBase = gameTypeService.listAll();
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

                Description newDescription = new Description();
                for (Language language: languageList) {
                    String descriptionValue = properties.getProperty("exported.gameType" + index + ".description." + language.getCode());
                    newDescription.setValue(descriptionValue, language.getCode());
                }

                GameType newGameType = new GameType(code);
                newGameType.setDifficultyEnabled(Boolean.parseBoolean(properties.getProperty("exported.gameType" + index + ".difficultyEnabled")));
                newGameType.setLengthEnabled(Boolean.parseBoolean(properties.getProperty("exported.gameType" + index + ".lengthEnabled")));
                newGameType.setDescription(newDescription);
                gameTypeService.createItem(newGameType);

            } catch (Exception e) {
                logger.error("Error saving the Game Type " + code + " to database.");
            }
        }
    }

    @Override
    public void importDifficultiesFromFile(Properties properties, List<Language> languageList) throws Exception {
        DifficultyServiceImpl difficultyService = new DifficultyServiceImpl(em);

        List<Difficulty> difficultyListInDataBase = difficultyService.listAll();
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

                Description newDescription = new Description();
                for (Language language: languageList) {
                    String descriptionValue = properties.getProperty("exported.difficulty" + index + ".description." + language.getCode());
                    newDescription.setValue(descriptionValue, language.getCode());
                }

                Difficulty newDifficulty = new Difficulty(code);
                newDifficulty.setDescription(newDescription);
                difficultyService.createItem(newDifficulty);

            } catch (Exception e) {
                logger.error("Error saving the Difficulty " + code + " to database.");
            }
        }
    }

    @Override
    public void importLengthsFromFile(Properties properties, List<Language> languageList) throws Exception {
        LengthServiceImpl lengthService = new LengthServiceImpl(em);

        List<Length> lengthListInDataBase = lengthService.listAll();
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

                Description newDescription = new Description();
                for (Language language: languageList) {
                    String descriptionValue = properties.getProperty("exported.length" + index + ".description." + language.getCode());
                    newDescription.setValue(descriptionValue, language.getCode());
                }

                Length newLength = new Length(code);
                newLength.setDescription(newDescription);
                lengthService.createItem(newLength);

            } catch (Exception e) {
                logger.error("Error saving the Length " + code + " to database.");
            }
        }
    }

    @Override
    public void importMaxPlayersFromFile(Properties properties, List<Language> languageList) throws Exception {
        MaxPlayersServiceImpl maxPlayersService = new MaxPlayersServiceImpl(em);
        PropertyService propertyService = new PropertyServiceImpl();

        List<MaxPlayers> maxPlayersListInDataBase = maxPlayersService.listAll();
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

                Description newDescription = new Description();
                for (Language language: languageList) {
                    String descriptionValue = properties.getProperty("exported.maxPlayers" + index + ".description." + language.getCode());
                    newDescription.setValue(descriptionValue, language.getCode());
                }

                MaxPlayers newMaxPlayers = new MaxPlayers(code);
                newMaxPlayers.setDescription(newDescription);
                maxPlayersService.createItem(newMaxPlayers);

            } catch (Exception e) {
                logger.error("Error saving the Max.Players code " + code + " to database.");
            }
        }
    }

    private List<Profile> saveProfilesToDatabase(List<Profile> selectedProfileList, Properties properties, boolean hasToSelectMaps) {
        List<Profile> savedProfileList = new ArrayList<Profile>();

        int profileIndex = 1;
        for (Profile profile: selectedProfileList) {
            try {
                Profile savedProfile = new ProfileDao(em).insert(profile);

                List<AbstractMap> mapList = importPlatformProfileMapsFromFile(profileIndex, savedProfile, properties, hasToSelectMaps);

                String mapName = properties.getProperty("exported.profile" + profileIndex + ".map");
                Optional<AbstractMap> mapOpt = mapList.stream().filter(m -> m.getCode().equalsIgnoreCase(mapName)).findFirst();
                savedProfile.setMap(mapOpt.isPresent()? mapOpt.get(): null);

                new ProfileDao(em).update(savedProfile);

                savedProfileList.add(savedProfile);
                profileIndex++;
            } catch (Exception e) {
                logger.error("Error saving the profile " + profile.getCode() + " to database", e);
            }
        }
        return savedProfileList;
    }

    private int getMapListSize(int profileIndex, EnumPlatform enumPlatform, Properties properties) throws NumberFormatException {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String strNumberOfMaps = properties.getProperty("exported.profile" + profileIndex + ".mapListSize");
            if (StringUtils.isNotBlank(strNumberOfMaps)) {
                return Integer.parseInt(strNumberOfMaps);
            }
        }
        // New imported structure
        String strNumberOfMaps = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".mapListSize");
        return StringUtils.isNotBlank(strNumberOfMaps) ? Integer.parseInt(strNumberOfMaps): 0;
    }

    private String getMapName(int profileIndex, EnumPlatform enumPlatform, int mapIndex, Properties properties) {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String mapName = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".name");
            if (StringUtils.isNotBlank(mapName)) {
                return mapName;
            }
        }
        // New imported structure
        String mapName = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".map" + mapIndex + ".name");
        return StringUtils.isNotBlank(mapName) ? mapName: StringUtils.EMPTY;
    }

    private boolean isOfficialMap(int profileIndex, EnumPlatform enumPlatform, int mapIndex, Properties properties) {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String strIsOfficial = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".official");
            if (StringUtils.isNotBlank(strIsOfficial)) {
                return Boolean.parseBoolean(strIsOfficial);
            }
        }
        // New imported structure
        String strIsOfficial = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".map" + mapIndex + ".official");
        return StringUtils.isNotBlank(strIsOfficial) ? Boolean.parseBoolean(strIsOfficial): false;
    }

    private boolean isInMapCycle(int profileIndex, EnumPlatform enumPlatform, int mapIndex, Properties properties) {
        String strIsInMapsCycle = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".profilemap" + mapIndex + ".isInMapsCycle");
        return Boolean.parseBoolean(StringUtils.isNotBlank(strIsInMapsCycle) ? strIsInMapsCycle: "true");
    }

    private boolean isDownloaded(int profileIndex, EnumPlatform enumPlatform, int mapIndex, Properties properties) {
        String strIsDownloaded = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".profilemap" + mapIndex + ".isDownloaded");
        return Boolean.parseBoolean(StringUtils.isNotBlank(strIsDownloaded) ? strIsDownloaded: "false");
    }

    private Long getIdWorkShop(int profileIndex, EnumPlatform enumPlatform, int mapIndex, Properties properties) throws NumberFormatException {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String strIdWorkShop = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".idWorkShop");
            if (StringUtils.isNotBlank(strIdWorkShop)) {
                return (Long) Long.parseLong(strIdWorkShop);
            }
        }
        // New imported structure
        String strIdWorkShop = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".map" + mapIndex + ".idWorkShop");
        return (Long) (StringUtils.isNotBlank(strIdWorkShop) ? Long.parseLong(strIdWorkShop): 0L);
    }

    private Boolean getIsMap(int profileIndex, EnumPlatform enumPlatform, int mapIndex, Properties properties) throws NumberFormatException {
        String strIsMap = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".map" + mapIndex + ".isMap");
        return (Boolean) (StringUtils.isNotBlank(strIsMap) ? Boolean.parseBoolean(strIsMap): null);
    }

    private List<AbstractMap> importPlatformProfileMapsFromFile(int profileIndex, Profile profile, Properties properties, boolean hasToSelectMaps) throws Exception {
        OfficialMapServiceImpl officialMapService = new OfficialMapServiceImpl(em);
        CustomMapModServiceImpl customMapService = new CustomMapModServiceImpl(em);
        PropertyService propertyService = new PropertyServiceImpl();

        List<AbstractMap> mapList = new ArrayList<AbstractMap>();
        List<MapToDisplay> customMapListToDisplay = new ArrayList<MapToDisplay>();

        java.util.Map<String,Integer> steamOfficialMapsIndex = new HashMap<String, Integer>();
        java.util.Map<String,Integer> epicOfficialMapsIndex = new HashMap<String, Integer>();

        List<CustomMapIndex> steamCustomMapList = new ArrayList<CustomMapIndex>();
        List<CustomMapIndex> epicCustomMapList = new ArrayList<CustomMapIndex>();

        int steamMapListSize = getMapListSize(profileIndex, EnumPlatform.STEAM, properties);
        for (int mapIndex = 1; mapIndex <= steamMapListSize; mapIndex++) {
            String mapName = getMapName(profileIndex, EnumPlatform.STEAM, mapIndex, properties);
            try {
                boolean isOfficial = isOfficialMap(profileIndex, EnumPlatform.STEAM, mapIndex, properties);
                if (isOfficial) {
                    steamOfficialMapsIndex.put(mapName, Integer.valueOf(mapIndex));
                } else {
                    Long idWorkShop = getIdWorkShop(profileIndex, EnumPlatform.STEAM, mapIndex, properties);
                    Boolean isMap = getIsMap(profileIndex, EnumPlatform.STEAM, mapIndex, properties);
                    MapToDisplay mapToDisplay = new MapToDisplay(idWorkShop, mapName);
                    mapToDisplay.setPlatformDescription(EnumPlatform.STEAM.getDescripcion());
                    customMapListToDisplay.add(mapToDisplay);
                    steamCustomMapList.add(new CustomMapIndex(Integer.valueOf(mapIndex), idWorkShop, isMap));
                }
            } catch (Exception e) {
                logger.error("Error importing the map " + mapName + " of profile index " + profileIndex + " in platform " + EnumPlatform.STEAM.getDescripcion() + " from file", e);
            }
        }

        int epicMapListSize = getMapListSize(profileIndex, EnumPlatform.EPIC, properties);
        for (int mapIndex = 1; mapIndex <= epicMapListSize; mapIndex++) {
            String mapName = getMapName(profileIndex, EnumPlatform.EPIC, mapIndex, properties);
            try {
                boolean isOfficial = isOfficialMap(profileIndex, EnumPlatform.EPIC, mapIndex, properties);
                if (isOfficial) {
                    epicOfficialMapsIndex.put(mapName, Integer.valueOf(mapIndex));
                } else {
                    Long idWorkShop = getIdWorkShop(profileIndex, EnumPlatform.EPIC, mapIndex, properties);
                    Boolean isMap = getIsMap(profileIndex, EnumPlatform.EPIC, mapIndex, properties);
                    MapToDisplay mapToDisplay = new MapToDisplay(idWorkShop, mapName);
                    mapToDisplay.setPlatformDescription(EnumPlatform.EPIC.getDescripcion());
                    customMapListToDisplay.add(mapToDisplay);
                    epicCustomMapList.add(new CustomMapIndex(Integer.valueOf(mapIndex), idWorkShop, isMap));
                }
            } catch (Exception e) {
                logger.error("Error importing the map " + mapName + " of profile index " + profileIndex + " in platform " + EnumPlatform.EPIC.getDescripcion() + " from file", e);
            }
        }

        List<MapToDisplay> selectedCustomMapList = null;
        if (hasToSelectMaps) {
            String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
            String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.selectMapsModsForProfile");
            selectedCustomMapList = Utils.selectMapsDialog(headerText + " " + profile.getCode(), customMapListToDisplay);
        } else {
            selectedCustomMapList = customMapListToDisplay;
        }

        for (java.util.Map.Entry<String, Integer> entry : steamOfficialMapsIndex.entrySet()) {
            String mapName = entry.getKey();
            Integer mapIndex = entry.getValue();
            try {
                boolean isInMapCycle = isInMapCycle(profileIndex, EnumPlatform.STEAM, mapIndex, properties);
                Optional mapInDataBaseOpt = officialMapService.findMapByCode(mapName);
                AbstractMap map = getImportedMap(EnumPlatform.STEAM, mapInDataBaseOpt, profile, properties, profileIndex, mapIndex, mapName, null, true,true);
                if (map != null) {
                    mapList.add(map);
                    importPlatformProfileMap(EnumPlatform.STEAM, profile, map, true, isInMapCycle, profileIndex, mapIndex,  properties);
                }
            } catch (Exception e) {
                logger.error("Error importing the official map " + mapName + " of profile index " + profileIndex + " in platform " + EnumPlatform.STEAM.getDescripcion() + " from file", e);
            }
        }

        for (java.util.Map.Entry<String, Integer> entry : epicOfficialMapsIndex.entrySet()) {
            String mapName = entry.getKey();
            Integer mapIndex = entry.getValue();
            try {
                boolean isInMapCycle = isInMapCycle(profileIndex, EnumPlatform.EPIC, mapIndex, properties);
                Optional mapInDataBaseOpt = officialMapService.findMapByCode(mapName);
                AbstractMap map = getImportedMap(EnumPlatform.EPIC, mapInDataBaseOpt, profile, properties, profileIndex, mapIndex, mapName, null, true, true);
                if (map != null) {
                    mapList.add(map);
                    importPlatformProfileMap(EnumPlatform.EPIC, profile, map, true, isInMapCycle, profileIndex, mapIndex,  properties);
                }
            } catch (Exception e) {
                logger.error("Error importing the official map " + mapName + " of profile index " + profileIndex + " in platform " + EnumPlatform.EPIC.getDescripcion() + " from file", e);
            }
        }

        for (MapToDisplay customMap: selectedCustomMapList) {
            EnumPlatform enumPlatform = null;
            Integer mapIndex = null;
            Boolean isMap = null;
            if (customMap.getPlatformDescription().equals(EnumPlatform.STEAM.getDescripcion())) {
                enumPlatform = EnumPlatform.STEAM;
                Optional<CustomMapIndex> steamCustomMapIndexOptional = steamCustomMapList.stream().filter(cm -> cm.getIdWorkShop().equals(customMap.getIdWorkShop())).findFirst();
                if (!steamCustomMapIndexOptional.isPresent()) {
                    continue;
                }
                mapIndex = steamCustomMapIndexOptional.get().getMapIndex();
                isMap = steamCustomMapIndexOptional.get().getMap();

            } else {
                enumPlatform = EnumPlatform.EPIC;
                Optional<CustomMapIndex> epicCustomMapIndexOptional = epicCustomMapList.stream().filter(cm -> cm.getIdWorkShop().equals(customMap.getIdWorkShop())).findFirst();
                if (!epicCustomMapIndexOptional.isPresent()) {
                    continue;
                }
                mapIndex = epicCustomMapIndexOptional.get().getMapIndex();
                isMap = epicCustomMapIndexOptional.get().getMap();

            }

            try {
                Optional mapInDataBaseOpt = customMapService.findByIdWorkShop(customMap.getIdWorkShop());
                AbstractMap map = getImportedMap(enumPlatform, mapInDataBaseOpt, profile, properties, profileIndex, mapIndex, customMap.getCommentary(), customMap.getIdWorkShop(), isMap, false);
                if (map != null) {
                    mapList.add(map);
                    boolean isInMapCycle = isInMapCycle(profileIndex, enumPlatform, mapIndex, properties);
                    boolean isDownloaded = isDownloaded(profileIndex, enumPlatform, mapIndex, properties);
                    importPlatformProfileMap(enumPlatform, profile, map, isDownloaded, isInMapCycle, profileIndex, mapIndex,  properties);
                }
            } catch (Exception e) {
                logger.error("Error importing the official map " + customMap.getCommentary() + " of profile index " + profileIndex + " from file", e);
            }
        }

        return mapList;
    }

    private String getAlias(int profileIndex, Integer mapIndex, EnumPlatform enumPlatform, String mapName, Properties properties) {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String alias = properties.getProperty("exported.profile" + profileIndex + ".profilemap" + mapIndex + ".alias");
            if (StringUtils.isNotBlank(alias)) {
                return alias;
            }
        }
        // New imported structure
        String alias = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".profilemap" + mapIndex + ".alias");
        return StringUtils.isNotBlank(alias) ? alias: mapName;
    }

    private Date getReleaseDateCopy(int profileIndex, Integer mapIndex, EnumPlatform enumPlatform, Properties properties) throws ParseException {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String releaseDateCopyStr = properties.getProperty("exported.profile" + profileIndex + ".profilemap" + mapIndex + ".releaseDate");
            if (StringUtils.isNotBlank(releaseDateCopyStr)) {
                return new SimpleDateFormat("yyyy/MM/dd").parse(releaseDateCopyStr);
            }
        }

        String releaseDateStr = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".profilemap" + mapIndex + ".releaseDate");
        return StringUtils.isNotBlank(releaseDateStr) ? new SimpleDateFormat("yyyy/MM/dd").parse(releaseDateStr): null;
    }

    private String getUrlInfoCopy(int profileIndex, Integer mapIndex, EnumPlatform enumPlatform, String originalUrlInfo, Properties properties) {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String urlInfoCopy = properties.getProperty("exported.profile" + profileIndex + ".profilemap" + mapIndex + ".urlInfo");
            if (StringUtils.isNotBlank(urlInfoCopy)) {
                return urlInfoCopy;
            }
        }
        // New imported structure
        String urlInfoCopy = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".profilemap" + mapIndex + ".urlInfo");
        return StringUtils.isNotBlank(urlInfoCopy) ? urlInfoCopy: originalUrlInfo;
    }

    private String getUrlPhotoCopy(int profileIndex, Integer mapIndex, EnumPlatform enumPlatform, String originalUrlPhoto, Properties properties) {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String urlPhotoCopy = properties.getProperty("exported.profile" + profileIndex + ".profilemap" + mapIndex + ".urlPhoto");
            if (StringUtils.isNotBlank(urlPhotoCopy)) {
                return urlPhotoCopy;
            }
        }
        // New imported structure
        String urlPhotoCopy = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".profilemap" + mapIndex + ".urlPhoto");
        return StringUtils.isNotBlank(urlPhotoCopy) ? urlPhotoCopy: originalUrlPhoto;
    }

    private void importPlatformProfileMap(EnumPlatform enumPlatform, Profile profile, AbstractMap map, boolean downloaded, boolean isInMapsCycle, int profileIndex, int mapIndex, Properties properties) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);

        String alias = getAlias(profileIndex, Integer.valueOf(mapIndex), enumPlatform, map.getCode(), properties);
        Date releaseDateCopy = getReleaseDateCopy(profileIndex, Integer.valueOf(mapIndex), enumPlatform, properties);
        Date releaseDate = releaseDateCopy != null ? releaseDateCopy: map.getReleaseDate() != null ? map.getReleaseDate(): null;
        String urlInfo = getUrlInfoCopy(profileIndex, Integer.valueOf(mapIndex), enumPlatform, map.getUrlInfo(), properties);
        String urlPhoto = getUrlPhotoCopy(profileIndex, Integer.valueOf(mapIndex), enumPlatform, map.getUrlPhoto(), properties);

        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(enumPlatform.name());
        if (platformOptional.isPresent()) {
            if (Kf2Factory.getInstance(platformOptional.get(), em).isValidInstallationFolder()) {
                PlatformProfileMap platformProfileMap = new PlatformProfileMap(platformOptional.get(), profile, map, releaseDate, urlInfo, urlPhoto, downloaded, isInMapsCycle);
                platformProfileMap.setAlias(alias);
                platformProfileMapService.createItem(platformProfileMap);
            }
        }
    }

    private String getUrlInfo(int profileIndex, Integer mapIndex, EnumPlatform enumPlatform, Properties properties) {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String urlInfo = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlInfo");
            if (StringUtils.isNotBlank(urlInfo)) {
                return urlInfo;
            }
        }
        // New imported structure
        String urlInfo = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".map" + mapIndex + ".urlInfo");
        return StringUtils.isNotBlank(urlInfo) ? urlInfo: StringUtils.EMPTY;
    }

    private String getUrlPhoto(int profileIndex, Integer mapIndex, EnumPlatform enumPlatform, Properties properties) {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String urlPhoto = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".urlPhoto");
            if (StringUtils.isNotBlank(urlPhoto)) {
                return urlPhoto;
            }
        }
        // New imported structure
        String urlPhoto = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".map" + mapIndex + ".urlPhoto");
        return StringUtils.isNotBlank(urlPhoto) ? urlPhoto: StringUtils.EMPTY;
    }

    private Date getReleaseDate(int profileIndex, Integer mapIndex, EnumPlatform enumPlatform, Properties properties) throws ParseException {
        if (enumPlatform.equals(EnumPlatform.STEAM)) {
            // Retrocompatibility
            String releaseDateStr = properties.getProperty("exported.profile" + profileIndex + ".map" + mapIndex + ".releaseDate");
            if (StringUtils.isNotBlank(releaseDateStr)) {
                return new SimpleDateFormat("yyyy/MM/dd").parse(releaseDateStr);
            }
        }

        String releaseDateStr = properties.getProperty("exported.profile" + profileIndex + ".platform" + enumPlatform.name() + ".map" + mapIndex + ".releaseDate");
        return StringUtils.isNotBlank(releaseDateStr) ? new SimpleDateFormat("yyyy/MM/dd").parse(releaseDateStr): null;
    }

    private AbstractMap getImportedMap(EnumPlatform enumPlatform, Optional<AbstractMap> mapInDataBaseOpt, Profile profile, Properties properties, int profileIndex, Integer mapIndex, String mapName, Long idWorkShop, Boolean isMap, boolean official) throws Exception {
        OfficialMapServiceImpl officialMapService = new OfficialMapServiceImpl(em);
        CustomMapModServiceImpl customMapService = new CustomMapModServiceImpl(em);

        if (mapInDataBaseOpt.isPresent()) {
            if (officialMapService.findByCode(mapInDataBaseOpt.get().getCode()).isPresent()) {
                if (officialMapService.updateItem(mapInDataBaseOpt.get())) {
                    return mapInDataBaseOpt.get();
                }
            } else {
                if (customMapService.updateItem(mapInDataBaseOpt.get())) {
                    return mapInDataBaseOpt.get();
                }
            }
        } else {
            String urlInfo = getUrlInfo(profileIndex, mapIndex, enumPlatform, properties);
            String urlPhoto = getUrlPhoto(profileIndex, mapIndex, enumPlatform, properties);
            Date releaseDate = getReleaseDate(profileIndex, mapIndex, enumPlatform, properties);

            AbstractMap map = null;
            if (official) {
                map = new OfficialMap(mapName, urlInfo, urlPhoto, releaseDate);
                return officialMapService.createItem(map);
            } else {
                map = new CustomMapMod(mapName, urlInfo, urlPhoto, idWorkShop, isMap);
                if (createNewCustomMapFromWorkshop((CustomMapMod) map)) {
                    return map;
                }
            }
        }
        return null;
    }

    private boolean createNewCustomMapFromWorkshop(CustomMapMod map) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);

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

        List<AbstractPlatform> validPlatformList = new ArrayList<AbstractPlatform>();
        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        if (steamPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(steamPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(steamPlatformOptional.get());
            }
        }
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        if (epicPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(epicPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(epicPlatformOptional.get());
            }
        }

        for (AbstractPlatform platform: validPlatformList) {
            Utils.downloadImageFromUrlToFile(strUrlMapImage, platform.getInstallationFolder() + map.getUrlPhoto());
        }

        AbstractMap insertedMap = customMapModService.createItem(map);;
        return (insertedMap != null);
    }

    @Override
    public List<Profile> getProfileListByNames(List<String> profileNameList, StringBuffer success, StringBuffer errors) {
        List<Profile> profileList = profileNameList.stream().map(pn -> {
            String message = "Error finding the profile with name" + pn;
            try {
                Optional<Profile> profileOptional = findProfileByCode(pn);
                if (profileOptional.isPresent()) {
                    success.append("The profile [" + pn + "] has been found!\n");
                    return profileOptional.get();
                }
                errors.append(message + "\n");
                return null;
            } catch (Exception e) {
                errors.append(message + "\n");
                logger.error(message, e);
                throw new RuntimeException(message, e);
            }
        }).collect(Collectors.toList());
        if (profileList == null || profileList.isEmpty()) {
            String message = "No profiles were found";
            errors.append(message + "\n");
            throw new RuntimeException(message);
        }
        return profileList;
    }

    @Override
    public boolean deleteAllItems(List<Profile> allItemList, List<Profile> allProfileList) throws Exception {
        for (Profile profile: allItemList) {
            deleteProfile(profile);
        }
        return true;
    }

    @Override
    public boolean deleteItem(Profile profile) throws Exception {
        return deleteProfile(profile);
    }

    @Override
    public boolean deleteItem(Profile profile, List<Profile> profileList) throws Exception {
        return deleteProfile(profile);
    }
}
