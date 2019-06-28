package pojos.kf2factory;

import com.github.sarxos.winreg.HKey;
import com.github.sarxos.winreg.RegistryException;
import com.github.sarxos.winreg.WindowsRegistry;
import constants.Constants;
import dtos.ProfileDto;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Kf2WindowsImpl extends Kf2Common {

    public Kf2WindowsImpl() {
        super();
    }

    @Override
    protected boolean prepareSteamCmd(String installationFolder) {
        try {
            File steamcmdExeFile = new File(installationFolder + "\\steamcmd\\steamcmd.exe");
            if (!steamcmdExeFile.exists()) {
                File steamcmdZipFile = new File(installationFolder + "\\steamcmd.zip");
                // Download SteamCmd
                FileUtils.copyURLToFile(
                        new URL(installUpdateServerFacade.findPropertyValue(Constants.KEY_URL_STEAMCMD)),
                        steamcmdZipFile,
                        Integer.parseInt(installUpdateServerFacade.findPropertyValue(Constants.KEY_DOWNLOAD_CONNECTION_TIMEOUT)),
                        Integer.parseInt(installUpdateServerFacade.findPropertyValue(Constants.KEY_DOWNLOAD_READ_TIMEOUT)));

                // Decompress SteamCmd
                ZipFile zipFile = new ZipFile(installationFolder + "\\steamcmd.zip");
                zipFile.extractAll(installationFolder + "\\steamcmd");

                // Remove steamcmd.zip file
                steamcmdZipFile.delete();
            }
            return true;
        } catch (Exception e) {
            Utils.errorDialog("Error preparing SteamCmd to be able to install KF2 server", "See stacktrace for more details", e);
        }
        return false;
    }

    @Override
    protected void installUpdateKf2Server(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) {
        try {
            StringBuffer command = new StringBuffer("cmd /C start ");
            command.append(installationFolder);
            command.append("\\steamcmd\\steamcmd.exe +login anonymous +force_install_dir ");
            command.append(installationFolder);
            command.append(" +app_update 232130 ");
            if (validateFiles) {
                command.append(" validate ");
            }
            if (isBeta) {
                command.append(" -beta \"");
                command.append(betaBrunch);
                command.append("\"");
            }
            command.append(" +exit");

            // Execute steamcmd and install / update the kf2 server
            Runtime.getRuntime().exec(command.toString(),null, new File(installationFolder + "\\steamcmd\\"));
        } catch (IOException e) {
            Utils.errorDialog("Error preparing SteamCmd to be able to install KF2 server", "See stacktrace for more details", e);
        }
    }

    @Override
    protected String runKf2Server(String installationFolder, ProfileDto profile) {
        try {
            StringBuffer command = new StringBuffer();
            command.append(installationFolder).append("\\Binaries\\Win64\\KFServer.exe ");
            command.append(profile.getMap().getKey());
            command.append("?Game=").append(profile.getGametype().getKey());
            command.append("?Difficulty=").append(profile.getDifficulty().getKey());
            command.append("?MaxPlayers=").append(profile.getMaxPlayers().getKey());
            if (profile.getGamePort() != null) {
                command.append("?Port=").append(profile.getGamePort());
            }
            if (profile.getQueryPort() != null) {
                command.append("?QueryPort=").append(profile.getQueryPort());
            }
            if (StringUtils.isNotEmpty(profile.getCustomParameters())) {
                command.append("?").append(profile.getCustomParameters());
            }
            command.append("?ConfigSubDir=").append(profile.getName());

            replaceInFileKfWebIni(installationFolder, profile);
            replaceInFilePcServerKfGameIni(installationFolder, profile);

            Runtime.getRuntime().exec("cmd /C start " + command.toString(),null, new File(installationFolder));
            return command.toString();
        } catch (Exception e) {
            Utils.errorDialog("Error executing Killing Floor 2 server", "See stacktrace for more details", e);
            return null;
        }
    }

    private void replaceInFileKfWebIni(String installationFolder, ProfileDto profile) throws Exception {
        String kfWebIniFile = installationFolder + "\\KFGame\\Config\\" + profile.getName() + "\\KFWeb.ini";
        StringBuilder contentBuilder = new StringBuilder();
        Path filePath = Paths.get(kfWebIniFile);
        Stream<String> stream = Files.lines( filePath, StandardCharsets.UTF_8);
        stream.forEach(line -> contentBuilder.append(replaceLineKfWebIni(line, profile)).append("\n"));
        stream.close();
        PrintWriter outputFile = new PrintWriter(kfWebIniFile);
        outputFile.println(contentBuilder.toString());
        outputFile.close();
    }

    private String replaceLineKfWebIni(String line, ProfileDto profile){
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

    private void replaceInFilePcServerKfGameIni(String installationFolder, ProfileDto profile) throws Exception {
        String pcServerKFGameIni = installationFolder + "\\KFGame\\Config\\" + profile.getName() + "\\PCServer-KFGame.ini";
        StringBuilder contentBuilder = new StringBuilder();
        Path filePath = Paths.get(pcServerKFGameIni);
        Stream<String> stream = Files.lines( filePath, StandardCharsets.UTF_8);
        stream.forEach(line -> contentBuilder.append(replaceLinePcServerKFGameIni(line, profile)).append("\n"));
        stream.close();
        PrintWriter outputFile = new PrintWriter(pcServerKFGameIni);
        outputFile.println(contentBuilder.toString());
        outputFile.close();
    }

    private String replaceLinePcServerKFGameIni(String line, ProfileDto profile) {
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
        // PENDIENTE setear GameMapCycle

        return modifiedLine;
    }

    @Override
    protected File getSteamExeFile() {
        try {
            String steamExePath = WindowsRegistry.getInstance().readString(HKey.HKCU, "Software\\Valve\\Steam", "SteamExe");
            if (StringUtils.isNotEmpty(steamExePath)) {
                File steamExeFile = new File(steamExePath);
                if (steamExeFile.exists() && steamExeFile.canExecute()) {
                    return steamExeFile;
                }
            }
        } catch (RegistryException e) {
            Utils.errorDialog(e.getMessage(), "See stacktrace for more details", e);
        }
        return null;
    }

    @Override
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

