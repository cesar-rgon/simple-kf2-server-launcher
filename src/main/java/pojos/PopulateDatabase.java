package pojos;

import entities.*;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumLanguage;
import pojos.enums.EnumPlatform;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.PlatformService;
import services.PlatformServiceImpl;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class PopulateDatabase extends AbstractPopulateDatabase {

    private static final Logger logger = LogManager.getLogger(PopulateDatabase.class);

    public PopulateDatabase(EntityManager em) {
        super(em);
    }

    @Override
    public void start() throws Exception {
        populatePlatforms();
        populateLanguages();

        PropertyService propertyService = new PropertyServiceImpl();
        String upgradeTemporalFileStr = propertyService.getPropertyValue("properties/config.properties", "prop.config.upgradeTemporalFile");

        if (StringUtils.isNotBlank(upgradeTemporalFileStr)) {
            File upgradeTemporalFile = new File(upgradeTemporalFileStr);
            if (upgradeTemporalFile.exists() && upgradeTemporalFile.isFile()) {
                String steamInstallationFolder = propertyService.getPropertyValue(upgradeTemporalFile, "prop.upgrade.steamInstallationFolder");
                String epicInstallationFolder = propertyService.getPropertyValue(upgradeTemporalFile, "prop.upgrade.epicInstallationFolder");

                Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
                steamPlatformOptional.ifPresent(steamPlatform -> {
                    steamPlatform.setInstallationFolder(steamInstallationFolder);
                    try {
                        platformService.updateSteamPlatform(steamPlatform);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                });
                Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
                epicPlatformOptional.ifPresent(epicPlatform -> {
                    epicPlatform.setInstallationFolder(epicInstallationFolder);
                    try {
                        platformService.updateEpicPlatform(epicPlatform);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                });

                String importProfilesTemporalFileStr = propertyService.getPropertyValue(upgradeTemporalFile, "prop.upgrade.profilesExportedFile");
                File importProfilesTemporalFile = new File(importProfilesTemporalFileStr);
                if (importProfilesTemporalFile.exists() && importProfilesTemporalFile.isFile()) {
                    StringBuffer errorMessage = new StringBuffer();

                    Properties entitiesProperties = propertyService.loadPropertiesFromFile(importProfilesTemporalFile);
                    List<Language> languageList = languageService.listAll();
                    profileService.importGameTypesFromFile(entitiesProperties, languageList);
                    profileService.importDifficultiesFromFile(entitiesProperties, languageList);
                    profileService.importLengthsFromFile(entitiesProperties, languageList);
                    profileService.importMaxPlayersFromFile(entitiesProperties, languageList);
                    List<Profile> allProfileList = profileService.prepareProfilesFromFile(entitiesProperties);
                    List<Profile> importedProfileList = profileService.importProfilesFromFile(allProfileList, entitiesProperties, errorMessage, false);

                    if (!importedProfileList.isEmpty()) {
                        for (Profile importedProfile: importedProfileList) {
                            try {
                                List<AbstractPlatform> validPlatformList = new ArrayList<AbstractPlatform>();
                                if (steamPlatformOptional.isPresent()) {
                                    if (Kf2Factory.getInstance(steamPlatformOptional.get(), em).isValidInstallationFolder()) {
                                        validPlatformList.add(steamPlatformOptional.get());
                                    }
                                }
                                if (epicPlatformOptional.isPresent()) {
                                    if (Kf2Factory.getInstance(epicPlatformOptional.get(), em).isValidInstallationFolder()) {
                                        validPlatformList.add(epicPlatformOptional.get());
                                    }
                                }

                                for (AbstractPlatform platform: validPlatformList) {
                                    Kf2Common kf2Common = Kf2Factory.getInstance(platform, em);
                                    kf2Common.createConfigFolder(platform.getInstallationFolder(), importedProfile.getName());
                                }
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                    }

                    if (allProfileList.size() > 0) {
                        Session.getInstance().setActualProfileName(allProfileList.get(0).getName());
                    }

                    if (StringUtils.isNotBlank(errorMessage)) {
                        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
                        String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profilesNotImported");
                        String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.seeLauncherLog");
                        logger.error(headerText + ": " + errorMessage.toString() + " " + contentText);
                    }
                    return;
                }
            }
        }

        populateDifficulties();
        populateGameTypes();
        populateLengths();
        polulateMaximunPlayersList();
        populateProfiles();
        populateOfficialMaps();
        setDefaultMapInProfile();
        Session.getInstance().setActualProfileName("Default");
    }

    @Override
    protected void populateLanguages() throws Exception {
        populateLanguage(EnumLanguage.en.name(), EnumLanguage.en.getDescripcion());
        populateLanguage(EnumLanguage.es.name(), EnumLanguage.es.getDescripcion());
        populateLanguage(EnumLanguage.fr.name(), EnumLanguage.fr.getDescripcion());
        populateLanguage(EnumLanguage.ru.name(), EnumLanguage.ru.getDescripcion());
    }

    @Override
    public List<Difficulty> populateDifficulties() throws Exception {
        List<Difficulty> defaultDifficultyList = new ArrayList<Difficulty>();
        defaultDifficultyList.add(populateDifficulty("0","Normal", "Normal", "Normal", "Обычная" ));
        defaultDifficultyList.add(populateDifficulty("1","Hard", "Difícil", "Difficile", "Трудная"));
        defaultDifficultyList.add(populateDifficulty("2","Suicidal", "Suicida", "Suicidaire", "Убийственная" ));
        defaultDifficultyList.add(populateDifficulty("3","Hell on Earth", "Infernal", "Enfer sur terre", "Ад на земле" ));
        return defaultDifficultyList;
    }

    @Override
    public List<GameType> populateGameTypes() throws Exception {
        List<GameType> deafultGameTypeList = new ArrayList<GameType>();
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_Survival", true, true, "Survival", "Supervivencia", "Survie", "Выживание"));
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_VersusSurvival", false, false, "Versus", "Versus", "Versus", "Выживание в бою"));
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_WeeklySurvival", false, false, "Weekly", "Semanal", "Hebdomadaire", "Еженедельно"));
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_Endless", true, false, "Endless", "Sin fin", "Infini", "Без конца"));
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_Objective", true, false, "Objetive", "Objetivo", "Objetif", "Задача"));

        return deafultGameTypeList;
    }

    @Override
    public List<Length> populateLengths() throws Exception {
        List<Length> deafultLengthTypeList = new ArrayList<Length>();
        deafultLengthTypeList.add(populateLength("0","4 waves", "4 oleadas", "4 vagues", "4 волны"));
        deafultLengthTypeList.add(populateLength("1","7 waves", "7 oleadas", "7 vagues", "7 волн"));
        deafultLengthTypeList.add(populateLength("2","10 waves", "10 oleadas", "10 vagues", "10 волн"));
        return deafultLengthTypeList;
    }

    @Override
    public List<MaxPlayers> polulateMaximunPlayersList() throws Exception {
        List<MaxPlayers> deafultMaxPlayersTypeList = new ArrayList<MaxPlayers>();
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("12", "Twelve", "Doce", "Douze", "12"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("11", "Eleven", "Once", "Onze", "11"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("10", "Ten", "Diez", "Dix", "10"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("9", "Nine", "Nueve", "Neuf", "9"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("8", "Eight", "Ocho", "Huit", "8"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("7", "Seven", "Siete", "Sept", "7"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("6", "Six", "Seis", "Six", "6"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("5", "Five", "Cinco", "Cinq", "5"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("4", "Four", "Cuatro", "Quatre", "4"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("3", "Three", "Tres", "Trois", "3"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("2", "Two", "Dos", "Deux", "2"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("1", "One", "Uno", "Un", "1"));
        return deafultMaxPlayersTypeList;
    }

    @Override
    protected void populateOfficialMaps() throws Exception {
        SimpleDateFormat dateFormatter=new SimpleDateFormat("yyyy-MM-dd");
        PropertyService propertyService = new PropertyServiceImpl();

        boolean downloadOfficialMapsProperties = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.downloadOfficialMapsProperties"));
        if (downloadOfficialMapsProperties) {
            try {
                // Download official map.properties
                String urlMapsProperties = propertyService.getPropertyValue("properties/config.properties", "prop.config.urlMapProperties");
                String downloadConnectionTimeOut = propertyService.getPropertyValue("properties/config.properties", "prop.config.downloadConnectionTimeout");
                String downloadReadTimeOut = propertyService.getPropertyValue("properties/config.properties", "prop.config.downloadReadTimeout");
                URL urlMapProperties = getClass().getClassLoader().getResource("properties/maps.properties");
                File mapsPropertiesFile = new File(urlMapProperties.toURI());

                FileUtils.copyURLToFile(
                        new URL(urlMapsProperties),
                        mapsPropertiesFile,
                        Integer.parseInt(downloadConnectionTimeOut),
                        Integer.parseInt(downloadReadTimeOut)
                );
            } catch (Exception e) {
                logger.error("Error trying to download maps.properties file from Github", e);
            }
        }

        Integer totalMaps = Integer.parseInt(propertyService.getPropertyValue("properties/maps.properties", "prop.maps.total_maps"));
        for (int i = 1; i <= totalMaps; i++) {
            try {
                populateOfficialMap(
                        propertyService.getPropertyValue("properties/maps.properties", "prop.maps." + i + ".title"),
                        propertyService.getPropertyValue("properties/maps.properties", "prop.maps." + i + ".url"),
                        propertyService.getPropertyValue("properties/maps.properties", "prop.maps." + i + ".image_path"),
                        dateFormatter.parse(propertyService.getPropertyValue("properties/maps.properties", "prop.maps." + i + ".release_date"))
                );
            } catch (Exception e) {
                logger.error("Error preparing the map number " + i + " in database. It could not be imported.", e);
            }
        }
    }

    @Override
    protected void populateProfiles() throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();

        Optional<Language> languageOptional = languageService.findByCode("en");
        Optional<GameType> gametypeOptional = gameTypeService.findByCode("KFGameContent.KFGameInfo_Survival");
        Optional<Difficulty> difficultyOptional = difficultyService.findByCode("0");
        Optional<Length> lengthOptional = lengthService.findByCode("0");
        Optional<MaxPlayers> maxPlayersOptional = maxPlayersService.findByCode("6");
        int webServerPort = Integer.parseInt(propertyService.getPropertyValue("properties/config.properties", "prop.config.webServerPort"));

        if (languageOptional.isPresent() && gametypeOptional.isPresent() && difficultyOptional.isPresent() && lengthOptional.isPresent() && maxPlayersOptional.isPresent()) {
            populateProfile(
                    "Default",
                    languageOptional.get(),
                    gametypeOptional.get(),
                    null,
                    difficultyOptional.get(),
                    lengthOptional.get(),
                    maxPlayersOptional.get(),
                    "Killing Floor 2 Server",
                    null,
                    true,
                    null,
                    8080,
                    7777,
                    27015,
                    "My clan",
                    "https://www.myweb.com",
                    "http://" + Utils.getPublicIp() + ":" + webServerPort + "/default.png",
                    "Hi! This is my Killing Floor 2 Server.\nWelcome!",
                    null,
                    false,
                    true,
                    false,
                    true,
                    true,
                    60.0,
                    true,
                    0.66,
                    true,
                    false,
                    true,
                    false,
                    null,
                    true,
                    10.0,
                    0.0,
                    true,
                    90,
                    4,
                    2,
                    true,
                    true,
                    0.0,
                    30,
                    35,
                    15000,
                    10000
            );

        } else {
            throw new RuntimeException("The profile could not be persisted to database in populate process");
        }
    }

    @Override
    protected void populatePlatforms() throws SQLException {
        populatePlatform(EnumPlatform.STEAM);
        populatePlatform(EnumPlatform.EPIC);
    }

    private void setDefaultMapInProfile() throws Exception {
        Optional<Profile> profileOptional = profileService.findByCode("Default");
        if (!profileOptional.isPresent()) {
            throw new RuntimeException("The profile 'Default' has not been found");
        }
        Optional<AbstractMap> officialMapOptional = officialMapService.findByCode("KF-BurningParis");
        if (!officialMapOptional.isPresent()) {
            throw new RuntimeException("The official map 'KF-BurningParis' has not been found");
        }

        profileOptional.get().setMap(officialMapOptional.get());
        profileService.updateItem(profileOptional.get());
    }
}
