package pojos.kf2factory;

import entities.Map;
import entities.Profile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.DatabaseService;
import services.DatabaseServiceImpl;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

public abstract class Kf2Common {

    private static final Logger logger = LogManager.getLogger(Kf2Common.class);
    protected final DatabaseService databaseService;
    protected final PropertyService propertyService;
    protected String languageCode;
    protected boolean byConsole;

    protected Kf2Common() {
        super();
        databaseService = new DatabaseServiceImpl();
        propertyService = new PropertyServiceImpl();
        byConsole = false;
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    public void installOrUpdateServer(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) {
        if (isValid(installationFolder) && prepareSteamCmd(installationFolder)) {
            installUpdateKf2Server(installationFolder, validateFiles, isBeta, betaBrunch);
        }
    }

    private boolean isValid(String installationFolder) {
        if (StringUtils.isBlank(installationFolder) || installationFolder.contains(" ")) {
            try {
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                if (!byConsole) {
                    Utils.warningDialog(headerText, contentText);
                } else {
                    System.out.println(headerText + "\n" + contentText);
                }
            } catch (Exception e) {
                if (!byConsole) {
                    logger.error(e.getMessage(), e);
                    Utils.errorDialog(e.getMessage(), e);
                } else {
                    System.out.println(e);
                }
            }
            return false;
        } else {
            return true;
        }
    }

    protected abstract boolean prepareSteamCmd(String installationFolder);
    protected abstract void installUpdateKf2Server(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch);

    public String runServer(Profile profile) {
        try {
            String errorMessage = validateParameters(profile);
            if (StringUtils.isEmpty(errorMessage)) {
                PropertyService propertyService = new PropertyServiceImpl();
                String installationFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.installationFolder");
                if (isValid(installationFolder)) {
                    createConfigFolder(installationFolder, profile.getName());
                    return runKf2Server(installationFolder, profile);
                }
            } else {
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.errorParameters");
                if (!byConsole) {
                    Utils.warningDialog(headerText, errorMessage);
                } else {
                    System.out.println(headerText + "\n" + errorMessage);
                }
            }
        } catch (Exception e) {
            String message = "Error executing Killing Floor 2 server";
            if (!byConsole) {
                logger.error(message, e);
                Utils.errorDialog(message, e);
            } else {
                System.out.println(message + "\n" + e);
            }
        }
        return null;
    }

    public String runServerByConsole(Profile profile) {
        byConsole = true;
        return runServer(profile);
    }


    protected String validateParameters(Profile profile) {
        StringBuffer errorMessage = new StringBuffer();
        try {
            if (profile == null || StringUtils.isEmpty(profile.getName())) {
                return propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.profileNotEmpty") + "\n";
            }
            if (profile.getLanguage() == null) {
                errorMessage.append(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.languageNotEmpty")).append("\n");
            }
            if (profile.getGametype() == null) {
                errorMessage.append(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.gameTypeNotEmpty")).append("\n");
            }
            if (profile.getMap() == null) {
                errorMessage.append(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.mapNotEmpty")).append("\n");
            }
            if (profile.getGametype().isDifficultyEnabled() && profile.getDifficulty() == null) {
                errorMessage.append(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.difficultyNotEmpty")).append("\n");
            }
            if (profile.getGametype().isLengthEnabled() && profile.getLength() == null) {
                errorMessage.append(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.lengthNotEmpty")).append("\n");
            }
            if (profile.getMaxPlayers() == null) {
                errorMessage.append(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.maxPlayersNotEmpty")).append("\n");
            }
            if (StringUtils.isEmpty(profile.getServerName())) {
                errorMessage.append(propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.serverNameNotEmpty")).append("\n");
            }
            return errorMessage.toString();
        } catch (Exception e) {
            if (!byConsole) {
                logger.error(e.getMessage(), e);
                Utils.errorDialog(e.getMessage(), e);
            } else {
                System.out.println(e);
            }
            return "Error validating parameters. The server can not be started!";
        }
    }

    public void createConfigFolder(String installationFolder, String profileName) {
        try {
            File configFolder = new File(installationFolder + "/KFGame/Config");
            File profileFolder = new File(configFolder.getAbsolutePath() + "/" + profileName);
            if (!profileFolder.exists() || !profileFolder.isDirectory()) {
                if (profileFolder.mkdirs()) {
                    File[] sourceFiles = configFolder.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            if(name.endsWith(".ini")) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                    for (File sourceFile: sourceFiles) {
                        FileUtils.copyFileToDirectory(sourceFile, profileFolder);
                    }
                }
            }
        } catch (IOException e) {
            String message = "Error copying files to profiles's config folder";
            if (!byConsole) {
                logger.error(message, e);
                Utils.errorDialog(message, e);
            } else {
                System.out.println(message + "\n" + e);
            }
        }
    }

    protected abstract String runKf2Server(String installationFolder, Profile profile);

    protected void replaceInFileKfEngineIni(String installationFolder, Profile profile, String filename) {
        try {
            File kfEngineIni = new File(installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename);
            BufferedReader br = new BufferedReader(new FileReader(kfEngineIni));
            String strTempFile = installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename + ".tmp";
            File tempFile = new File(strTempFile);
            PrintWriter pw = new PrintWriter(new FileWriter(strTempFile));
            String line;
            boolean firstDownloadManager = true;
            while ((line = br.readLine()) != null) {
                if (firstDownloadManager) {
                    if (StringUtils.isNotBlank(line) && line.contains("DownloadManagers=")) {
                        firstDownloadManager = false;
                        if (!line.contains("DownloadManagers=OnlineSubsystemSteamworks.SteamWorkshopDownload")) {
                            pw.println("DownloadManagers=OnlineSubsystemSteamworks.SteamWorkshopDownload");
                        }
                    }
                    if (StringUtils.isBlank(line) || (!line.contains("[OnlineSubsystemSteamworks.KFWorkshopSteamworks]") &&
                            !line.contains("ServerSubscribedWorkshopItems="))) {
                        pw.println(line);
                    }
                } else {
                    if (StringUtils.isBlank(line) || (!line.contains("DownloadManagers=OnlineSubsystemSteamworks.SteamWorkshopDownload") &&
                            !line.contains("[OnlineSubsystemSteamworks.KFWorkshopSteamworks]") && !line.contains("ServerSubscribedWorkshopItems="))) {
                        pw.println(line);
                    }
                }
            }

            List<Map> customMaps = databaseService.listCustomMapsAndMods();
            if (customMaps != null && !customMaps.isEmpty()) {
                pw.println("[OnlineSubsystemSteamworks.KFWorkshopSteamworks]");
                for (Map customMap: customMaps) {
                    pw.println("ServerSubscribedWorkshopItems=" + customMap.getIdWorkShop() + " // " + customMap.getCode());
                }
            }

            br.close();
            pw.close();
            kfEngineIni.delete();
            tempFile.renameTo(kfEngineIni);
        } catch (Exception e) {
            if (!byConsole) {
                logger.error(e.getMessage(), e);
                Utils.errorDialog(e.getMessage(), e);
            } else {
                System.out.println(e);
            }
        }
    }

    protected void replaceInFileKfGameIni(String installationFolder, Profile profile, String filename) {
        try {
            File kfGameIni = new File(installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename);
            BufferedReader br = new BufferedReader(new FileReader(kfGameIni));
            String strTempFile = installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename + ".tmp";
            File tempFile = new File(strTempFile);
            PrintWriter pw = new PrintWriter(new FileWriter(strTempFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlank(line) && line.contains("KFMapSummary]")) {
                    String[] array = line.split(" ");
                    String mapName = array[0].replace("[", "");
                    Map map = databaseService.findMapByName(mapName);
                    if (map != null && map.isOfficial() || line.contains("[KF-Default KFMapSummary]")) {
                        pw.println(line);
                        while (!line.contains("MapName=")) {
                            line = br.readLine();
                        }
                        pw.println(line);
                        while (!line.contains("MapAssociation=")) {
                            line = br.readLine();
                        }
                        pw.println(line);
                        while (!line.contains("ScreenshotPathName=")) {
                            line = br.readLine();
                        }
                        pw.println(line);
                    }
                } else {
                    String modifiedLine = replaceLineKfGameIni(line, profile);
                    if (modifiedLine != null) {
                        pw.println(modifiedLine);
                    }
                }
            }

            List<Map> customMaps = databaseService.listCustomMaps();
            if (customMaps != null && !customMaps.isEmpty()) {
                for (Map customMap: customMaps) {
                    pw.println("[" + customMap.getCode() + " KFMapSummary]");
                    pw.println("MapName=" + customMap.getCode());
                    pw.println("MapAssociation=2");
                    pw.println("ScreenshotPathName=");
                    pw.println("");
                }
            }

            br.close();
            pw.close();
            kfGameIni.delete();
            tempFile.renameTo(kfGameIni);
        } catch (Exception e) {
            if (!byConsole) {
                logger.error(e.getMessage(), e);
                Utils.errorDialog(e.getMessage(), e);
            } else {
                System.out.println(e);
            }
        }
    }

    protected void replaceInFileKfWebIni(String installationFolder, Profile profile, Charset charset) throws Exception {
        String kfWebIniFile = installationFolder + "/KFGame/Config/" + profile.getName() + "/KFWeb.ini";
        StringBuilder contentBuilder = new StringBuilder();
        Path filePath = Paths.get(kfWebIniFile);
        Stream<String> stream = Files.lines( filePath, charset);
        stream.forEach(line -> contentBuilder.append(replaceLineKfWebIni(line, profile)).append("\n"));
        stream.close();
        PrintWriter outputFile = new PrintWriter(kfWebIniFile);
        outputFile.println(contentBuilder.toString());
        outputFile.close();
    }

    protected String replaceLineKfWebIni(String line, Profile profile){
        String modifiedLine = line;
        if (profile.getWebPort() != null && line.contains("ListenPort=")) {
            modifiedLine = "ListenPort=" + profile.getWebPort();
        }
        if (line.contains("bEnabled=")) {
            if (profile.getWebPage() == null || !profile.getWebPage()) {
                modifiedLine = "bEnabled=false";
            } else {
                modifiedLine = "bEnabled=true";
            }
        }
        return modifiedLine;
    }

    protected String replaceLineKfGameIni(String line, Profile profile) {
        String modifiedLine = line;

        if (line.contains("MapName=") || line.contains("MapAssociation=") || line.contains("ScreenshotPathName=")) {
            modifiedLine = null;
        }
        if (line.contains("GameMapCycles=(Maps=(")) {
            try {
                List<Map> mapList = databaseService.listDownloadedMaps();
                modifiedLine = generateMapCycleLine(mapList);
            } catch (SQLException e) {
                modifiedLine = "GameMapCycles=(Maps=())";
                logger.error(e.getMessage(), e);
                Utils.errorDialog(e.getMessage(), e);
            }
        }
        if (line.contains("GameDifficulty=")) {
            modifiedLine = "GameDifficulty=" + profile.getDifficulty().getCode();
        }
        if (line.contains("GameLength=")) {
            modifiedLine = "GameLength=" + (profile.getGametype().isLengthEnabled()? profile.getLength().getCode(): "");
        }
        if (line.contains("ServerName=")) {
            modifiedLine = "ServerName=" + profile.getServerName();
        }
        try {
            if (line.contains("GamePassword=")) {
                modifiedLine = "GamePassword=" + Utils.decryptAES(profile.getServerPassword());
            }
            if (line.contains("AdminPassword=")) {
                String decryptedPassword = Utils.decryptAES(profile.getWebPassword());
                if (StringUtils.isNotEmpty(decryptedPassword)){
                    modifiedLine = "AdminPassword=" + decryptedPassword;
                } else {
                    modifiedLine = "AdminPassword=admin";
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
        if (line.contains("BannerLink=")) {
            modifiedLine = "BannerLink=" + (profile.getUrlImageServer() != null ? profile.getUrlImageServer(): "");
        }
        if (line.contains("ClanMotto=")) {
            modifiedLine = "ClanMotto=" + (profile.getYourClan() != null ? profile.getYourClan(): "");
        }
        if (line.contains("WebsiteLink=")) {
            modifiedLine = "WebsiteLink=" + (profile.getYourWebLink() != null ? profile.getYourWebLink(): "");
        }
        if (line.contains("ServerMOTD=")) {
            modifiedLine = "ServerMOTD=" + (profile.getWelcomeMessage() != null ? profile.getWelcomeMessage(): "");
            modifiedLine = modifiedLine.replaceAll("\n","\\\\n");
        }
        return modifiedLine;
    }

    public String joinServer(Profile profile) {
        File steamExeFile = getSteamExeFile();
        if (steamExeFile != null) {
            return joinToKf2Server(steamExeFile, profile);
        } else {
            try {
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.steamInstallDirInvalid");
                Utils.warningDialog(headerText, contentText);
            } catch (Exception e) {
                Utils.errorDialog(e.getMessage(), e);
            } finally {
                return "";
            }
        }
    }

    protected abstract File getSteamExeFile();

    protected String joinToKf2Server(File steamExeFile, Profile profile) {
        try {
            String serverPassword = Utils.decryptAES(profile.getServerPassword());
            String passwordParam = "";
            if (StringUtils.isNotEmpty(serverPassword)) {
                passwordParam = "?password=" + serverPassword;
            }
            String gamePortParam = "";
            if (profile.getGamePort() != null) {
                gamePortParam = ":" + profile.getGamePort();
            }
            StringBuffer command = new StringBuffer(steamExeFile.getAbsolutePath());
            command.append(" -applaunch 232090 ").append(Utils.getPublicIp()).append(gamePortParam).append(passwordParam).append(" -nostartupmovies");
            Runtime.getRuntime().exec(command.toString(),null, steamExeFile.getParentFile());
            StringBuffer consoleCommand = new StringBuffer();
            if (StringUtils.isBlank(passwordParam)) {
                consoleCommand = command;
            } else {
                consoleCommand.append(steamExeFile.getAbsolutePath()).append(" -applaunch 232090 ").append(Utils.getPublicIp()).append(gamePortParam).append("?password=*****").append(" -nostartupmovies");
            }
            return consoleCommand.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
            return "";
        }

    }

    private String generateMapCycleLine(List<Map> mapList) {
        StringBuffer sb = new StringBuffer("GameMapCycles=(Maps=(");
        boolean showSeparator = true;
        if (!mapList.isEmpty()) {
            sb.append("\"----- OFFICIAL MAPS -----\",");
            for (Map map: mapList) {
                if (showSeparator && !map.isOfficial()) {
                    sb.append("\"----- CUSTOM MAPS -----\",");
                    showSeparator = false;
                }
                sb.append("\"").append(map.getCode()).append("\"");
                if (mapList.indexOf(map) < (mapList.size() - 1)) {
                    sb.append(",");
                }
            }
        }
        sb.append("))");
        return sb.toString();
    }

    public abstract Long getIdWorkShopFromPath(Path path, String installationFolder);
}
