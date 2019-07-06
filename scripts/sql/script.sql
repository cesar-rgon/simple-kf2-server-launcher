-----------------------------------
-- LANGUAGES
-----------------------------------
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (1, 'English', 'Inglés');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (2, 'Spanish', 'Español');

INSERT INTO KF2DATABASE.LANGUAGES VALUES(1, 'en', 1);
INSERT INTO KF2DATABASE.LANGUAGES VALUES(2, 'es', 2);

-----------------------------------
-- DIFFICULTIES
-----------------------------------
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (3, 'Normal', 'Normal');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (4, 'Hard', 'Dificil');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (5, 'Suicidal', 'Suicida');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (6, 'Hell on Earth', 'Infernal');

INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (1, '0', 3);
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (2, '1', 4);
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (3, '2', 5);
INSERT INTO KF2DATABASE.DIFFICULTIES VALUES (4, '3', 6);

-----------------------------------
-- GAME TYPES
-----------------------------------
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (7, 'Survival', 'Supervivencia');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (8, 'Versus', 'Versus');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (9, 'Weekly', 'Semanal');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (10, 'Endless', 'Sin fin');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (47, 'Objetive', 'Objetivo');

INSERT INTO KF2DATABASE.GAME_TYPES VALUES (1, 'KFGameContent.KFGameInfo_Survival', 7);
INSERT INTO KF2DATABASE.GAME_TYPES VALUES (2, 'KFGameContent.KFGameInfo_VersusSurvival', 8);
INSERT INTO KF2DATABASE.GAME_TYPES VALUES (3, 'KFGameContent.KFGameInfo_WeeklySurvival', 9);
INSERT INTO KF2DATABASE.GAME_TYPES VALUES (4, 'KFGameContent.KFGameInfo_Endless', 10);
INSERT INTO KF2DATABASE.GAME_TYPES VALUES (5, 'KFGameContent.KFGameInfo_Objective', 47);

-----------------------------------
-- LENGTH
-----------------------------------
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (11, '4 waves', '4 oleadas');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (12, '7 waves', '7 oleadas');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (13, '10 waves', '10 oleadas');

INSERT INTO KF2DATABASE.LENGTHS VALUES (1, '0', 11);
INSERT INTO KF2DATABASE.LENGTHS VALUES (2, '1', 12);
INSERT INTO KF2DATABASE.LENGTHS VALUES (3, '2', 13);


-----------------------------------
-- MAX PLAYERS
-----------------------------------
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (14, 'Six', 'Seis');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (15, 'Five', 'Cinco');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (16, 'Four', 'Cuatro');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (17, 'Three', 'Tres');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (18, 'Two', 'Dos');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (19, 'One', 'Uno');

INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (1, '6', 14);
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (2, '5', 15);
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (3, '4', 16);
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (4, '3', 17);
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (5, '2', 18);
INSERT INTO KF2DATABASE.MAX_PLAYERS VALUES (6, '1', 19);


-----------------------------------
-- OFFICIAL MAPS
-----------------------------------
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (20, 'Burning Paris', 'París arde');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (21, 'Bioticslab', 'Laboratorio biótico');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (22, 'Outpost', 'Avanzada');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (23, 'Volter Manor', 'Mansión Volter');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (24, 'Catacombs', 'Catacumbas');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (25, 'Evacuation Point', 'Punto de evacuación');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (26, 'Farmhouse', 'Casa rural');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (27, 'Black Forest', 'Selva negra');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (28, 'Prison', 'Cárcel');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (29, 'Containment Station', 'Puesto de contención');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (30, 'Hostile Grounds', 'Tierra hostil');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (31, 'Infernal Realm', 'Reino infernal');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (32, 'Zed Landing', 'Aterrizaje Zed');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (33, 'Nuked', 'Bombardeado');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (34, 'The Descent', 'El descendiente');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (35, 'The Tragic Kingdom', 'El reino trágico');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (36, 'Nightmare', 'Pesadilla');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (37, 'Krampus Lair', 'Guarida de Krampus');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (38, 'Die Sector', 'Sector mortal');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (39, 'Powercore', 'Núcleo de poder');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (40, 'Airship', 'Aeronave');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (41, 'Lockdown', 'Bloqueo');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (42, 'Monster Ball', 'Baile de monstruos');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (43, 'Santas Workshop', 'El taller de Papa Noel');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (44, 'Shopping Spree', 'Compra compulsiva');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (45, 'Spillway', 'Desagüe');
INSERT INTO KF2DATABASE.DESCRIPTIONS VALUES (46, 'Steam Fortress', 'Steam Fortress');

INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (1, 'KF-BurningParis', 20, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Burning_Paris','/KFGame/Web/images/maps/KF-BurningParis.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (2, 'KF-Bioticslab', 21, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Biotics_Lab_(Killing_Floor_2)','/KFGame/Web/images/maps/KF-Bioticslab.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (3, 'KF-Outpost', 22, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Outpost','/KFGame/Web/images/maps/KF-Outpost.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (4, 'KF-VolterManor', 23, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Volter_Manor','/KFGame/Web/images/maps/KF-VolterManor.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (5, 'KF-Catacombs', 24, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Catacombs','/KFGame/Web/images/maps/KF-Catacombs.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (6, 'KF-EvacuationPoint', 25, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Evacuation_Point','/KFGame/Web/images/maps/KF-EvacuationPoint.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (7, 'KF-Farmhouse', 26, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Farmhouse','/KFGame/Web/images/maps/KF-Farmhouse.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (8, 'KF-BlackForest', 27, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Black_Forest','/KFGame/Web/images/maps/KF-BlackForest.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (9, 'KF-Prison', 28, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Prison','/KFGame/Web/images/maps/KF-Prison.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (10, 'KF-ContainmentStation', 29, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Containment_Station','/KFGame/Web/images/maps/KF-ContainmentStation.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (11, 'KF-HostileGrounds', 30, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Hostile_Grounds','/KFGame/Web/images/maps/KF-HostileGrounds.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (12, 'KF-InfernalRealm', 31, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Infernal_Realm','/KFGame/Web/images/maps/KF-InfernalRealm.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (13, 'KF-ZedLanding', 32, TRUE, 'https://wiki.killingfloor2.com/index.php?title=ZED_Landing','/KFGame/Web/images/maps/KF-ZedLanding.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (14, 'KF-Nuked', 33, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Nuked','/KFGame/Web/images/maps/KF-Nuked.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (15, 'KF-TheDescent', 34, TRUE, 'https://wiki.killingfloor2.com/index.php?title=The_Descent','/KFGame/Web/images/maps/KF-TheDescent.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (16, 'KF-TragicKingdom', 35, TRUE, 'https://wiki.killingfloor2.com/index.php?title=The_Tragic_Kingdom','/KFGame/Web/images/maps/KF-TragicKingdom.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (17, 'KF-Nightmare', 36, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Nightmare','/KFGame/Web/images/maps/KF-Nightmare.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (18, 'KF-KrampusLair', 37, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Krampus_Lair','/KFGame/Web/images/maps/KF-KrampusLair.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (19, 'KF-DieSector', 38, TRUE, 'https://wiki.killingfloor2.com/index.php?title=DieSector','/KFGame/Web/images/maps/KF-DieSector.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (20, 'KF-Powercore_Holdout', 39, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Powercore','/KFGame/Web/images/maps/KF-Powercore_Holdout.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (21, 'KF-Airship', 40, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Airship','/KFGame/Web/images/maps/KF-Airship.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (22, 'KF-Lockdown', 41, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Lockdown','/KFGame/Web/images/maps/KF-Lockdown.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (23, 'KF-MonsterBall', 42, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Monster_Ball','/KFGame/Web/images/maps/KF-MonsterBall.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (24, 'KF-SantasWorkshop', 43, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Santa%27s_Workshop','/KFGame/Web/images/maps/KF-SantasWorkshop.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (25, 'KF-ShoppingSpree', 44, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Shopping_Spree','/KFGame/Web/images/maps/KF-ShoppingSpree.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (26, 'KF-Spillway', 45, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Spillway','/KFGame/Web/images/maps/KF-Spillway.jpg');
INSERT INTO KF2DATABASE.MAPS(ID, CODE, ID_DESCRIPTION, OFFICIAL, URL_INFO, URL_PHOTO) VALUES (27, 'KF-SteamFortress', 46, TRUE, 'https://wiki.killingfloor2.com/index.php?title=Steam_Fortress','/KFGame/Web/images/maps/KF-SteamFortress.jpg');


-----------------------------------
-- PROFILES
-----------------------------------
INSERT INTO KF2DATABASE.PROFILES(ID, NAME, ID_LANGUAGE, ID_GAMETYPE, ID_MAP, ID_DIFFICULTY, ID_LENGTH, ID_MAXPLAYERS, SERVER_NAME, WEB_PORT, GAME_PORT, QUERY_PORT) VALUES(1, 'Default', 1, 1, 1, 1, 1, 1, 'Killing Floor 2 Server', 8080, 7777, 27015);

-----------------------------------
-- PROPERTIES
-----------------------------------
INSERT INTO KF2DATABASE.PROPERTIES VALUES (1, 'prop.key.urlSteamcmd', 'https://steamcdn-a.akamaihd.net/client/installer/steamcmd.zip');
INSERT INTO KF2DATABASE.PROPERTIES VALUES (2, 'prop.key.betaBrunch', 'preview');
INSERT INTO KF2DATABASE.PROPERTIES VALUES (3, 'prop.key.downloadConnectionTimeout', '10000');
INSERT INTO KF2DATABASE.PROPERTIES VALUES (4, 'prop.key.downloadReadTimeout', '30000');
INSERT INTO KF2DATABASE.PROPERTIES VALUES (5, 'prop.key.defaultServername', 'Killing Floor 2 Server');
INSERT INTO KF2DATABASE.PROPERTIES VALUES (6, 'prop.key.defaultWebPort', '8080');
INSERT INTO KF2DATABASE.PROPERTIES VALUES (7, 'prop.key.defaultGamePort', '7777');
INSERT INTO KF2DATABASE.PROPERTIES VALUES (8, 'prop.key.defaultQueryPort', '27015');
