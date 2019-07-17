package pojos.kf2factory;

import com.github.sarxos.winreg.HKey;
import com.github.sarxos.winreg.RegistryException;
import com.github.sarxos.winreg.WindowsRegistry;
import constants.Constants;
import entities.Map;
import entities.Profile;
import net.lingala.zip4j.core.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;
import utils.Utils;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
                        new URL(databaseService.findPropertyValue(Constants.KEY_URL_STEAMCMD)),
                        steamcmdZipFile,
                        Integer.parseInt(databaseService.findPropertyValue(Constants.KEY_DOWNLOAD_CONNECTION_TIMEOUT)),
                        Integer.parseInt(databaseService.findPropertyValue(Constants.KEY_DOWNLOAD_READ_TIMEOUT)));

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
            Process process = Runtime.getRuntime().exec(command.toString(),null, new File(installationFolder + "\\steamcmd\\"));
            process.waitFor();

            // If it's the first time, run the server to create needed config files
            File kfEngineIni = new File(installationFolder + "\\KFGame\\Config\\PCServer-KFEngine.ini");
            File kfGameIni = new File(installationFolder + "\\KFGame\\Config\\PCServer-KFGame.ini");
            if (!kfEngineIni.exists() || !kfGameIni.exists()) {
                Process processTwo = Runtime.getRuntime().exec("cmd /C " + installationFolder + "\\Binaries\\Win64\\KFServer.exe KF-BioticsLab",
                        null,
                        new File(installationFolder));
                while (processTwo.isAlive() && (!kfEngineIni.exists() || !kfGameIni.exists())) {
                    processTwo.waitFor(5, TimeUnit.SECONDS);
                }
                if (processTwo.isAlive()) {
                    processTwo.destroy();
                }
            }
        } catch (Exception e) {
            Utils.errorDialog("Error installing KF2 server", "See stacktrace for more details", e);
        }
    }

    @Override
    protected String runKf2Server(String installationFolder, Profile profile) {
        try {
            StringBuffer command = new StringBuffer();
            command.append(installationFolder).append("\\Binaries\\Win64\\KFServer.exe ");
            command.append(profile.getMap().getCode());
            command.append("?Game=").append(profile.getGametype().getCode());
            command.append("?Difficulty=").append(profile.getDifficulty().getCode());
            command.append("?MaxPlayers=").append(profile.getMaxPlayers().getCode());
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
            replaceInFileKfGameIni(installationFolder, profile);

            Process proccess = Runtime.getRuntime().exec("cmd /C " + command.toString(),null, new File(installationFolder));
            Session.getInstance().getProcessList().add(proccess);
            return command.toString();
        } catch (Exception e) {
            Utils.errorDialog("Error executing Killing Floor 2 server", "See stacktrace for more details", e);
            return null;
        }
    }

    private void replaceInFileKfWebIni(String installationFolder, Profile profile) throws Exception {
        String kfWebIniFile = installationFolder + "/KFGame/Config/" + profile.getName() + "/KFWeb.ini";
        StringBuilder contentBuilder = new StringBuilder();
        Path filePath = Paths.get(kfWebIniFile);
        Stream<String> stream = Files.lines( filePath, StandardCharsets.ISO_8859_1);
        stream.forEach(line -> contentBuilder.append(replaceLineKfWebIni(line, profile)).append("\n"));
        stream.close();
        PrintWriter outputFile = new PrintWriter(kfWebIniFile);
        outputFile.println(contentBuilder.toString());
        outputFile.close();
    }

    private void replaceInFileKfGameIni(String installationFolder, Profile profile) throws Exception {
        String pcServerKFGameIni = installationFolder + "/KFGame/Config/" + profile.getName() + "/" + "PCServer-KFGame.ini";
        StringBuilder contentBuilder = new StringBuilder();
        Path filePath = Paths.get(pcServerKFGameIni);
        Stream<String> stream = Files.lines( filePath, StandardCharsets.ISO_8859_1);
        stream.forEach(line -> contentBuilder.append(replaceLinePcServerKFGameIni(line, profile)).append("\n"));
        stream.close();
        PrintWriter outputFile = new PrintWriter(pcServerKFGameIni);
        outputFile.println(contentBuilder.toString());
        outputFile.close();
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
    public void addCustomMapToKfEngineIni(Long idWorkShop, String installationFolder) {
        addCustomMapToKfEngineIni(idWorkShop, installationFolder,"PCServer-KFEngine.ini");
    }

    @Override
    public void removeCustomMapsFromKfEngineIni(List<Long> idWorkShopList, String installationFolder) {
        removeCustomMapsFromKfEngineIni(idWorkShopList, installationFolder,"PCServer-KFEngine.ini");
    }

    @Override
    public void addCustomMapsToKfGameIni(List<String> mapNameList, String installationFolder, List<Map> mapList) {
        addCustomMapsToKfGameIni(mapNameList, installationFolder, mapList, "PCServer-KFGame.ini");
    }

    @Override
    public void removeCustomMapsFromKfGameIni(List<String> mapNameList, String installationFolder, List<Map> mapList) {
        removeCustomMapsFromKfGameIni(mapNameList, installationFolder, mapList, "PCServer-KFGame.ini");
    }

    @Override
    public void checkForNewOfficialMaps(String installationFolder) {
        checkForNewOfficialMaps(installationFolder, "PCServer-KFGame.ini");
    }
}

