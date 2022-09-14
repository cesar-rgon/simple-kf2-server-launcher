package pojos;

import daos.*;
import entities.*;
import pojos.enums.EnumPlatform;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

public class PopulateDatabase extends AbstractPopulateDatabase {

    public PopulateDatabase() {
        super();
    }

    @Override
    public void start() throws Exception {
        populatePlatforms();
        populateLanguages();
        populateDifficulties();
        populateGameTypes();
        populateLengths();
        polulateMaximunPlayersList();
        populateOfficialMaps();
        populateProfiles();
        populatePlatformProfileMapList();
    }

    @Override
    protected void populateLanguages() throws SQLException {
        populateLanguage("en", "English");
        populateLanguage("es", "Español");
        populateLanguage("fr", "Français");
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

        populateOfficialMap("KF-BurningParis", "https://wiki.killingfloor2.com/index.php?title=Burning_Paris","/KFGame/Web/images/maps/KF-BurningParis.jpg", dateFormatter.parse("2015-04-21"));
        populateOfficialMap("KF-BioticsLab", "https://wiki.killingfloor2.com/index.php?title=Biotics_Lab_(Killing_Floor_2)","/KFGame/Web/images/maps/KF-BioticsLab.jpg", dateFormatter.parse("2015-04-21"));
        populateOfficialMap("KF-Outpost", "https://wiki.killingfloor2.com/index.php?title=Outpost","/KFGame/Web/images/maps/KF-Outpost.jpg", dateFormatter.parse("2015-04-21"));
        populateOfficialMap("KF-VolterManor", "https://wiki.killingfloor2.com/index.php?title=Volter_Manor","/KFGame/Web/images/maps/KF-VolterManor.jpg", dateFormatter.parse("2015-05-26"));
        populateOfficialMap("KF-Catacombs", "https://wiki.killingfloor2.com/index.php?title=Catacombs","/KFGame/Web/images/maps/KF-Catacombs.jpg", dateFormatter.parse("2015-09-01"));
        populateOfficialMap("KF-EvacuationPoint", "https://wiki.killingfloor2.com/index.php?title=Evacuation_Point","/KFGame/Web/images/maps/KF-EvacuationPoint.jpg", dateFormatter.parse("2015-09-01"));
        populateOfficialMap("KF-Farmhouse", "https://wiki.killingfloor2.com/index.php?title=Farmhouse","/KFGame/Web/images/maps/KF-Farmhouse.jpg", dateFormatter.parse("2015-12-03"));
        populateOfficialMap("KF-BlackForest", "https://wiki.killingfloor2.com/index.php?title=Black_Forest","/KFGame/Web/images/maps/KF-BlackForest.jpg", dateFormatter.parse("2015-12-03"));
        populateOfficialMap("KF-Prison", "https://wiki.killingfloor2.com/index.php?title=Prison","/KFGame/Web/images/maps/KF-Prison.jpg", dateFormatter.parse("2016-04-07"));
        populateOfficialMap("KF-ContainmentStation", "https://wiki.killingfloor2.com/index.php?title=Containment_Station","/KFGame/Web/images/maps/KF-ContainmentStation.jpg", dateFormatter.parse("2016-06-13"));
        populateOfficialMap("KF-HostileGrounds", "https://wiki.killingfloor2.com/index.php?title=Hostile_Grounds","/KFGame/Web/images/maps/KF-HostileGrounds.jpg", dateFormatter.parse("2016-06-13"));
        populateOfficialMap("KF-InfernalRealm", "https://wiki.killingfloor2.com/index.php?title=Infernal_Realm","/KFGame/Web/images/maps/KF-InfernalRealm.jpg", dateFormatter.parse("2016-08-25"));
        populateOfficialMap("KF-ZedLanding", "https://wiki.killingfloor2.com/index.php?title=ZED_Landing","/KFGame/Web/images/maps/KF-ZedLanding.jpg", dateFormatter.parse("2017-01-19"));
        populateOfficialMap("KF-Nuked", "https://wiki.killingfloor2.com/index.php?title=Nuked","/KFGame/Web/images/maps/KF-Nuked.jpg", dateFormatter.parse("2017-03-21"));
        populateOfficialMap("KF-TheDescent", "https://wiki.killingfloor2.com/index.php?title=The_Descent","/KFGame/Web/images/maps/KF-TheDescent.jpg", dateFormatter.parse("2017-03-21"));
        populateOfficialMap("KF-TragicKingdom", "https://wiki.killingfloor2.com/index.php?title=The_Tragic_Kingdom","/KFGame/Web/images/maps/KF-TragicKingdom.jpg", dateFormatter.parse("2017-06-13"));
        populateOfficialMap("KF-Nightmare", "https://wiki.killingfloor2.com/index.php?title=Nightmare","/KFGame/Web/images/maps/KF-Nightmare.jpg", dateFormatter.parse("2017-10-17"));
        populateOfficialMap("KF-KrampusLair", "https://wiki.killingfloor2.com/index.php?title=Krampus_Lair","/KFGame/Web/images/maps/KF-KrampusLair.jpg", dateFormatter.parse("2017-12-12"));
        populateOfficialMap("KF-DieSector", "https://wiki.killingfloor2.com/index.php?title=DieSector","/KFGame/Web/images/maps/KF-DieSector.jpg", dateFormatter.parse("2017-12-12"));
        populateOfficialMap("KF-PowerCore_Holdout", "https://wiki.killingfloor2.com/index.php?title=Powercore","/KFGame/Web/images/maps/KF-PowerCore_Holdout.jpg", dateFormatter.parse("2017-12-12"));
        populateOfficialMap("KF-Airship", "https://wiki.killingfloor2.com/index.php?title=Airship","/KFGame/Web/images/maps/KF-Airship.jpg", dateFormatter.parse("2018-06-12"));
        populateOfficialMap("KF-Lockdown", "https://wiki.killingfloor2.com/index.php?title=Lockdown","/KFGame/Web/images/maps/KF-Lockdown.jpg", dateFormatter.parse("2018-06-12"));
        populateOfficialMap("KF-MonsterBall", "https://wiki.killingfloor2.com/index.php?title=Monster_Ball","/KFGame/Web/images/maps/KF-MonsterBall.jpg", dateFormatter.parse("2018-10-02"));
        populateOfficialMap("KF-SantasWorkshop", "https://wiki.killingfloor2.com/index.php?title=Santa%27s_Workshop","/KFGame/Web/images/maps/KF-SantasWorkshop.jpg", dateFormatter.parse("2018-12-04"));
        populateOfficialMap("KF-ShoppingSpree", "https://wiki.killingfloor2.com/index.php?title=Shopping_Spree","/KFGame/Web/images/maps/KF-ShoppingSpree.jpg", dateFormatter.parse("2018-12-04"));
        populateOfficialMap("KF-Spillway", "https://wiki.killingfloor2.com/index.php?title=Spillway","/KFGame/Web/images/maps/KF-Spillway.jpg", dateFormatter.parse("2019-03-26"));
        populateOfficialMap("KF-SteamFortress", "https://wiki.killingfloor2.com/index.php?title=Steam_Fortress","/KFGame/Web/images/maps/KF-SteamFortress.jpg", dateFormatter.parse("2019-06-18"));
        populateOfficialMap("KF-AshwoodAsylum", "https://wiki.killingfloor2.com/index.php?title=Ashwood_Asylum","/KFGame/Web/images/maps/KF-AshwoodAsylum.jpg", dateFormatter.parse("2019-10-01"));
        populateOfficialMap("KF-Sanitarium", "https://wiki.killingfloor2.com/index.php?title=Sanitarium","/KFGame/Web/images/maps/KF-Sanitarium.jpg", dateFormatter.parse("2019-12-10"));
        populateOfficialMap("KF-Biolapse", "https://wiki.killingfloor2.com/index.php?title=Biolapse","/KFGame/Web/images/maps/KF-Biolapse.jpg", dateFormatter.parse("2020-03-24"));
        populateOfficialMap("KF-Desolation", "https://wiki.killingfloor2.com/index.php?title=Desolation","/KFGame/Web/images/maps/KF-Desolation.jpg", dateFormatter.parse("2020-06-08"));
        populateOfficialMap("KF-HellmarkStation", "https://wiki.killingfloor2.com/index.php?title=Hellmark_Station","/KFGame/Web/images/maps/KF-HellmarkStation.jpg", dateFormatter.parse("2020-09-29"));
        populateOfficialMap("KF-Elysium", "https://wiki.killingfloor2.com/index.php?title=Elysium","/KFGame/Web/images/maps/KF-Elysium.jpg", dateFormatter.parse("2020-12-08"));
        populateOfficialMap("KF-Dystopia2029", "https://wiki.killingfloor2.com/index.php?title=Dystopia_2029","/KFGame/Web/images/maps/KF-Dystopia2029.jpg", dateFormatter.parse("2021-03-23"));
        populateOfficialMap("KF-Moonbase", "https://wiki.killingfloor2.com/index.php?title=Moonbase","/KFGame/Web/images/maps/KF-Moonbase.jpg", dateFormatter.parse("2021-06-22"));
        populateOfficialMap("KF-Netherhold", "https://wiki.killingfloor2.com/index.php?title=Netherhold","/KFGame/Web/images/maps/KF-Netherhold.jpg", dateFormatter.parse("2021-10-05"));
    }

    @Override
    protected void populateProfiles() throws Exception {

        Optional<Language> languageOptional = LanguageDao.getInstance().findByCode("en");
        Optional<GameType> gametypeOptional = GameTypeDao.getInstance().findByCode("KFGameContent.KFGameInfo_Survival");
        Optional<OfficialMap> officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-BurningParis");
        Optional<Difficulty> difficultyOptional = DifficultyDao.getInstance().findByCode("0");
        Optional<Length> lengthOptional = LengthDao.getInstance().findByCode("0");
        Optional<MaxPlayers> maxPlayersOptional = MaxPlayersDao.getInstance().findByCode("6");

        if (languageOptional.isPresent() && gametypeOptional.isPresent() && officialMapOptional.isPresent() && difficultyOptional.isPresent() && lengthOptional.isPresent() && maxPlayersOptional.isPresent()) {
            populateProfile(
                    "Default",
                    languageOptional.get(),
                    gametypeOptional.get(),
                    officialMapOptional.get(),
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
                    null,
                    null,
                    null,
                    new ArrayList<PlatformProfileMap>(),
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

    private void prepareAndPopulatePlatformProfileMap(
            Optional<Profile> profileOptional,
            Optional<OfficialMap> officialMapOptional,
            Optional<Platform> steamPlatformOptional,
            Optional<Platform> epicPlatformOptional
    ) throws Exception {

        if (!profileOptional.isPresent() && !officialMapOptional.isPresent()) {
            throw new RuntimeException("The relation between the profile <NOT FOUND> and the map <NOT FOUND> could not be persisted to database in populate process");
        }
        if (!profileOptional.isPresent()) {
            throw new RuntimeException("The relation between the profile <NOT FOUND> and the map '" + officialMapOptional.get().getDescription() + "' could not be persisted to database in populate process");
        }
        if (!officialMapOptional.isPresent()) {
            throw new RuntimeException("The relation between the profile '" + profileOptional.get().getDescription() + "' and the map <NOT FOUND> could not be persisted to database in populate process");
        }

        if (steamPlatformOptional.isPresent()) {
            populatePlatformProfileMap(steamPlatformOptional.get(), profileOptional.get(), officialMapOptional.get(), officialMapOptional.get().getReleaseDate(), officialMapOptional.get().getUrlInfo(), officialMapOptional.get().getUrlPhoto());
        }
        if (epicPlatformOptional.isPresent()) {
            populatePlatformProfileMap(epicPlatformOptional.get(), profileOptional.get(), officialMapOptional.get(), officialMapOptional.get().getReleaseDate(), officialMapOptional.get().getUrlInfo(), officialMapOptional.get().getUrlPhoto());
        }
    }

    @Override
    protected void populatePlatformProfileMapList() throws Exception {
        Optional<Profile> profileOptional = ProfileDao.getInstance().findByCode("Default");
        Optional<Platform> steamPlatformOptional = PlatformDao.getInstance().findByCode(EnumPlatform.STEAM.name());
        Optional<Platform> epicPlatformOptional = PlatformDao.getInstance().findByCode(EnumPlatform.EPIC.name());

        Optional<OfficialMap> officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-BurningParis");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-BioticsLab");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Outpost");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-VolterManor");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Catacombs");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-EvacuationPoint");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Farmhouse");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-BlackForest");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Prison");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-ContainmentStation");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-HostileGrounds");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-InfernalRealm");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-ZedLanding");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Nuked");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-TheDescent");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-TragicKingdom");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Nightmare");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-KrampusLair");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-DieSector");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-PowerCore_Holdout");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Airship");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Lockdown");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-MonsterBall");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-SantasWorkshop");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-ShoppingSpree");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Spillway");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-SteamFortress");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-AshwoodAsylum");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Sanitarium");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Biolapse");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Desolation");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-HellmarkStation");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Elysium");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Dystopia2029");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Moonbase");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);

        officialMapOptional = OfficialMapDao.getInstance().findByCode("KF-Netherhold");
        prepareAndPopulatePlatformProfileMap(profileOptional, officialMapOptional, steamPlatformOptional, epicPlatformOptional);
    }

    @Override
    protected void populatePlatforms() throws SQLException {
        populatePlatform(EnumPlatform.STEAM);
        populatePlatform(EnumPlatform.EPIC);
    }

}
