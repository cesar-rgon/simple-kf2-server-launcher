package pojos.kf2factory;

import constants.Constants;
import entities.Map;
import entities.Profile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import services.DatabaseService;
import services.DatabaseServiceImpl;
import utils.Utils;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

public abstract class Kf2Common {

    protected final DatabaseService databaseService;

    protected Kf2Common() {
        super();
        databaseService = new DatabaseServiceImpl();
    }

    public void installOrUpdateServer(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) {
        if (prepareSteamCmd(installationFolder)) {
            installUpdateKf2Server(installationFolder, validateFiles, isBeta, betaBrunch);
            checkForNewOfficialMaps(installationFolder);
        } else {
            Utils.errorDialog("Error preparing SteamCmd to be able to install KF2 server", "The installation process is aborted.", null);
        }
    }

    protected abstract boolean prepareSteamCmd(String installationFolder);
    protected abstract void installUpdateKf2Server(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch);

    public String runServer(Profile profile) {
        try {
            String errorMessage = validateParameters(profile);
            if (StringUtils.isEmpty(errorMessage)) {
                String installationFolder = databaseService.findPropertyValue(Constants.KEY_INSTALLATION_FOLDER);
                createConfigFolder(installationFolder, profile.getName());
                checkForNewOfficialMaps(installationFolder);
                return runKf2Server(installationFolder, profile);
            } else {
                Utils.errorDialog("Error validating parameters. The server can not be launched!", errorMessage, null);
            }
        } catch (SQLException e) {
            Utils.errorDialog("Error executing Killing Floor 2 server", "See stacktrace for more details", e);
        }
        return null;
    }

    protected String validateParameters(Profile profile) {
        StringBuffer errorMessage = new StringBuffer();

        if (profile == null || StringUtils.isEmpty(profile.getName())) {
            return "The profile name can not be empty.";
        }
        if (profile.getLanguage() == null) {
            errorMessage.append("The language can not be empty.\n");
        }
        if (profile.getGametype() == null) {
            errorMessage.append("The game type can not be empty.\n");
        }
        if (profile.getMap() == null) {
            errorMessage.append("The map can not be empty.\n");
        }
        if (profile.getDifficulty() == null) {
            errorMessage.append("The difficulty can not be empty.\n");
        }
        if (profile.getLength() == null) {
            errorMessage.append("The length can not be empty.\n");
        }
        if (profile.getMaxPlayers() == null) {
            errorMessage.append("The max.players can not be empty.\n");
        }
        if (StringUtils.isEmpty(profile.getServerName())) {
            errorMessage.append("The server name can not be empty.\n");
        }
        return errorMessage.toString();
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
            Utils.errorDialog("Error copying files to profiles's config folder", "See stacktrace for more details", e);
        }
    }

    protected abstract String runKf2Server(String installationFolder, Profile profile);

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

    protected String replaceLinePcServerKFGameIni(String line, Profile profile) {
        String modifiedLine = line;

        if (line.contains("GameDifficulty=")) {
            modifiedLine = "GameDifficulty=" + profile.getDifficulty().getCode();
        }
        if (line.contains("GameLength=")) {
            modifiedLine = "GameLength=" + profile.getLength().getCode();
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
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
        if (line.contains("BannerLink=")) {
            modifiedLine = "BannerLink=" + profile.getUrlImageServer();
        }
        if (line.contains("ClanMotto=")) {
            modifiedLine = "ClanMotto=" + profile.getYourClan();
        }
        if (line.contains("WebsiteLink=")) {
            modifiedLine = "WebsiteLink=" + profile.getYourWebLink();
        }
        if (line.contains("ServerMOTD=")) {
            modifiedLine = "ServerMOTD=" + profile.getWelcomeMessage();
            modifiedLine = modifiedLine.replaceAll("\n","\\\\n");
        }
        return modifiedLine;
    }

    public void joinServer(Profile profile) {
        File steamExeFile = getSteamExeFile();
        if (steamExeFile != null) {
            joinToKf2Server(steamExeFile, profile);
        } else {
            Utils.errorDialog("Error validating Steam installation directory", "The process is aborted.", null);
        }
    }

    protected abstract File getSteamExeFile();

    protected void joinToKf2Server(File steamExeFile, Profile profile) {
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
            command.append(" -applaunch 232090 127.0.0.1").append(gamePortParam).append(passwordParam).append(" -nostartupmovies");
            Runtime.getRuntime().exec(command.toString(),null, steamExeFile.getParentFile());
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }

    }

    protected void addCustomMapToKfEngineIni(Long idWorkShop, String installationFolder, String filename) {
        try {
            List<Profile> profileList = databaseService.listAllProfiles();
            if (profileList != null && !profileList.isEmpty()) {
                for (Profile profile: profileList) {
                    File kfEngineIni = new File(installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename);
                    BufferedReader br = new BufferedReader(new FileReader(kfEngineIni));
                    String strTempFile = installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename + ".tmp";
                    File tempFile = new File(strTempFile);
                    PrintWriter pw = new PrintWriter(new FileWriter(strTempFile));
                    String line;
                    boolean firstDownloadManager = true;
                    boolean customMapAdded = false;
                    while ((line = br.readLine()) != null) {
                        if (firstDownloadManager) {
                            if (StringUtils.isNotBlank(line) && line.contains("DownloadManagers=")) {
                                firstDownloadManager = false;
                                if (!line.contains("DownloadManagers=OnlineSubsystemSteamworks.SteamWorkshopDownload")) {
                                    pw.println("DownloadManagers=OnlineSubsystemSteamworks.SteamWorkshopDownload");
                                }
                            }
                            pw.println(line);
                            if (StringUtils.isNotBlank(line) && line.contains("[OnlineSubsystemSteamworks.KFWorkshopSteamworks]")) {
                                pw.println("ServerSubscribedWorkshopItems=" + idWorkShop);
                                customMapAdded = true;
                            }
                        } else {
                            if (StringUtils.isBlank(line) || !line.contains("DownloadManagers=OnlineSubsystemSteamworks.SteamWorkshopDownload")) {
                                pw.println(line);
                                if (StringUtils.isNotBlank(line) && line.contains("[OnlineSubsystemSteamworks.KFWorkshopSteamworks]")) {
                                    pw.println("ServerSubscribedWorkshopItems=" + idWorkShop);
                                    customMapAdded = true;
                                }
                            }
                        }
                    }
                    if (!customMapAdded) {
                        pw.println("\n[OnlineSubsystemSteamworks.KFWorkshopSteamworks]");
                        pw.println("ServerSubscribedWorkshopItems=" + idWorkShop);
                    }
                    br.close();
                    pw.close();
                    kfEngineIni.delete();
                    tempFile.renameTo(kfEngineIni);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    public abstract void addCustomMapToKfEngineIni(Long idWorkShop, String installationFolder);

    protected void removeCustomMapsFromKfEngineIni(List<Long> idWorkShopList, String installationFolder, String filename) {
        try {
            List<Profile> profileList = databaseService.listAllProfiles();
            if (profileList != null && !profileList.isEmpty()) {
                for (Profile profile : profileList) {
                    File kfEngineIni = new File(installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename);
                    BufferedReader br = new BufferedReader(new FileReader(kfEngineIni));
                    String strTempFile = installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename + ".tmp";
                    File tempFile = new File(strTempFile);
                    PrintWriter pw = new PrintWriter(new FileWriter(strTempFile));
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (line.contains("ServerSubscribedWorkshopItems=")) {
                            String[] array = line.split("=");
                            Long idWorkshop = Long.parseLong(array[1]);
                            if (idWorkShopList.contains(idWorkshop)) {
                                idWorkShopList.remove(idWorkshop);
                            } else {
                                pw.println(line);
                            }
                        } else {
                            pw.println(line);
                        }
                    }
                    br.close();
                    pw.close();
                    kfEngineIni.delete();
                    tempFile.renameTo(kfEngineIni);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    public abstract void removeCustomMapsFromKfEngineIni(List<Long> idWorkShopList, String installationFolder);

    private String generateMapCycleLine(List<Map> mapList) {
        StringBuffer sb = new StringBuffer("GameMapCycles=(Maps=(");
        if (!mapList.isEmpty()) {
            for (Map map: mapList) {
                sb.append("\"").append(map.getCode()).append("\"");
                if (mapList.indexOf(map) < (mapList.size() - 1)) {
                    sb.append(",");
                }
            }
        }
        sb.append("))");
        return sb.toString();
    }

    protected void addCustomMapsToKfGameIni(List<String> mapNameList, String installationFolder, List<Map> mapList, String filename) {
        try {
            List<Profile> profileList = databaseService.listAllProfiles();
            if (profileList != null && !profileList.isEmpty()) {
                for (Profile profile : profileList) {
                    File kfGameIni = new File(installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename);
                    BufferedReader br = new BufferedReader(new FileReader(kfGameIni));
                    String strTempFile = installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename + ".tmp";
                    File tempFile = new File(strTempFile);
                    PrintWriter pw = new PrintWriter(new FileWriter(strTempFile));
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (StringUtils.isNotBlank(line) && line.contains("GameMapCycles=(Maps=(")) {
                            pw.println(generateMapCycleLine(mapList));
                            continue;
                        }
                        pw.println(line);
                    }
                    for (String mapName: mapNameList) {
                        pw.println("[" + mapName + " KFMapSummary]");
                        pw.println("MapName=" + mapName);
                        pw.println("");
                    }
                    br.close();
                    pw.close();
                    kfGameIni.delete();
                    tempFile.renameTo(kfGameIni);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    public abstract void addCustomMapsToKfGameIni(List<String> mapNameList, String installationFolder, List<Map> mapList);

    protected void removeCustomMapsFromKfGameIni(List<String> mapNameList, String installationFolder, List<Map> mapList, String filename) {
        try {
            List<Profile> profileList = databaseService.listAllProfiles();
            if (profileList != null && !profileList.isEmpty()) {
                for (Profile profile : profileList) {
                    File kfGameIni = new File(installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename);
                    BufferedReader br = new BufferedReader(new FileReader(kfGameIni));
                    String strTempFile = installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename + ".tmp";
                    File tempFile = new File(strTempFile);
                    PrintWriter pw = new PrintWriter(new FileWriter(strTempFile));
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (StringUtils.isNotBlank(line)) {
                            if (line.contains("GameMapCycles=(Maps=(")) {
                                pw.println(generateMapCycleLine(mapList));
                                continue;
                            }
                            if (line.contains(" KFMapSummary]")) {
                                String[] array = line.split(" ");
                                String mapName = array[0].replace("[","");
                                if (mapNameList.contains(mapName)) {
                                    continue;
                                }
                            }
                            if (line.contains("MapName=")) {
                                String[] array = line.split("=");
                                String mapName = array[1];
                                if (mapNameList.contains(mapName)) {
                                    continue;
                                }
                            }
                        }
                        pw.println(line);
                    }
                    br.close();
                    pw.close();
                    kfGameIni.delete();
                    tempFile.renameTo(kfGameIni);
                }
            }
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    public abstract void removeCustomMapsFromKfGameIni(List<String> mapNameList, String installationFolder, List<Map> mapList);

    protected void checkForNewOfficialMaps(String installationFolder, String filename) {
        try {
            List<Map> officialMapList = databaseService.listOfficialMaps();
            File kfGameIni = new File(installationFolder + "/KFGame/Config/" + filename);
            BufferedReader br = new BufferedReader(new FileReader(kfGameIni));
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlank(line) && line.contains("GameMapCycles=(Maps=(")) {
                    String[] array = line.replace("GameMapCycles=(Maps=(", "").replace("))", "").replaceAll("\"", "").split(",");
                    if (array != null && array.length > 0) {
                        for (String value: array) {
                            if (value.startsWith("KF-")) {
                                // It's an official map
                                String mapInMapCycle = value;
                                boolean found = false;
                                for (Map databaseMap: officialMapList) {
                                    if (databaseMap.getCode().equalsIgnoreCase(mapInMapCycle)) {
                                        officialMapList.remove(databaseMap);
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    // It's a new map, it must be stored in database
                                    Map newOfficialMap = new Map(mapInMapCycle, true, null, null, "/KFGame/Web/images/maps/" + mapInMapCycle + ".jpg", true);
                                    databaseService.insertMap(newOfficialMap);
                                }
                            }
                        }
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
    }

    public abstract void checkForNewOfficialMaps(String installationFolder);
}
