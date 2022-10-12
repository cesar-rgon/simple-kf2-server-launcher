package pojos.kf2factory;

import entities.Profile;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.WindowsRegistry;
import pojos.session.Session;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


public class Kf2SteamWindowsImpl extends Kf2Steam {

    private static final Logger logger = LogManager.getLogger(Kf2SteamWindowsImpl.class);

    public Kf2SteamWindowsImpl() {
        super();
    }

    @Override
    public boolean isValidInstallationFolder() {
        return StringUtils.isNotBlank(this.platform.getInstallationFolder()) && !this.platform.getInstallationFolder().contains(" ");
    }

    @Override
    protected boolean prepareSteamCmd() {
        try {
            String tempFolder = System.getProperty("java.io.tmpdir");

            File steamcmdExeFile = new File(tempFolder + "steamcmd\\steamcmd.exe");
            if (!steamcmdExeFile.exists()) {
                File steamcmdZipFile = new File(tempFolder + "steamcmd.zip");
                String urlSteamCmd = propertyService.getPropertyValue("properties/config.properties", "prop.config.urlSteamcmd");
                String downloadConnectionTimeOut = propertyService.getPropertyValue("properties/config.properties", "prop.config.downloadConnectionTimeout");
                String downloadReadTimeOut = propertyService.getPropertyValue("properties/config.properties", "prop.config.downloadReadTimeout");
                // Download SteamCmd
                FileUtils.copyURLToFile(
                        new URL(urlSteamCmd),
                        steamcmdZipFile,
                        Integer.parseInt(downloadConnectionTimeOut),
                        Integer.parseInt(downloadReadTimeOut));

                // Decompress SteamCmd
                ZipFile zipFile = new ZipFile(tempFolder + "steamcmd.zip");
                zipFile.extractAll(tempFolder + "steamcmd");

                // Remove steamcmd.zip file
                steamcmdZipFile.delete();
            }
            return true;
        } catch (Exception e) {
            String message = "Error preparing SteamCmd to be able to install KF2 server";
            logger.error(message, e);
            Utils.errorDialog(message, e);
            return false;
        }
    }

    @Override
    protected void installUpdateKf2Server(boolean validateFiles, boolean isBeta, String betaBrunch) throws Exception {
        String tempFolder = System.getProperty("java.io.tmpdir");

        StringBuffer command = new StringBuffer("cmd /C start /wait ");
        command.append(tempFolder);
        command.append("steamcmd\\steamcmd.exe +force_install_dir ");
        command.append(this.platform.getInstallationFolder());
        command.append(" +login anonymous +app_update 232130 ");
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
        Process installUpdateProcess = Runtime.getRuntime().exec(command.toString(),null, new File(tempFolder + "\\steamcmd\\"));
        installUpdateProcess.waitFor();

        // If it's the first time, run the server to create needed config files
        File kfEngineIni = new File(this.platform.getInstallationFolder() + "\\KFGame\\Config\\PCServer-KFEngine.ini");
        File kfGameIni = new File(this.platform.getInstallationFolder() + "\\KFGame\\Config\\PCServer-KFGame.ini");
        if (!kfEngineIni.exists() || !kfGameIni.exists()) {
            Process runServerFirstTimeProcess = Runtime.getRuntime().exec("cmd /C start /wait " + this.platform.getInstallationFolder() + "\\Binaries\\Win64\\KFServer.exe KF-BioticsLab",null, new File(this.platform.getInstallationFolder()));
            while (runServerFirstTimeProcess.isAlive() && (!kfEngineIni.exists() || !kfGameIni.exists())) {
                runServerFirstTimeProcess.waitFor(1, TimeUnit.SECONDS);
            }
            if (runServerFirstTimeProcess.isAlive()) {
                Utils.infoDialog("The config files have been created", "You can close the server's console right now.");
            }
        }
    }

    @Override
    protected void applyPatchToDownloadMaps() throws Exception {
        String tempFolder = System.getProperty("java.io.tmpdir");

        StringBuffer command = new StringBuffer("cmd /C start /wait ");
        command.append(tempFolder);
        command.append("steamcmd\\steamcmd.exe +force_install_dir ");
        command.append(this.platform.getInstallationFolder());
        command.append("\\Binaries\\Win64 ");
        command.append(" +login anonymous +app_update 1007 validate ");
        command.append(" +exit");

        Process applyPatch = Runtime.getRuntime().exec(command.toString(),null, new File(tempFolder + "\\steamcmd\\"));
        applyPatch.waitFor();
    }

    @Override
    protected String runKf2Server(Profile profile) {
        try {
            StringBuffer command = new StringBuffer();
            command.append("cmd /C start /wait ");
            command.append(this.platform.getInstallationFolder()).append("\\Binaries\\Win64\\KFServer.exe ");
            command.append(profile.getMap().getCode());
            command.append("?Game=").append(profile.getGametype().getCode());
            if (profile.getGametype().isDifficultyEnabled()) {
                command.append("?Difficulty=").append(profile.getDifficulty().getCode());
            }
            command.append("?MaxPlayers=").append(profile.getMaxPlayers().getCode());
            if (profile.getGamePort() != null) {
                command.append("?Port=").append(profile.getGamePort());
            }
            if (profile.getQueryPort() != null) {
                command.append("?QueryPort=").append(profile.getQueryPort());
            }
            if (StringUtils.isNotEmpty(profile.getCustomParameters())) {
                if (profile.getCustomParameters().startsWith("?")) {
                    command.append(profile.getCustomParameters());
                } else {
                    command.append("?").append(profile.getCustomParameters());
                }
            }
            command.append("?ConfigSubDir=").append(profile.getCode());

            replaceInFileKfEngineIni(this.platform.getInstallationFolder(), profile, "PCServer-KFEngine.ini");
            replaceInFileKfWebIni(this.platform.getInstallationFolder(), profile, StandardCharsets.ISO_8859_1);
            replaceInFileKfGameIni(this.platform.getInstallationFolder(), profile, "PCServer-KFGame.ini");
            replaceInFileKfWebAdminIni(this.platform.getInstallationFolder(), profile);

            Process process = Runtime.getRuntime().exec(command.toString(),null, new File(this.platform.getInstallationFolder()));
            Session.getInstance().getProcessList().add(process);
            return command.toString();
        } catch (Exception e) {
            String message = "Error executing Killing Floor 2 server";
            if (!byConsole) {
                logger.error(message, e);
                Utils.errorDialog(message, e);
            } else {
                System.out.println(message + "\n" + e);
            }
            return null;
        }
    }

    @Override
    protected File getExeFile() {
        String steamExePath = WindowsRegistry.readRegistry("HKCU\\Software\\Valve\\Steam", "SteamExe");
        if (StringUtils.isNotEmpty(steamExePath)) {
            File steamExeFile = new File(steamExePath);
            if (steamExeFile.exists() && steamExeFile.canExecute()) {
                return steamExeFile;
            }
        }
        return null;
    }

    @Override
    public Long getIdWorkShopFromPath(Path path) {
        try {
            String[] array = path.toString().replace(this.platform.getInstallationFolder(), "").replace("\\KFGame\\Cache\\", "").split("\\\\");
            return Long.parseLong(array[0]);
        } catch (Exception e) {
            logger.error("Error getting idWorkShop from path: " + path.toString(), e);
            return null;
        }
    }

    @Override
    protected void executeFileBeforeRunServer(File fileToBeExecuted) throws Exception {
        String executeFileViaConsoleStr = propertyService.getPropertyValue("properties/config.properties", "prop.config.executeFileViaConsole");
        boolean executeFileViaConsole = StringUtils.isNotBlank(executeFileViaConsoleStr) ? Boolean.parseBoolean(executeFileViaConsoleStr): false;
        StringBuffer command = new StringBuffer();
        if (executeFileViaConsole) {
            command.append("cmd /C start /wait ");
        }
        command.append(fileToBeExecuted.getAbsolutePath());
        Runtime.getRuntime().exec(command.toString(),null, fileToBeExecuted.getParentFile());
    }
}

