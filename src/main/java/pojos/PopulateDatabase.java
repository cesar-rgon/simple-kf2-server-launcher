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
    protected void populateDifficulties() throws Exception {
        populateDifficulty("0","Normal", "Normal", "Normal" );
        populateDifficulty("1","Hard", "Difícil", "Difficile" );
        populateDifficulty("2","Suicidal", "Suicida", "Suicidaire" );
        populateDifficulty("3","Hell on Earth", "Infernal", "Enfer sur terre" );
    }

    @Override
    protected void populateGameTypes() throws Exception {
        populateGameType("KFGameContent.KFGameInfo_Survival", true, true, "Survival", "Supervivencia", "Survie");
        populateGameType("KFGameContent.KFGameInfo_VersusSurvival", false, false, "Versus", "Versus", "Versus");
        populateGameType("KFGameContent.KFGameInfo_WeeklySurvival", false, false, "Weekly", "Semanal", "Hebdomadaire");
        populateGameType("KFGameContent.KFGameInfo_Endless", true, false, "Endless", "Sin fin", "Infini");
        populateGameType("KFGameContent.KFGameInfo_Objective", true, false, "Objetive", "Objetivo", "Objetif");
    }

    @Override
    protected void populateLengths() throws Exception {
        populateLength("0","4 waves", "4 oleadas", "4 vagues");
        populateLength("1","7 waves", "7 oleadas", "7 vagues");
        populateLength("2","10 waves", "10 oleadas", "10 vagues");
    }

    @Override
    protected void polulateMaximunPlayersList() throws Exception {
        polulateMaximunPlayers("12", "Twelve", "Doce", "Douze");
        polulateMaximunPlayers("11", "Eleven", "Once", "Onze");
        polulateMaximunPlayers("10", "Ten", "Diez", "Dix");
        polulateMaximunPlayers("9", "Nine", "Nueve", "Neuf");
        polulateMaximunPlayers("8", "Eight", "Ocho", "Huit");
        polulateMaximunPlayers("7", "Seven", "Siete", "Sept");
        polulateMaximunPlayers("6", "Six", "Seis", "Six");
        polulateMaximunPlayers("5", "Five", "Cinco", "Cinq");
        polulateMaximunPlayers("4", "Four", "Cuatro", "Quatre");
        polulateMaximunPlayers("3", "Three", "Tres", "Trois");
        polulateMaximunPlayers("2", "Two", "Dos", "Deux");
        polulateMaximunPlayers("1", "One", "Uno", "Un");
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
