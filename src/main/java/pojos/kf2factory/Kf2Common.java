package pojos.kf2factory;

import constants.Constants;
import dtos.ProfileDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import stories.installupdateserver.InstallUpdateServerFacade;
import stories.installupdateserver.InstallUpdateServerFacadeImpl;
import utils.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Stream;

public abstract class Kf2Common {

    protected final InstallUpdateServerFacade installUpdateServerFacade;

    protected Kf2Common() {
        this.installUpdateServerFacade = new InstallUpdateServerFacadeImpl();
    }

    public void installOrUpdateServer(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) {
        if (prepareSteamCmd(installationFolder)) {
            installUpdateKf2Server(installationFolder, validateFiles, isBeta, betaBrunch);
        } else {
            Utils.errorDialog("Error preparing SteamCmd to be able to install KF2 server", "The installation process is aborted.", null);
        }
    }

    protected abstract boolean prepareSteamCmd(String installationFolder);
    protected abstract void installUpdateKf2Server(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch);

    public String runServer(ProfileDto profileDto) {
        try {
            String errorMessage = validateParameters(profileDto);
            if (StringUtils.isEmpty(errorMessage)) {
                String installationFolder = installUpdateServerFacade.findPropertyValue(Constants.KEY_INSTALLATION_FOLDER);
                createConfigFolder(installationFolder, profileDto);
                return runKf2Server(installationFolder, profileDto);
            } else {
                Utils.errorDialog("Error validating parameters. The server can not be launched!", errorMessage, null);
            }
        } catch (SQLException e) {
            Utils.errorDialog("Error executing Killing Floor 2 server", "See stacktrace for more details", e);
        }
        return null;
    }

    protected String validateParameters(ProfileDto profileDto) {
        StringBuffer errorMessage = new StringBuffer();

        if (profileDto == null || StringUtils.isEmpty(profileDto.getName())) {
            return errorMessage.append("The profile name can not be empty.").toString();
        }
        if (profileDto.getLanguage() == null) {
            errorMessage.append("The language can not be empty.\n");
        }
        if (profileDto.getGametype() == null) {
            errorMessage.append("The game type can not be empty.\n");
        }
        if (profileDto.getMap() == null) {
            errorMessage.append("The map can not be empty.\n");
        }
        if (profileDto.getDifficulty() == null) {
            errorMessage.append("The difficulty can not be empty.\n");
        }
        if (profileDto.getLength() == null) {
            errorMessage.append("The length can not be empty.\n");
        }
        if (profileDto.getMaxPlayers() == null) {
            errorMessage.append("The max.players can not be empty.\n");
        }
        if (StringUtils.isEmpty(profileDto.getServerName())) {
            errorMessage.append("The server name can not be empty.\n");
        }
        return errorMessage.toString();
    }

    protected void createConfigFolder(String installationFolder, ProfileDto profileDto) {
        try {
            File configFolder = new File(installationFolder + "/KFGame/Config");
            File profileFolder = new File(configFolder.getAbsolutePath() + "/" + profileDto.getName());
            if (!profileFolder.isDirectory() || !profileFolder.exists()) {
                if (profileFolder.mkdir()) {
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

    protected abstract String runKf2Server(String installationFolder, ProfileDto profile);

    protected void replaceInFileKfWebIni(String installationFolder, ProfileDto profile) throws Exception {
        String kfWebIniFile = installationFolder + "/KFGame/Config/" + profile.getName() + "/KFWeb.ini";
        StringBuilder contentBuilder = new StringBuilder();
        Path filePath = Paths.get(kfWebIniFile);
        Stream<String> stream = Files.lines( filePath, StandardCharsets.UTF_8);
        stream.forEach(line -> contentBuilder.append(replaceLineKfWebIni(line, profile)).append("\n"));
        stream.close();
        PrintWriter outputFile = new PrintWriter(kfWebIniFile);
        outputFile.println(contentBuilder.toString());
        outputFile.close();
    }

    protected String replaceLineKfWebIni(String line, ProfileDto profile){
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

    protected void replaceInFileKfGameIni(String filename, String installationFolder, ProfileDto profile) throws Exception {
        String pcServerKFGameIni = installationFolder + "/KFGame/Config/" + profile.getName() + "/" + filename;
        StringBuilder contentBuilder = new StringBuilder();
        Path filePath = Paths.get(pcServerKFGameIni);
        Stream<String> stream = Files.lines( filePath, StandardCharsets.UTF_8);
        stream.forEach(line -> contentBuilder.append(replaceLinePcServerKFGameIni(line, profile)).append("\n"));
        stream.close();
        PrintWriter outputFile = new PrintWriter(pcServerKFGameIni);
        outputFile.println(contentBuilder.toString());
        outputFile.close();
    }

    protected String replaceLinePcServerKFGameIni(String line, ProfileDto profile) {
        String modifiedLine = line;

        if (line.contains("GameDifficulty=")) {
            modifiedLine = "GameDifficulty=" + profile.getDifficulty().getKey();
        }
        if (line.contains("GameLength=")) {
            modifiedLine = "GameLength=" + profile.getLength().getKey();
        }
        if (line.contains("ServerName=")) {
            modifiedLine = "ServerName=" + profile.getServerName();
        }
        try {
            if (line.contains("GamePassword=")) {
                modifiedLine = "GamePassword=" + Utils.decryptAES(profile.getServerPassword());
            }
            if (line.contains("AdminPassword=")) {
                modifiedLine = "AdminPassword=" + Utils.decryptAES(profile.getWebPassword());
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
        // TODO: set GameMapCycle

        return modifiedLine;
    }

    public void joinServer(ProfileDto profile) {
        File steamExeFile = getSteamExeFile();
        if (steamExeFile != null) {
            joinToKf2Server(steamExeFile, profile);
        } else {
            Utils.errorDialog("Error validating Steam installation directory", "The process is aborted.", null);
        }
    }

    protected abstract File getSteamExeFile();

    protected void joinToKf2Server(File steamExeFile, ProfileDto profile) {
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
}
