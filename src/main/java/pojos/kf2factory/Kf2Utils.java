package pojos.kf2factory;

import dtos.CustomMapModDto;
import dtos.PlatformProfileMapDto;
import entities.*;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import services.*;
import utils.Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Kf2Utils {

    private static final Logger logger = LogManager.getLogger(Kf2Utils.class);

    protected final PropertyService propertyService;
    protected final EntityManager em;

    protected String languageCode;
    protected boolean byConsole;
    private final CustomMapModServiceImpl customMapService;
    private final AbstractMapService officialMapService;
    private final PlatformProfileMapService platformProfileMapService;

    protected Kf2Utils(EntityManager em) {
        super();
        this.em = em;
        propertyService = new PropertyServiceImpl();
        customMapService = new CustomMapModServiceImpl(em);
        officialMapService = new OfficialMapServiceImpl(em);
        platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        byConsole = false;
        try {
            languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }

    protected void installationFolderNotValid() {
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
    }

    protected String validateParameters(Profile profile) {
        StringBuffer errorMessage = new StringBuffer();
        try {
            if (profile == null || StringUtils.isEmpty(profile.getCode())) {
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
                        if ("DefaultWebAdmin.ini".equalsIgnoreCase(sourceFile.getName())) {
                            File targetFile = new File(configFolder.getAbsolutePath() + "/" + profileName + "/KFWebAdmin.ini");
                            FileUtils.copyFile(sourceFile, targetFile);
                        }
                    }
                }
            }
            File cacheFolder = new File(installationFolder + "/KFGame/Cache");
            if (!cacheFolder.exists()) {
                cacheFolder.mkdir();
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

    protected void replaceInFileKfWebAdminIni(AbstractPlatform platform, Profile profile) {
        try {
            File kfWebAdminIni = new File(platform.getInstallationFolder() + "/KFGame/Config/" + profile.getCode() + "/KFWebAdmin.ini");
            BufferedReader br = new BufferedReader(new FileReader(kfWebAdminIni));
            String strTempFile = platform.getInstallationFolder() + "/KFGame/Config/" + profile.getCode() + "/KFWebAdmin.ini.tmp";
            File tempFile = new File(strTempFile);
            PrintWriter pw = new PrintWriter(new FileWriter(strTempFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlank(line) && line.contains("bChatLog=")) {
                    pw.println("bChatLog=" + (profile.getChatLogging() != null ? profile.getChatLogging() : "False"));
                } else if (StringUtils.isNotBlank(line) && line.contains("bIncludeTimeStamp=")) {
                    pw.println("bIncludeTimeStamp=" + (profile.getChatLoggingFileTimestamp()!= null ? profile.getChatLoggingFileTimestamp() : "False"));
                } else {
                    if (StringUtils.isBlank(line) || !line.contains("Filename=")) {
                        pw.println(line);
                    }
                    if (StringUtils.isNotBlank(line) && line.contains("[WebAdmin.Chatlog]") &&
                            profile.getChatLogging() != null && profile.getChatLogging() && StringUtils.isNotBlank(profile.getChatLoggingFile())) {
                        pw.println("Filename=" + profile.getChatLoggingFile());
                    }
                }
            }

            br.close();
            pw.close();
            kfWebAdminIni.delete();
            tempFile.renameTo(kfWebAdminIni);
        } catch (Exception e) {
            if (!byConsole) {
                logger.error(e.getMessage(), e);
                Utils.errorDialog(e.getMessage(), e);
            } else {
                System.out.println(e);
            }
        }
    }

    protected void replaceInFileKfEngineIni(AbstractPlatform platform, Profile profile, String filename) {
        try {
            File kfEngineIni = new File(platform.getInstallationFolder() + "/KFGame/Config/" + profile.getCode() + "/" + filename);
            BufferedReader br = new BufferedReader(new FileReader(kfEngineIni));
            String strTempFile = platform.getInstallationFolder() + "/KFGame/Config/" + profile.getCode() + "/" + filename + ".tmp";
            File tempFile = new File(strTempFile);
            PrintWriter pw = new PrintWriter(new FileWriter(strTempFile));
            String line;
            boolean firstDownloadManager = true;
            boolean rateParameters = false;
            while ((line = br.readLine()) != null) {
                if (firstDownloadManager) {
                    if (StringUtils.isNotBlank(line) && line.contains("DownloadManagers=")) {
                        firstDownloadManager = false;
                        if (!line.contains("DownloadManagers=OnlineSubsystemSteamworks.SteamWorkshopDownload")) {
                            pw.println("DownloadManagers=OnlineSubsystemSteamworks.SteamWorkshopDownload");
                        }
                    }
                    if (StringUtils.isNotBlank(line) && line.contains("bUsedForTakeover=")) {
                        pw.println("bUsedForTakeover=" + (profile.getTakeover()!=null?profile.getTakeover():"FALSE"));
                    } else if (StringUtils.isNotBlank(line) && line.contains("[IpDrv.TcpNetDriver]")) {
                        pw.println(line);
                        rateParameters = true;
                    } else if (StringUtils.isNotBlank(line) && rateParameters && line.contains("NetServerMaxTickRate=")) {
                        pw.println("NetServerMaxTickRate=" + ((profile.getNetTickrate() != null) ? profile.getNetTickrate(): 30));
                    } else if (StringUtils.isNotBlank(line) && rateParameters && line.contains("LanServerMaxTickRate=")) {
                        pw.println("LanServerMaxTickRate=" + ((profile.getLanTickrate() != null) ? profile.getLanTickrate(): 35));
                    } else if (StringUtils.isNotBlank(line) && rateParameters && line.contains("MaxClientRate=")) {
                        pw.println("MaxClientRate=" + ((profile.getLanMaxClientRate() != null) ? profile.getLanMaxClientRate(): 15000));
                    } else if (StringUtils.isNotBlank(line) && rateParameters && line.contains("MaxInternetClientRate=")) {
                        pw.println("MaxInternetClientRate=" + ((profile.getInternetMaxClientRate() != null) ? profile.getInternetMaxClientRate(): 10000));
                    } else {
                        if (StringUtils.isBlank(line) || (!line.contains("[OnlineSubsystemSteamworks.KFWorkshopSteamworks]") &&
                                !line.contains("ServerSubscribedWorkshopItems="))) {
                            pw.println(line);
                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(line) && line.contains("bUsedForTakeover=")) {
                        pw.println("bUsedForTakeover=" + (profile.getTakeover() != null ? profile.getTakeover() : "FALSE"));
                    } else if (StringUtils.isNotBlank(line) && line.contains("[IpDrv.TcpNetDriver]")) {
                        pw.println(line);
                        rateParameters = true;
                    } else if (StringUtils.isNotBlank(line) && rateParameters && line.contains("NetServerMaxTickRate=")) {
                        pw.println("NetServerMaxTickRate=" + ((profile.getNetTickrate() != null) ? profile.getNetTickrate(): 30));
                    } else if (StringUtils.isNotBlank(line) && rateParameters && line.contains("LanServerMaxTickRate=")) {
                        pw.println("LanServerMaxTickRate=" + ((profile.getLanTickrate() != null) ? profile.getLanTickrate(): 35));
                    } else if (StringUtils.isNotBlank(line) && rateParameters && line.contains("MaxClientRate=")) {
                        pw.println("MaxClientRate=" + ((profile.getLanMaxClientRate() != null) ? profile.getLanMaxClientRate(): 15000));
                    } else if (StringUtils.isNotBlank(line) && rateParameters && line.contains("MaxInternetClientRate=")) {
                        pw.println("MaxInternetClientRate=" + ((profile.getInternetMaxClientRate() != null) ? profile.getInternetMaxClientRate(): 10000));
                    } else {
                        if (StringUtils.isBlank(line) || (!line.contains("DownloadManagers=OnlineSubsystemSteamworks.SteamWorkshopDownload") &&
                                !line.contains("[OnlineSubsystemSteamworks.KFWorkshopSteamworks]") && !line.contains("ServerSubscribedWorkshopItems="))) {
                            pw.println(line);
                        }
                    }
                }
            }

            List<AbstractMap> allCustomMapModList = customMapService.listAllMaps();
            List<AbstractMap> downloadedCustomMapsInMapsCycle = platformProfileMapService.listPlatformProfileMaps(platform, profile).stream()
                    .filter(PlatformProfileMap::isDownloaded)
                    .filter(PlatformProfileMap::isInMapsCycle)
                    .map(PlatformProfileMap::getMap)
                    .filter(allCustomMapModList::contains)
                    .filter(m -> ((CustomMapMod) Hibernate.unproxy(m)).getMap() != null ? ((CustomMapMod) Hibernate.unproxy(m)).getMap(): false)
                    .sorted((m1, m2) -> m1.getCode().compareTo(m2.getCode()))
                    .collect(Collectors.toList());

            if (downloadedCustomMapsInMapsCycle != null && !downloadedCustomMapsInMapsCycle.isEmpty()) {
                pw.println("[OnlineSubsystemSteamworks.KFWorkshopSteamworks]");
                for (AbstractMap map: downloadedCustomMapsInMapsCycle) {
                    CustomMapMod customMap = (CustomMapMod) Hibernate.unproxy(map);
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

    protected void replaceInFileKfGameIni(AbstractPlatform platform, Profile profile, String filename) {
        try {
            File kfGameIni = new File(platform.getInstallationFolder() + "/KFGame/Config/" + profile.getCode() + "/" + filename);
            BufferedReader br = new BufferedReader(new FileReader(kfGameIni));
            String strTempFile = platform.getInstallationFolder() + "/KFGame/Config/" + profile.getCode() + "/" + filename + ".tmp";
            File tempFile = new File(strTempFile);
            PrintWriter pw = new PrintWriter(new FileWriter(strTempFile));
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isNotBlank(line) && line.contains("KFMapSummary]")) {
                    String[] array = line.split(" ");
                    String mapName = array[0].replace("[", "");

                    Optional<AbstractMap> map = platformProfileMapService.listPlatformProfileMaps(platform, profile).stream().
                            map(PlatformProfileMap::getMap).
                            filter(m -> m.getCode().equals(mapName)).
                            findFirst();

                    if (map.isPresent() && officialMapService.findByCode(map.get().getCode()).isPresent() || line.contains("[KF-Default KFMapSummary]")) {
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
                    String modifiedLine = replaceLineKfGameIni(line, platform, profile);
                    if (modifiedLine != null) {
                        pw.println(modifiedLine);
                    }
                }
            }

            List<AbstractMap> customMaps = platformProfileMapService.listPlatformProfileMaps(platform, profile).stream().
                    map(PlatformProfileMap::getMap).
                    filter(m -> {
                        try {
                            return customMapService.findByCode(m.getCode()).isPresent();
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            throw new RuntimeException(e.getMessage(), e);
                        }
                    }).
                    collect(Collectors.toList());

            if (customMaps != null && !customMaps.isEmpty()) {
                for (AbstractMap customMap: customMaps) {
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

    protected void replaceInFileKfWebIni(AbstractPlatform platform, Profile profile, Charset charset) throws Exception {
        String kfWebIniFile = platform.getInstallationFolder() + "/KFGame/Config/" + profile.getCode() + "/KFWeb.ini";
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

    protected String replaceLineKfGameIni(String line, AbstractPlatform platform, Profile profile) {
        String modifiedLine = line;

        if (line.contains("MapName=") || line.contains("MapAssociation=") || line.contains("ScreenshotPathName=")) {
            modifiedLine = null;
        }
        if (line.contains("GameMapCycles=(Maps=(")) {

            try {
                List<PlatformProfileMap> platformProfileMapListForProfile = platformProfileMapService.listPlatformProfileMaps(platform, profile);
                if (platformProfileMapListForProfile != null && !platformProfileMapListForProfile.isEmpty()) {

                    List<AbstractMap> officialMapsInMapCycles = platformProfileMapListForProfile.stream().
                            filter(ppm -> ppm.isInMapsCycle()).
                            map(PlatformProfileMap::getMap).
                            filter(m -> {
                                try {
                                    return officialMapService.findMapByCode(m.getCode()).isPresent();
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                    throw new RuntimeException(e.getMessage(), e);
                                }
                            }).
                            sorted((o1, o2) -> o1.getCode().compareTo(o2.getCode())).
                            collect(Collectors.toList());

                    List<AbstractMap> downloadedCustomMapsInMapCycles = platformProfileMapListForProfile.stream().
                            filter(ppm -> ppm.isDownloaded()).
                            filter(ppm -> ppm.isInMapsCycle()).
                            map(PlatformProfileMap::getMap).
                            filter(m -> {
                                try {
                                    return customMapService.findByCode(m.getCode()).isPresent();
                                } catch (Exception e) {
                                    logger.error(e.getMessage(), e);
                                    throw new RuntimeException(e.getMessage(), e);
                                }
                            }).
                            filter(m -> ((CustomMapMod)Hibernate.unproxy(m)).getMap() != null ? ((CustomMapMod)Hibernate.unproxy(m)).getMap(): false).
                            sorted((o1, o2) -> o1.getCode().compareTo(o2.getCode())).
                            collect(Collectors.toList());

                    modifiedLine = generateMapCycleLine(officialMapsInMapCycles, downloadedCustomMapsInMapCycles);
                }
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
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
        if (line.contains("bDisableMapVote=")) {
            modifiedLine = "bDisableMapVote=" + (profile.getMapVoting() != null ? !profile.getMapVoting(): "True");
        }
        if (line.contains("MapVoteDuration=") && profile.getMapVotingTime() != null) {
            modifiedLine = "MapVoteDuration=" + profile.getMapVotingTime();
        }
        if (line.contains("bDisableKickVote=")) {
            modifiedLine = "bDisableKickVote=" + (profile.getKickVoting() != null ? !profile.getKickVoting(): "True");
        }
        if (line.contains("KickVotePercentage=") && profile.getKickPercentage() != null) {
            modifiedLine = "KickVotePercentage=" + profile.getKickPercentage();
        }
        if (line.contains("bDisablePublicTextChat=")) {
            modifiedLine = "bDisablePublicTextChat=" + (profile.getPublicTextChat() != null ? !profile.getPublicTextChat(): "True");
        }
        if (line.contains("bPartitionSpectators=")) {
            modifiedLine = "bPartitionSpectators=" + (profile.getSpectatorsOnlyChatToOtherSpectators() != null ? profile.getSpectatorsOnlyChatToOtherSpectators(): "False");
        }
        if (line.contains("bDisableVOIP=")) {
            modifiedLine = "bDisableVOIP=" + (profile.getVoip() != null ? !profile.getVoip(): "True");
        }
        if (line.contains("bDisableTeamCollision=")) {
            modifiedLine = "bDisableTeamCollision=" + (profile.getTeamCollision() != null ? !profile.getTeamCollision(): "True");
        }
        if (line.contains("bAdminCanPause=")) {
            modifiedLine = "bAdminCanPause=" + (profile.getAdminCanPause() != null ? profile.getAdminCanPause(): "False");
        }
        if (line.contains("bSilentAdminLogin=")) {
            modifiedLine = "bSilentAdminLogin=" + (profile.getAnnounceAdminLogin() != null ? !profile.getAnnounceAdminLogin(): "True");
        }
        if (line.contains("TimeBetweenFailedVotes=") && profile.getTimeBetweenKicks() != null) {
            modifiedLine = "TimeBetweenFailedVotes=" + profile.getTimeBetweenKicks();
        }
        if (line.contains("MaxIdleTime=") && profile.getMaxIdleTime() != null) {
            modifiedLine = "MaxIdleTime=" + profile.getMaxIdleTime();
        }
        if (line.contains("bEnableDeadToVOIP=")) {
            modifiedLine = "bEnableDeadToVOIP=" + (profile.getDeadPlayersCanTalk() != null ? profile.getDeadPlayersCanTalk(): "False");
        }
        if (line.contains("ReadyUpDelay=") && profile.getReadyUpDelay() != null) {
            modifiedLine = "ReadyUpDelay=" + profile.getReadyUpDelay();
        }
        if (line.contains("GameStartDelay=") && profile.getGameStartDelay() != null) {
            modifiedLine = "GameStartDelay=" + profile.getGameStartDelay();
        }
        if (line.contains("MaxSpectators=") && profile.getMaxSpectators() != null) {
            modifiedLine = "MaxSpectators=" + profile.getMaxSpectators();
        }
        if (line.contains("bEnableMapObjectives=")) {
            modifiedLine = "bEnableMapObjectives=" + (profile.getMapObjetives() != null ? profile.getMapObjetives(): "False");
        }
        if (line.contains("bDisablePickups=")) {
            modifiedLine = "bDisablePickups=" + (profile.getPickupItems() != null ? !profile.getPickupItems(): "True");
        }
        if (line.contains("FriendlyFireScale=") && profile.getFriendlyFirePercentage() != null) {
            modifiedLine = "FriendlyFireScale=" + profile.getFriendlyFirePercentage();
        }
        return modifiedLine;
    }


    private String generateMapCycleLine(List<AbstractMap> officialMaps, List<AbstractMap> downloadedCustomMaps) {
        StringBuffer sb = new StringBuffer("GameMapCycles=(Maps=(");

        if (!officialMaps.isEmpty()) {
            sb.append("\"----- OFFICIAL MAPS -----\",");
            for (AbstractMap map: officialMaps) {
                sb.append("\"").append(map.getCode()).append("\"");
                if (officialMaps.indexOf(map) < (officialMaps.size() - 1)) {
                    sb.append(",");
                }
            }
        }

        if (!downloadedCustomMaps.isEmpty()) {
            if (!officialMaps.isEmpty()) {
                sb.append(",");
            }
            sb.append("\"----- CUSTOM MAPS -----\",");
            for (AbstractMap map: downloadedCustomMaps) {
                sb.append("\"").append(map.getCode()).append("\"");
                if (downloadedCustomMaps.indexOf(map) < (downloadedCustomMaps.size() - 1)) {
                    sb.append(",");
                }
            }
        }
        sb.append("))");
        return sb.toString();
    }
}
