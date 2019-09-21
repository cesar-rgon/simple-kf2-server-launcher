![Logo](src/main/resources/images/documentation/kf2banner.png)

_[Click here to open documentation in spanish | Pulse aquí para abrir documentación en español](LEEME.md)_

---
Application to easily customize and launch a Killing Floor 2 server through a visual interface instead of edditing batch files or server's config files. It has been developed with Java, so the interface is compatible on Windows and Linux OS.

*Example of configuration*

![Example configuration](src/main/resources/images/documentation/screenshot00.png)

*Result after join the server through the game's client*

![In game](src/main/resources/images/documentation/screenshot-in-game.jpg)

### Index
> 1. [Features](#features)
> 2. [Pre-requisites](#pre-requisites)
> 3. [Install and run the launcher](#install-and-run-the-launcher)
>   - [On Windows OS](#on-windows-os)
>   - [On Linux OS](#on-linux-os)
> 4. [Quick view](#quick-view)
>   - [Main page](#main-page)
>   - [WebAdmin page](#webadmin-page)
>   - [Install or update page](#install-or-update-page)
>   - [Maps and Mods page](#maps-and-mods-page)
>   - [Configuration-Profiles](#configuration-profiles)
>   - [Configuration-Game types](#configuration-game-types)
>   - [Configuration-Difficulties](#configuration-difficulties)
>   - [Configuration-Lengths](#configuration-lengths)
>   - [Configuration-Maximum players](#configuration-maximum-players)
> 5. [Console parameters](#console-parameters)
> 6. [Donation](#donation)

## Features

```
Version: 2.0 final
Supported OS: Microsoft Windows and Linux (Ubuntu/Debian)
Author: César Rodríguez González
Languages: English, Spanish, French
```

- Install/Update server to the latest official or beta version.
- Launch the server with specified parameters by the user in launcher's main page.
- Join the running server's match.
- Open WebAdmin page to administrate the running server.
- Add custom maps and mods to the server through Steam's WorkShop.
- Remove custom maps and mods from the server.
- Administrate profiles, game types, difficulties, lengths and maximum number of players that can be selected to launch the server.
- Import official maps, custom maps and mods from the server to the launcher.
- Import profiles from file to the launcher.
- Export profiles from the launcher to file.
- Launch the server through console with no need of interaction with the interface.
- All those features are available through the launcher's interface on Windows and Linux OS.

## Pre-requisites
- Internet connection to download, update and publish a Killing Floor 2 server.
- Open needed ports in your router and firewall if you want your server be visible on internet.

  | Port        | Default  | Protocol  | What this option controls                                                   |
  |-------------|----------|-----------|-----------------------------------------------------------------------------|
  | Game Port   | 7777     | UDP       | This is the main port the game will send connections over                   |
  | Query Port  | 27015    | UDP       | This port is used to communicate with the Steam Master Server               |
  | Web Admin   | 8080     | TCP       | This port is used to connect to your servers web admin page (if turned on)  |
  | Steam Port  | 20560    | UDP       |                                                                             |
  | NTP Port    | 123      | UDP       | Weekly Outbreak Only - Internet time lookup to determine correct Outbreak   |

- Install Oracle JRE 8 (Java Runtime Environment) in order to be able to execute the launcher. JRE can be downloaded from [here](https://www.java.com/en/download/).

## Install and run the launcher

- Download the latest release of the program from [here](https://github.com/cesar-rgon/simple-kf2-server-launcher/releases)
- Extract the content of the zip file to a local folder.

##### On Windows OS
- Execute the file "SimpleKF2ServerLauncher.jar" just by double click over the file or, as an alternative method, type next commands in a terminal.

*Example*
```
C:\> cd SimpleKF2ServerLauncher-local-folder
C:\SimpleKF2ServerLauncher-local-folder> java -jar SimpleKF2ServerLauncher.jar
```
*NOTE: Replace SimpleKF2ServerLauncher-local-folder for the local folder that you want.*

##### On Linux OS
- Execute the file "SimpleKF2ServerLauncher.jar" with next commands in a terminal:

*Example*
```
$ cd SimpleKF2ServerLauncher-local-folder
$ java -jar SimpleKF2ServerLauncher.jar
```
*NOTE: Replace SimpleKF2ServerLauncher-local-folder for the local folder that you want.*

*NOTE: The JRE must be the Oracle version (not the OpenJDK available in repositories).*

## Quick view

#### Main page
![Launcher screenshot](src/main/resources/images/documentation/screenshot01.png)

* _Profile_: This combo is mandatory. It allows to customize filter values by profile name.

* _Language_: This combo is mandatory. It allows to select a language for the launcher's interface. List of available languages are described in the [Features](#features) section of this document. 

* _Game Type_: This combo is mandatory. It allows you to select one specific game type.

* _Map_: This combo is mandatory. It allows you to select one specific official map or custom map. *NOTE: Custom maps are visible in this field only if they were already downloaded before*.

* _Difficulty_: This combo is mandatory. It allows you to select one specific difficulty level.

* _Length_: This combo is mandatory. It allows you to select one specific number of waves.

* _Max. players_: This field is mandatory. It allows you to select one specific maximum number of players in a match.

* _Server name_: This field is mandatory. It must contain at least one character.

* _Server password_: This field is optional. It allows you to protect your server with a password (needed to known to be able to join the game).

* _Web page_: If web page check is enabled you can manage the server through WebAdmin page. Killing Floor 2 server must be launched before you can access WebAdmin page.

* _Web password_: This field is optional. It allows you to protect WebAdmin page with a password to login.

* _Ports_: Ports are optional. You need to open ports in your router and firewall. If more than one server is launched, ports must be different between them (one configuration per profile).

* _Your clan_: This field is optional.

* _Your web link_: This field is optional.

* _URL image server_: This field is optional. This link must return an uploaded image to internet and it will be used as the welcome image in your Killing Floor 2 server. Format and resolution must be PNG 512x256 pixels.

* _Welcome message_: This field is optional. It's a welcome message in starting screen of the server.

* _Custom parameters_: This field is optional. It defines additional parameters. The format must be: [?]parameter1=value1?parameter2=value2?...?parameterN=valueN

  [?] means: optionally you can start parameters by "?"

* _Console_: This field is automatically filled when the server is running. It's composed by console commands to execute the server.

* _Run server_: Run a Killing Floor 2 server with the specified filters. All mandatory fields must be specified. Server config files are placed in folder: KFGame/Config/PROFILENAME. So, the original config files placed in folder: KFGame/Config are never modified.
If more than one profile, you can launch multiple servers at once (one per profile).

* _Join server_: Join to a Killing Floor 2 server game previously started. If the server has not been started, the operation will start the game but it will not join to any sever. Pre-requisites: Steam application and Killing Floor 2 game client must be installed.
If more than one profile, you can select wich server you want to join (one per profile).

#### WebAdmin page
In this section you can access to WebAdmin page only if the server is already started and web page is checked in "Main Page" section of the application.
WebAdmin allows you to manage and control the Killing Floor 2 server.

![Launcher screenshot](src/main/resources/images/documentation/screenshot02.png)

#### Install or update page
In this section you can install or update the Killing Floor 2 server.

![Launcher screenshot](src/main/resources/images/documentation/screenshot03.png)

* _Installation folder_: This field is mandatory. Needed to find the local folder where the server must be installed.

* _Validate files_: This field is optional. If enabled, the install/update operation checks the integrity of the server's files. Slow operation.

* _Update to beta?_: This field is optional. If enabled, the operation updates to a beta version of the game (if available).

* _Beta brunch_: This field is optional. The user specifies the name of the beta brunch (only used when last check was enabled).

#### Maps and Mods page
In this section you can manage custom maps, mods and official maps present in the server.

![Launcher screenshot](src/main/resources/images/documentation/screenshot04.png)

* _Search_: Filter to search maps and mods by name while you are writing the text.

* _Columns_: Slider that allows change the thumbnail size of the maps by the specific number of columns.

* _Add new maps/mods_: It allows to add new custom maps and mods from Steam's WorkShop by WorkShop's Id or URL. If more than one element, use comma as separator.

* _Select all maps/mods_: It allows to select or unselect all custom maps and mods as well as official maps.

* _Delete maps/mods_: It allows to delete all maps and mods previously selected.

* _Import maps/mods_: It allows to import all custom maps, mods and official maps from the server to the launcher. Only the elements not present already in the launcher.

* _Search in WorkShop_: It allows to browse in Workshop's pages looking for new maps and mods. Then you can choose wich of them add to the launcher.

![Launcher screenshot](src/main/resources/images/documentation/screenshot05.png)


#### Configuration-Profiles
In this section you can manage profiles from the launcher. Each profile is used to customize your server parameters.

![Launcher screenshot](src/main/resources/images/documentation/screenshot06.png)

* _Double click on a profile name_: It allows to edit the profile name.
* _Add new profile_: It allows to add a new profile. The profile name must be unique.
* _Clone selected profile_: It allows to duplicate the selected profile to a new profile.
* _Remove selected profile_: It allows to remove the selected profile.
* _Import profiles_: It allows to import profiles from a file to the launcher.
* _Export profiles_: It allows to export profiles from the launcher to a file.

**NOTE**: No profile name duplication accepted in any operation.

#### Configuration-Game types
In this section you can add, edit or remove game types from the launcher. Each game type is composed by four fields:
1. _Code_: Code to identify the game type mode. It's not a free text, it has to be exactly like described in the Killing Floor 2 server documentation.
2. _Description_: Free text to identify the game type mode.
3. _Difficulties enabled_: If difficulties combo is active or not.
4. _Lengths enabled_: If lengths combo is active or not.

![Launcher screenshot](src/main/resources/images/documentation/screenshot07.png)

* _Add new game type_: It allows to add a new game type with no code duplicated.
* _Double click on a game type code or description_: It allows to edit the field.
* _Remove selected game type_: It allows to remove the selected game type code and description.

#### Configuration-Difficulties
In this section you can add, edit or remove difficulties from the launcher. Each difficulty is composed by two fields:
1. _Code_: Code to identify the difficulty mode. It's not a free text, it has to be exactly like described in the Killing Floor 2 server documentation.
2. _Description_: Free text to identify the difficulty mode.

![Launcher screenshot](src/main/resources/images/documentation/screenshot08.png)

* _Add new difficulty_: It allows to add a new difficulty with no code duplicated.
* _Double click on a difficulty code or description_: It allows to edit the field.
* _Remove selected difficulty_: It allows to remove the selected difficulty code and description.

#### Configuration-Lengths
In this section you can add, edit or remove length modes from the launcher. Each length is composed by two fields:
1. _Code_: Code to identify the length mode. It's not a free text, it has to be exactly like described in the Killing Floor 2 server documentation.
2. _Description_: Free text to identify the length mode.

![Launcher screenshot](src/main/resources/images/documentation/screenshot09.png)

* _Add new length_: It allows to add a new length with no code duplicated.
* _Double click on a length code or description_: It allows to edit the field.
* _Remove selected length_: It allows to remove the selected length code and description.

#### Configuration-Maximum players
In this section you can add, edit or remove the max.players from the launcher. Each max.players is composed by two fields:
1. _Code_: Code to identify the max.players. It's not a free text, it has to be exactly like described in the Killing Floor 2 server documentation.
2. _Description_: Free text to identify the max.players.

![Launcher screenshot](src/main/resources/images/documentation/screenshot10.png)

* _Add new max.players_: It allows to add a new max.players with no code duplicated.
* _Double click on a max.players code or description_: It allows to edit the field.
* _Remove selected max.players_: It allows to remove the selected max.players code and description.

## Console parameters
The launcher allows to execute servers through terminal parameters without user interaction with the interface.

Accepted parameters are: *For example*
```
java -jar SimpleKF2ServerLauncher.jar --profiles profileName1 [profileName2 profileName3 ... ]
```
*NOTE: [ ] means that these parameters are optional*

- This command executes one or multiple servers, one per profileName.
- The profile names must be separated by whitespace.
- The profile names must exist in launcher's database.

## Donation
If you find useful this application, you liked it and you want to contribute, you can donate the amount you desire [here](https://www.paypal.me/cesarrgon).

![Paypal logo](src/main/resources/images/documentation/paypal-logo.png)

Thank you very much!
