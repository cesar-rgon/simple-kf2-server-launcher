package pojos.kf2factory;

import com.github.sarxos.winreg.HKey;
import com.github.sarxos.winreg.RegistryException;
import com.github.sarxos.winreg.WindowsRegistry;
import entities.Profile;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import utils.Utils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;


public class Kf2WindowsImpl extends Kf2Common {

    private static final Logger logger = LogManager.getLogger(Kf2WindowsImpl.class);

    public Kf2WindowsImpl() {
        super();
    }

    @Override
    protected boolean prepareSteamCmd(String installationFolder) {
        try {
            File steamcmdExeFile = new File(installationFolder + "\\steamcmd\\steamcmd.exe");
            if (!steamcmdExeFile.exists()) {
                File steamcmdZipFile = new File(installationFolder + "\\steamcmd.zip");
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
                ZipFile zipFile = new ZipFile(installationFolder + "\\steamcmd.zip");
                zipFile.extractAll(installationFolder + "\\steamcmd");

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
    protected void installUpdateKf2Server(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) {
        try {
            StringBuffer command = new StringBuffer("cmd /C start /wait ");
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
            Process installUpdateProcess = Runtime.getRuntime().exec(command.toString(),null, new File(installationFolder + "\\steamcmd\\"));
            installUpdateProcess.waitFor();

            // If it's the first time, run the server to create needed config files
            File kfEngineIni = new File(installationFolder + "\\KFGame\\Config\\PCServer-KFEngine.ini");
            File kfGameIni = new File(installationFolder + "\\KFGame\\Config\\PCServer-KFGame.ini");
            if (!kfEngineIni.exists() || !kfGameIni.exists()) {
                Process runServerFirstTimeProcess = Runtime.getRuntime().exec("cmd /C start /wait " + installationFolder + "\\Binaries\\Win64\\KFServer.exe KF-BioticsLab",null, new File(installationFolder));
                while (runServerFirstTimeProcess.isAlive() && (!kfEngineIni.exists() || !kfGameIni.exists())) {
                    runServerFirstTimeProcess.waitFor(1, TimeUnit.SECONDS);
                }
                if (runServerFirstTimeProcess.isAlive()) {
                    Utils.infoDialog("The config files have been created", "You can close the server's console right now.");
                }
            }
        } catch (Exception e) {
            String message = "Error installing KF2 server";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
    }

    @Override
    protected String runKf2Server(String installationFolder, Profile profile) {
        try {
            StringBuffer command = new StringBuffer();
            command.append("cmd /C start /wait ");
            command.append(installationFolder).append("\\Binaries\\Win64\\KFServer.exe ");
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

            replaceInFileKfEngineIni(installationFolder, profile, "PCServer-KFEngine.ini");
            replaceInFileKfWebIni(installationFolder, profile, StandardCharsets.ISO_8859_1);
            replaceInFileKfGameIni(installationFolder, profile, "PCServer-KFGame.ini");
            replaceInFileKfWebAdminIni(installationFolder, profile);

            Process process = Runtime.getRuntime().exec(command.toString(),null, new File(installationFolder));
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
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Long getIdWorkShopFromPath(Path path, String installationFolder) {
        try {
            String[] array = path.toString().replace(installationFolder, "").replace("\\KFGame\\Cache\\", "").split("\\\\");
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

