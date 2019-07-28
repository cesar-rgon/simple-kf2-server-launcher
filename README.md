![Logo](src/main/resources/images/documentation/kf2banner.png)

Application to easily customize and launch a Killing Floor 2 server through a visual interface instead of edditing batch files or server's config files. It has been developed with Java, so the interface is compatible on Windows and Linux OS.

### Index
> 1. [Features](#features)
> 2. [Pre-requisites](#pre-requisites)
> 3. [Installing and running the launcher](#installing-and-running-the-launcher)
>   - [On Windows OS](#on-windows-os)
>   - [On Linux OS](#on-linux-os)
> 4. [Quick view](#quick-view)
>   - [Main page](#main-page)
>   - [WebAdmin page](#webadmin-page)
>   - [Install or update page](#install-or-update-page)
>   - [Maps page](#maps-page)
>   - [Configuration-Profiles](#configuration-profiles)
>   - [Configuration-Game types](#configuration-game-types)
>   - [Configuration-Difficulties](#configuration-difficulties)
>   - [Configuration-Length](#configuration-length)
>   - [Configuration-Maximun players](#configuration-maximun-players)
> 5. [Author notes](#author-notes)

### Features

```
Version: 2.0 beta
Last modification date: 2019/08/01
Supported OS: Microsoft Windows and Linux (Ubuntu/Debian)
Author: César Rodríguez González
Language: English (Spanish soon)
```

- Install/Update server to the latest official or beta version.
- Launch the server with specified parameters by the user in launcher's main page.
- Join the running server's match.
- Open WebAdmin page to administrate the running server.
- Add custom maps to the server through Steam's WorkShop.
- Remove custom maps from the server.
- Administrate profiles, game types, difficulties, lengths and maximun numer of players that can be selected to launch the server.
- All those features are available through the launcher's interface on Windows and Linux OS (no need to use console commands at all).

### Pre-requisites
- Internet connection to download, update and publish a Killing Floor 2 server.
- Open needed ports in your router and firewall if you want your server be visible on internet. Needed ports are shown [here](https://wiki.tripwireinteractive.com).
- Install Oracle JRE (Java Runtime Environment) in order to be able to execute the launcher. JRE can be downloaded from [here](https://www.java.com/en/download/).

### Installing and running the launcher

- Download the latest release of the program from [here](https://github.com/cesar-rgon/simple-kf2-server-launcher/releases)
- Extract the contents of the zip file to a local folder.

###### On Windows OS
- Execute the file "SimpleKF2ServerLauncher.jar" just by double click over the file or, as an alternative method, type next commands in a terminal.

*Example*
```
C:\> cd SimpleKF2ServerLauncher-local-folder
C:\SimpleKF2ServerLauncher-local-folder> java -jar SimpleKF2ServerLauncher.jar
```
*NOTE: Replace SimpleKF2ServerLauncher-local-folder for the local folder that you want.*

###### On Linux OS
- Execute the file "SimpleKF2ServerLauncher.jar" with next commands in a terminal:

*Example*
```
$ cd SimpleKF2ServerLauncher-local-folder
$ java -jar SimpleKF2ServerLauncher.jar
```
*NOTE: Replace SimpleKF2ServerLauncher-local-folder for the local folder that you want.*

*NOTE: The JRE must be the Oracle version (not the OpenJDK available in repositories).*

### Quick view

##### Main page
![Launcher screenshot](src/main/resources/images/documentation/screenshot00.png)

![Launcher screenshot](src/main/resources/images/documentation/screenshot01.png)

* _Profile_: This combo is mandatory. It allows to customize filter values by profile name.

* _Language_: This combo is mandatory. You can not select any language right now. Only english translation is available at this moment.

* _Game Type_: This combo is mandatory. It allows you to select one specific game type.

* _Map_: This combo is mandatory. It allows you to select one specific official map or custom map. *NOTE: Custom maps are visible in this field only if they were already downloaded before*

* _Difficulty_: This combo is mandatory. It allows you to select one specific difficulty level.

* _Length_: This combo is mandatory. It allows you to select one specific number of waves.

* _Max. players_: This field is mandatory. It allows you to select one specific maximun number of players in a match.

* _Server name_: This field is mandatory. It must contain at least one character.

* _Server password_: This field is optional. It allows you to protect your server with a password (needed to known to be able to join the game).

* _Web page_: If web page check is enabled you can manage the server through WebAdmin page. Killing Floor 2 server must be launched before you can access WebAdmin page.

* _Web password_: This field is optional. It allows you to protect WebAdmin page with a password to login.

* _Ports_: Ports are optional. You need to open ports in your router and firewall. If more than one server is launched, ports must be different between them (one profile per server configuration).

* _Your clan_: This field is optional.

* _Your web link_: This field is optional

* _URL image server_: This field is optional. This link must return an uploaded image to internet and it will be used as a preview image in your Killing Floor 2 server. Format and resolution must be PNG 512x256 pixels.

* _Welcome message_: This field is optional. It's a welcome message in starting screen of the server.

* _Custom parameters_: This field is optional. It defines additional parameters. The format must be: parameter1=value1?parameter2=value2?...?parameterN=valueN

* _Console_: This field is automatically filled when the server is running. It's composed by console commands to execute the server.

* _Run server_: Run a Killing Floor 2 server with the specified filters. All mandatory fields must be specified. Server config files are placed in folder: KFGame/Config/PROFILENAME. So, the original config files placed in folder: KFGame/Config are never modified.

* _Join server_: Join to a Killing Floor 2 server game previously started. If the server has not been started, the operation will start the game but it will not join to any sever. Pre-requisites: Steam application and Killing Floor 2 client game must be installed.

##### WebAdmin page
In this section you can access to WebAdmin page only if the server is already started and web page is checked in Main Page of the application.
WebAdmin allows you to manage and control the Killing Floor 2 server.

![Launcher screenshot](src/main/resources/images/documentation/screenshot02.png)

##### Install or update page
In this section you can install or update a Killing Floor 2 server.

![Launcher screenshot](src/main/resources/images/documentation/screenshot03.png)

* _Installation folder_: This field is mandatory. Needed to find the local folder where the server must be installed.

* _Validate files_: This field is optional. If enabled, the install/update operation checks the integrity of the server's files. Slow operation.

* _Update to beta?_: This field is optional. If enabled, the install/update operation updates to a beta version of the game.

* _Beta brunch_: This field is optional. The user specifies the name of the beta brunch (only used when last check was enabled).

##### Maps page
In this section you can view official maps and custom maps, as well as, add or remove custom maps from Steam's WorkShop.

![Launcher screenshot](src/main/resources/images/documentation/screenshot04.png)

* _Search_: Filter to search maps by name while you are writing the text.

* _Add new map_: Adds a new map from Steam's WorkShop by workshop's Id or Url.

* _Remove selected maps_: You can select one or multiple custom maps and then remove them by pressing this button.

* _View_: It allows change de view mode: 1. View official and custom maps; 2. View only official maps; 3. View only custom maps.

* _Size_: It allows change the thumbnail size of the maps.

##### Configuration-Profiles
In this section you can add, edit or remove profiles to the launcher. Each profile is used to customize your server parameters.

![Launcher screenshot](src/main/resources/images/documentation/screenshot05.png)

* _Add new profile_: It allows to add a new profile with no name duplicated.
* _Double click on a profile name_: It allows to edit the profile name.
* _Remove selected profile_: It allows to remove the selected profile.

##### Configuration-Game types
In this section you can add, edit or remove game types to the launcher. Each game type is composed by two fields:
1. _Code_: Code to identify the game type mode. It's not a free text, it has to be exactly like described in the Killing Floor 2 server documentation.
2. _Description_: Free text to identify the game type mode.

![Launcher screenshot](src/main/resources/images/documentation/screenshot06.png)

* _Add new game type_: It allows to add a new profile with no code duplicated.
* _Double click on a game type code or description_: It allows to edit the field.
* _Remove selected game type_: It allows to remove the selected game type code and description.

##### Configuration-Difficulties
In this section you can add, edit or remove difficulties to the launcher. Each difficulty is composed by two fields:
1. _Code_: Code to identify the difficulty mode. It's not a free text, it has to be exactly like described in the Killing Floor 2 server documentation.
2. _Description_: Free text to identify the difficulty mode.

![Launcher screenshot](src/main/resources/images/documentation/screenshot07.png)

* _Add new difficulty_: It allows to add a new difficulty with no code duplicated.
* _Double click on a difficulty code or description_: It allows to edit the field.
* _Remove selected difficulty_: It allows to remove the selected difficulty code and description.

##### Configuration-Length
In this section you can add, edit or remove length modes to the launcher. Each length is composed by two fields:
1. _Code_: Code to identify the length mode. It's not a free text, it has to be exactly like described in the Killing Floor 2 server documentation.
2. _Description_: Free text to identify the length mode.

![Launcher screenshot](src/main/resources/images/documentation/screenshot08.png)

* _Add new length_: It allows to add a new length with no code duplicated.
* _Double click on a length code or description_: It allows to edit the field.
* _Remove selected length_: It allows to remove the selected length code and description.

##### Configuration-Maximun players
In this section you can add, edit or remove the max.players to the launcher. Each max.players is composed by two fields:
1. _Code_: Code to identify the max.players. It's not a free text, it has to be exactly like described in the Killing Floor 2 server documentation.
2. _Description_: Free text to identify the max.players.

![Launcher screenshot](src/main/resources/images/documentation/screenshot09.png)

* _Add new max.players_: It allows to add a new max.players with no code duplicated.
* _Double click on a max.players code or description_: It allows to edit the field.
* _Remove selected max.players_: It allows to remove the selected max.players code and description.

### Author notes
I hope you can find useful this application.

By a gamer for gamers :)
