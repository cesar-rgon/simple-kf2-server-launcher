-----------------------------------
-- LANGUAGES
-----------------------------------
INSERT INTO KF2DATABASE.LANGUAGES VALUES(1, 'en', 'English');
INSERT INTO KF2DATABASE.LANGUAGES VALUES(2, 'es', 'Español');

-----------------------------------
-- DIFFICULTIES
-----------------------------------
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (1000, '0');
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (1001, '1');
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (1002, '2');
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (1003, '3');

-----------------------------------
-- GAME TYPES
-----------------------------------
INSERT INTO KF2DATABASE.GAME_TYPES(ID, CODE, DIFFICULTY_ENABLED, LENGTH_ENABLED) VALUES (1000, 'KFGameContent.KFGameInfo_Survival', true, true);
INSERT INTO KF2DATABASE.GAME_TYPES(ID, CODE, DIFFICULTY_ENABLED, LENGTH_ENABLED) VALUES (1001, 'KFGameContent.KFGameInfo_VersusSurvival', false, false);
INSERT INTO KF2DATABASE.GAME_TYPES(ID, CODE, DIFFICULTY_ENABLED, LENGTH_ENABLED) VALUES (1002, 'KFGameContent.KFGameInfo_WeeklySurvival', false, false);
INSERT INTO KF2DATABASE.GAME_TYPES(ID, CODE, DIFFICULTY_ENABLED, LENGTH_ENABLED) VALUES (1003, 'KFGameContent.KFGameInfo_Endless', true, false);
INSERT INTO KF2DATABASE.GAME_TYPES(ID, CODE, DIFFICULTY_ENABLED, LENGTH_ENABLED) VALUES (1004, 'KFGameContent.KFGameInfo_Objective', true, false);

-----------------------------------
-- LENGTH
-----------------------------------
INSERT INTO KF2DATABASE.LENGTHS VALUES (1000, '0');
INSERT INTO KF2DATABASE.LENGTHS VALUES (1001, '1');
INSERT INTO KF2DATABASE.LENGTHS VALUES (1002, '2');

-----------------------------------
-- MAX PLAYERS
-----------------------------------
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (1000, '6');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (1001, '5');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (1002, '4');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (1003, '3');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (1004, '2');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (1005, '1');

-----------------------------------
-- OFFICIAL MAPS
-----------------------------------
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1000, 'KF-BurningParis', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Burning_Paris','/KFGame/Web/images/maps/KF-BurningParis.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1001, 'KF-BioticsLab', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Biotics_Lab_(Killing_Floor_2)','/KFGame/Web/images/maps/KF-BioticsLab.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1002, 'KF-Outpost', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Outpost','/KFGame/Web/images/maps/KF-Outpost.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1003, 'KF-VolterManor', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Volter_Manor','/KFGame/Web/images/maps/KF-VolterManor.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1004, 'KF-Catacombs', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Catacombs','/KFGame/Web/images/maps/KF-Catacombs.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1005, 'KF-EvacuationPoint', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Evacuation_Point','/KFGame/Web/images/maps/KF-EvacuationPoint.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1006, 'KF-Farmhouse', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Farmhouse','/KFGame/Web/images/maps/KF-Farmhouse.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1007, 'KF-BlackForest', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Black_Forest','/KFGame/Web/images/maps/KF-BlackForest.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1008, 'KF-Prison', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Prison','/KFGame/Web/images/maps/KF-Prison.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1009, 'KF-ContainmentStation', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Containment_Station','/KFGame/Web/images/maps/KF-ContainmentStation.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1010, 'KF-HostileGrounds', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Hostile_Grounds','/KFGame/Web/images/maps/KF-HostileGrounds.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1011, 'KF-InfernalRealm', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Infernal_Realm','/KFGame/Web/images/maps/KF-InfernalRealm.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1012, 'KF-ZedLanding', TRUE, 'https://wiki.killingfloor2.com/index.php?title=ZED_Landing','/KFGame/Web/images/maps/KF-ZedLanding.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1013, 'KF-Nuked', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Nuked','/KFGame/Web/images/maps/KF-Nuked.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1014, 'KF-TheDescent', TRUE, 'https://wiki.killingfloor2.com/index.php?title=The_Descent','/KFGame/Web/images/maps/KF-TheDescent.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1015, 'KF-TragicKingdom', TRUE, 'https://wiki.killingfloor2.com/index.php?title=The_Tragic_Kingdom','/KFGame/Web/images/maps/KF-TragicKingdom.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1016, 'KF-Nightmare', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Nightmare','/KFGame/Web/images/maps/KF-Nightmare.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1017, 'KF-KrampusLair', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Krampus_Lair','/KFGame/Web/images/maps/KF-KrampusLair.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1018, 'KF-DieSector', TRUE, 'https://wiki.killingfloor2.com/index.php?title=DieSector','/KFGame/Web/images/maps/KF-DieSector.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1019, 'KF-PowerCore_Holdout', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Powercore','/KFGame/Web/images/maps/KF-PowerCore_Holdout.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1020, 'KF-Airship', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Airship','/KFGame/Web/images/maps/KF-Airship.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1021, 'KF-Lockdown', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Lockdown','/KFGame/Web/images/maps/KF-Lockdown.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1022, 'KF-MonsterBall', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Monster_Ball','/KFGame/Web/images/maps/KF-MonsterBall.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1023, 'KF-SantasWorkshop', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Santa%27s_Workshop','/KFGame/Web/images/maps/KF-SantasWorkshop.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1024, 'KF-ShoppingSpree', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Shopping_Spree','/KFGame/Web/images/maps/KF-ShoppingSpree.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1025, 'KF-Spillway', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Spillway','/KFGame/Web/images/maps/KF-Spillway.jpg', TRUE, FALSE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED, MOD) VALUES (1026, 'KF-SteamFortress', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Steam_Fortress','/KFGame/Web/images/maps/KF-SteamFortress.jpg', TRUE, FALSE);

-----------------------------------
-- PROFILES
-----------------------------------
INSERT INTO KF2DATABASE.PROFILES(ID, NAME, ID_LANGUAGE, ID_GAMETYPE, ID_MAP, ID_DIFFICULTY, ID_LENGTH, ID_MAXPLAYERS, SERVER_NAME, WEB_PORT, GAME_PORT, QUERY_PORT) VALUES(1000, 'Default', 1, 1000, 1000, 1000, 1000, 1000, 'Killing Floor 2 Server', 8080, 7777, 27015);
