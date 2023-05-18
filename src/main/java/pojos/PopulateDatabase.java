package pojos;

import entities.*;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumLanguage;
import pojos.enums.EnumPlatform;
import pojos.listener.TimeListener;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PopulateDatabase extends AbstractPopulateDatabase {

    private static final Logger logger = LogManager.getLogger(PopulateDatabase.class);

    public PopulateDatabase(EntityManager em) {
        super(em);
    }

    @Override
    public void start() throws Exception {
        populatePlatforms();
        populateLanguages();
        populateDifficulties();
        populateGameTypes();
        populateLengths();
        polulateMaximunPlayersList();
        populateProfiles();
        populateOfficialMaps();
        setDefaultMapInProfile();
    }

    @Override
    protected void populateLanguages() throws Exception {
        populateLanguage(EnumLanguage.en.name(), EnumLanguage.en.getDescripcion());
        populateLanguage(EnumLanguage.es.name(), EnumLanguage.es.getDescripcion());
        populateLanguage(EnumLanguage.fr.name(), EnumLanguage.fr.getDescripcion());
    }

    @Override
    public List<Difficulty> populateDifficulties() throws Exception {
        List<Difficulty> defaultDifficultyList = new ArrayList<Difficulty>();
        defaultDifficultyList.add(populateDifficulty("0","Normal", "Normal", "Normal" ));
        defaultDifficultyList.add(populateDifficulty("1","Hard", "Difícil", "Difficile" ));
        defaultDifficultyList.add(populateDifficulty("2","Suicidal", "Suicida", "Suicidaire" ));
        defaultDifficultyList.add(populateDifficulty("3","Hell on Earth", "Infernal", "Enfer sur terre" ));
        return defaultDifficultyList;
    }

    @Override
    public List<GameType> populateGameTypes() throws Exception {
        List<GameType> deafultGameTypeList = new ArrayList<GameType>();
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_Survival", true, true, "Survival", "Supervivencia", "Survie"));
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_VersusSurvival", false, false, "Versus", "Versus", "Versus"));
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_WeeklySurvival", false, false, "Weekly", "Semanal", "Hebdomadaire"));
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_Endless", true, false, "Endless", "Sin fin", "Infini"));
        deafultGameTypeList.add(populateGameType("KFGameContent.KFGameInfo_Objective", true, false, "Objetive", "Objetivo", "Objetif"));
        return deafultGameTypeList;
    }

    @Override
    public List<Length> populateLengths() throws Exception {
        List<Length> deafultLengthTypeList = new ArrayList<Length>();
        deafultLengthTypeList.add(populateLength("0","4 waves", "4 oleadas", "4 vagues"));
        deafultLengthTypeList.add(populateLength("1","7 waves", "7 oleadas", "7 vagues"));
        deafultLengthTypeList.add(populateLength("2","10 waves", "10 oleadas", "10 vagues"));
        return deafultLengthTypeList;
    }

    @Override
    public List<MaxPlayers> polulateMaximunPlayersList() throws Exception {
        List<MaxPlayers> deafultMaxPlayersTypeList = new ArrayList<MaxPlayers>();
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("12", "Twelve", "Doce", "Douze"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("11", "Eleven", "Once", "Onze"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("10", "Ten", "Diez", "Dix"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("9", "Nine", "Nueve", "Neuf"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("8", "Eight", "Ocho", "Huit"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("7", "Seven", "Siete", "Sept"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("6", "Six", "Seis", "Six"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("5", "Five", "Cinco", "Cinq"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("4", "Four", "Cuatro", "Quatre"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("3", "Three", "Tres", "Trois"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("2", "Two", "Dos", "Deux"));
        deafultMaxPlayersTypeList.add(polulateMaximunPlayers("1", "One", "Uno", "Un"));
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

        Optional<Language> languageOptional = languageService.findByCode("en");
        Optional<GameType> gametypeOptional = gameTypeService.findByCode("KFGameContent.KFGameInfo_Survival");
        Optional<Difficulty> difficultyOptional = difficultyService.findByCode("0");
        Optional<Length> lengthOptional = lengthService.findByCode("0");
        Optional<MaxPlayers> maxPlayersOptional = maxPlayersService.findByCode("6");

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
                    null,
                    null,
                    "http://art.tripwirecdn.com/TestItemIcons/MOTDServer.png",
                    null,
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
                    0.0
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
