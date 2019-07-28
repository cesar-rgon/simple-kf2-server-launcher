-----------------------------------
-- LANGUAGES
-----------------------------------
INSERT INTO KF2DATABASE.LANGUAGES VALUES(1, 'en', 'English');
INSERT INTO KF2DATABASE.LANGUAGES VALUES(2, 'es', 'Español');

-----------------------------------
-- DIFFICULTIES
-----------------------------------
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (1, '0', 'prop.difficulty.normal');
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (2, '1', 'prop.difficulty.hard');
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (3, '2', 'prop.difficulty.suicidal');
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (4, '3', 'prop.difficulty.hoe');

-----------------------------------
-- GAME TYPES
-----------------------------------
INSERT INTO KF2DATABASE.GAME_TYPES VALUES (1, 'KFGameContent.KFGameInfo_Survival', 'prop.gametype.survival');
INSERT INTO KF2DATABASE.GAME_TYPES VALUES (2, 'KFGameContent.KFGameInfo_VersusSurvival', 'prop.gametype.versus');
INSERT INTO KF2DATABASE.GAME_TYPES VALUES (3, 'KFGameContent.KFGameInfo_WeeklySurvival', 'prop.gametype.weekly');
INSERT INTO KF2DATABASE.GAME_TYPES VALUES (4, 'KFGameContent.KFGameInfo_Endless', 'prop.gametype.endless');
INSERT INTO KF2DATABASE.GAME_TYPES VALUES (5, 'KFGameContent.KFGameInfo_Objective', 'prop.gametype.objetive');

-----------------------------------
-- LENGTH
-----------------------------------
INSERT INTO KF2DATABASE.LENGTHS VALUES (1, '0', 'prop.length.four');
INSERT INTO KF2DATABASE.LENGTHS VALUES (2, '1', 'prop.length.seven');
INSERT INTO KF2DATABASE.LENGTHS VALUES (3, '2', 'prop.length.ten');

-----------------------------------
-- MAX PLAYERS
-----------------------------------
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (1, '6', 'prop.maxplayers.six');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (2, '5', 'prop.maxplayers.five');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (3, '4', 'prop.maxplayers.four');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (4, '3', 'prop.maxplayers.three');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (5, '2', 'prop.maxplayers.two');
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (6, '1', 'prop.maxplayers.one');

-----------------------------------
-- OFFICIAL MAPS
-----------------------------------
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (1, 'KF-BurningParis', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Burning_Paris','/KFGame/Web/images/maps/KF-BurningParis.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (2, 'KF-Bioticslab', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Biotics_Lab_(Killing_Floor_2)','/KFGame/Web/images/maps/KF-Bioticslab.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (3, 'KF-Outpost', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Outpost','/KFGame/Web/images/maps/KF-Outpost.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (4, 'KF-VolterManor', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Volter_Manor','/KFGame/Web/images/maps/KF-VolterManor.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (5, 'KF-Catacombs', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Catacombs','/KFGame/Web/images/maps/KF-Catacombs.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (6, 'KF-EvacuationPoint', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Evacuation_Point','/KFGame/Web/images/maps/KF-EvacuationPoint.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (7, 'KF-Farmhouse', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Farmhouse','/KFGame/Web/images/maps/KF-Farmhouse.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (8, 'KF-BlackForest', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Black_Forest','/KFGame/Web/images/maps/KF-BlackForest.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (9, 'KF-Prison', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Prison','/KFGame/Web/images/maps/KF-Prison.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (10, 'KF-ContainmentStation', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Containment_Station','/KFGame/Web/images/maps/KF-ContainmentStation.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (11, 'KF-HostileGrounds', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Hostile_Grounds','/KFGame/Web/images/maps/KF-HostileGrounds.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (12, 'KF-InfernalRealm', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Infernal_Realm','/KFGame/Web/images/maps/KF-InfernalRealm.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (13, 'KF-ZedLanding', TRUE, 'https://wiki.killingfloor2.com/index.php?title=ZED_Landing','/KFGame/Web/images/maps/KF-ZedLanding.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (14, 'KF-Nuked', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Nuked','/KFGame/Web/images/maps/KF-Nuked.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (15, 'KF-TheDescent', TRUE, 'https://wiki.killingfloor2.com/index.php?title=The_Descent','/KFGame/Web/images/maps/KF-TheDescent.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (16, 'KF-TragicKingdom', TRUE, 'https://wiki.killingfloor2.com/index.php?title=The_Tragic_Kingdom','/KFGame/Web/images/maps/KF-TragicKingdom.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (17, 'KF-Nightmare', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Nightmare','/KFGame/Web/images/maps/KF-Nightmare.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (18, 'KF-KrampusLair', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Krampus_Lair','/KFGame/Web/images/maps/KF-KrampusLair.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (19, 'KF-DieSector', TRUE, 'https://wiki.killingfloor2.com/index.php?title=DieSector','/KFGame/Web/images/maps/KF-DieSector.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (20, 'KF-Powercore_Holdout', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Powercore','/KFGame/Web/images/maps/KF-Powercore_Holdout.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (21, 'KF-Airship', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Airship','/KFGame/Web/images/maps/KF-Airship.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (22, 'KF-Lockdown', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Lockdown','/KFGame/Web/images/maps/KF-Lockdown.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (23, 'KF-MonsterBall', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Monster_Ball','/KFGame/Web/images/maps/KF-MonsterBall.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (24, 'KF-SantasWorkshop', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Santa%27s_Workshop','/KFGame/Web/images/maps/KF-SantasWorkshop.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (25, 'KF-ShoppingSpree', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Shopping_Spree','/KFGame/Web/images/maps/KF-ShoppingSpree.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (26, 'KF-Spillway', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Spillway','/KFGame/Web/images/maps/KF-Spillway.jpg', TRUE);
INSERT INTO KF2DATABASE.MAPS(ID, CODE, OFFICIAL, URL_INFO, URL_PHOTO, DOWNLOADED) VALUES (27, 'KF-SteamFortress', TRUE, 'https://wiki.killingfloor2.com/index.php?title=Steam_Fortress','/KFGame/Web/images/maps/KF-SteamFortress.jpg', TRUE);

-----------------------------------
-- PROFILES
-----------------------------------
INSERT INTO KF2DATABASE.PROFILES(ID, NAME, ID_LANGUAGE, ID_GAMETYPE, ID_MAP, ID_DIFFICULTY, ID_LENGTH, ID_MAXPLAYERS, SERVER_NAME, WEB_PORT, GAME_PORT, QUERY_PORT) VALUES(1, 'Default', 1, 1, 1, 1, 1, 1, 'Killing Floor 2 Server', 8080, 7777, 27015);

