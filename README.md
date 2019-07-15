![Logo](src/main/resources/images/documentation/kf2banner.png)

Application to easily customize and launch a Killing Floor 2 server through a visual interface instead of edditing batch files or server's config files. It has been developed with Java, so the interface is compatible on Windows and Linux OS.

```
Version: 2.0 beta
Last modification date: 2019/07/14
Supported OS: Microsoft Windows and Linux (Ubuntu/Debian)
Author: César Rodríguez González
Language: English (Spanish soon)
```
*Main page on Windows OS*
![Launcher screenshot](src/main/resources/images/documentation/screenshot00.png)

*Main page on Linux OS*
![Launcher screenshot](src/main/resources/images/documentation/screenshot01.png)

##### Index
> 1. [Pre-requisites](#pre-requisites)
> 2. [Installing and running the launcher](#installing-and-running-the-launcher)
>   - [On Windows OS](#on-windows-os)
>   - [On Linux OS](#on-linux-os)
> 3. [Understanding the launcher](#understanding-the-launcher)
>   - [Main page](#main-page)


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

### Understanding the launcher

##### Main page
![Launcher screenshot](src/main/resources/images/documentation/screenshot01.png)

**Profile**: This combo is mandatory. It allows to customize filter values by profile name.

**Language**: This combo is mandatory. You can not select any language right now. Only english translation is available at this moment.

**Game Type**: This combo is mandatory. It allows you to select one specific game type.

**Map**: This combo is mandatory. It allows you to select one specific official map or custom map. *NOTE: Custom maps are visible in this field only if they were already downloaded before*

**Difficulty**: This combo is mandatory. It allows you to select one specific difficulty level.

**Length**: This combo is mandatory. It allows you to select one specific number of waves.

**Max. players**: This field is mandatory. It allows you to select one specific maximun number of players in a match.

**Server name**: This field is mandatory. It must contain at least one character.

**Server password**: This field is optional. It allows you to protect your server with a password (needed to known to be able to join the game).

**Web page**: If web page check is enabled you can manage the server through WebAdmin page. Killing Floor 2 server must be launched before you can access WebAdmin page.

**Web password**: This field is optional. It allows you to protect WebAdmin page with a password to login.

**Ports**: Ports are optional. You need to open ports in your router and firewall. If more than one server is launched, ports must be different between them (one profile per server configuration).

**Your clan**: This field is optional.

**Your web link**: This field is optional

**URL image server**: This field is optional. This link must return an uploaded image to internet and it will be used as a preview image in your Killing Floor 2 server. Format and resolution must be PNG 512x256 pixels.

**Welcome message**: This field is optional. It's a welcome message in starting screen of the server.

**Custom parameters**: This field is optional. It defines additional parameters. The format must be: parameter1=value1?parameter2=value2?...?parameterN=valueN

**Console**: This field is automatically filled when the server is running. It's composed by console commands to execute the server.

**Run server**: Run a Killing Floor 2 server with the specified filters. All mandatory fields must be specified. Server config files are placed in folder: KFGame/Config/PROFILENAME. So, the original config files placed in folder: KFGame/Config are never modified.

**Join server**: Join to a Killing Floor 2 server game previously started. If the server has not been started, the operation will start the game but it will not join to any sever. Pre-requisites: Steam application and Killing Floor 2 client game must be installed.

![Work in progress](src/main/resources/images/documentation/work-in-progress.png)
*The documentation is not completed yet*.


**More screenshots**
![Launcher screenshot](src/main/resources/images/documentation/screenshot02.png)

![Launcher screenshot](src/main/resources/images/documentation/screenshot03.png)

![Launcher screenshot](src/main/resources/images/documentation/screenshot04.png)

![Launcher screenshot](src/main/resources/images/documentation/screenshot05.png)

![Launcher screenshot](src/main/resources/images/documentation/screenshot06.png)

![Launcher screenshot](src/main/resources/images/documentation/screenshot07.png)

![Launcher screenshot](src/main/resources/images/documentation/screenshot08.png)

![Launcher screenshot](src/main/resources/images/documentation/screenshot09.png)
