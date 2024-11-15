package pojos.kf2factory;

import com.github.tuupertunut.powershelllibjava.PowerShell;
import entities.CustomMapMod;
import entities.PlatformProfileMap;
import entities.Profile;
import jakarta.persistence.EntityManager;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.WindowsRegistry;
import pojos.session.Session;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import utils.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class Kf2SteamWindowsImpl extends Kf2Steam {

    private static final Logger logger = LogManager.getLogger(Kf2SteamWindowsImpl.class);

    public Kf2SteamWindowsImpl(EntityManager em) {
        super(em);
    }

    @Override
    public boolean isValidInstallationFolder() {
        return isValidInstallationFolder(true);
    }

    public boolean isValidInstallationFolder(boolean checkIfInstalled) {
        if (checkIfInstalled) {
            return StringUtils.isNotBlank(this.platform.getInstallationFolder()) && !this.platform.getInstallationFolder().contains(" ") && (Files.exists(Paths.get(this.platform.getInstallationFolder() + "/Binaries/Win64/KFServer.exe")));
        } else {
            return StringUtils.isNotBlank(this.platform.getInstallationFolder()) && !this.platform.getInstallationFolder().contains(" ");
        }
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
            PowerShell psSession = PowerShell.open();
            psSession.executeCommands("start \"" + this.platform.getInstallationFolder() + "\\Binaries\\Win64\\KFServer.exe\" KF-BioticsLab");
            Utils.infoDialog("Wait some seconds until the server has been started at all", "This process is necessary to generate all needed config files.");
        }

        // Copy config files to all profiles that need them
        if (kfEngineIni.exists() && kfGameIni.exists()) {
            ProfileService profileService = new ProfileServiceImpl(em);
            List<Profile> allProfileList = profileService.listAllProfiles();
            for (Profile profile: allProfileList) {
                File kfProfileEngineIni = new File(this.platform.getInstallationFolder() + "\\KFGame\\Config\\" + profile.getName() + "\\PCServer-KFEngine.ini");
                File kfProfileGameIni = new File(this.platform.getInstallationFolder() + "\\KFGame\\Config\\" + profile.getName() + "\\PCServer-KFGame.ini");
                if (!kfProfileEngineIni.exists()) {
                    FileUtils.copyFile(kfEngineIni, kfProfileEngineIni);
                }
                if (!kfProfileGameIni.exists()) {
                    FileUtils.copyFile(kfGameIni, kfProfileGameIni);
                }

                File configFolder = new File(this.platform.getInstallationFolder() + "\\KFGame\\Config");
                File profileFolder = new File(configFolder.getAbsolutePath() + "\\" + profile.getName());
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
                    File targetFile = new File(profileFolder + "\\" + sourceFile.getName() + ".ini");
                    if (!targetFile.exists()) {
                        FileUtils.copyFileToDirectory(sourceFile, profileFolder);
                        if ("DefaultWebAdmin.ini".equalsIgnoreCase(sourceFile.getName())) {
                            FileUtils.copyFile(sourceFile, new File(configFolder.getAbsolutePath() + "\\" + profile.getName() + "/KFWebAdmin.ini"));
                        }
                    }
                }
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

            replaceInFileKfEngineIni(this.platform, profile, "PCServer-KFEngine.ini");
            replaceInFileKfWebIni(this.platform, profile, StandardCharsets.ISO_8859_1);
            replaceInFileKfGameIni(this.platform, profile, "PCServer-KFGame.ini");
            replaceInFileKfWebAdminIni(this.platform, profile);

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

    @Override
    public boolean downloadMapFromSteamCmd(CustomMapMod customMap) throws Exception {
        if (prerequisitesAreValid()) {
            String tempFolder = System.getProperty("java.io.tmpdir");
            StringBuffer command = new StringBuffer("cmd /C start /wait ");
            command.append(tempFolder);
            command.append("steamcmd\\steamcmd.exe +force_install_dir ");
            command.append(tempFolder);
            command.append("steamcmd +login anonymous +workshop_download_item 232090 ");
            command.append(customMap.getIdWorkShop());
            command.append(" +exit");
            Process downloadMapProcess = Runtime.getRuntime().exec(command.toString(),null, new File(tempFolder + "\\steamcmd\\"));
            downloadMapProcess.waitFor();
            return true;
        } else {
            return false;
        }
    }

    public String copyMapToCachePlatform(CustomMapMod customMap) throws Exception {
        String tempFolder = System.getProperty("java.io.tmpdir");

        List<File> sourceFileList = Files.walk(Paths.get(tempFolder + "steamcmd\\steamapps\\workshop\\content\\232090\\" + customMap.getIdWorkShop()))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());

        String mapFilename = customMap.getCode();
        for (File sourceFile: sourceFileList) {
            String relativePath = StringUtils.replace(sourceFile.getAbsolutePath(), tempFolder + "steamcmd\\steamapps\\workshop\\content\\232090\\" + customMap.getIdWorkShop() , "");
            String[] relativePathArray = relativePath.split("\\\\");
            StringBuffer relativeFolder = new StringBuffer();
            for (int i=0; i< (relativePathArray.length - 1) ; i++) {
                if (StringUtils.isNotBlank(relativePathArray[i])) {
                    relativeFolder.append(relativePathArray[i]);
                    relativeFolder.append("\\");
                }
            }
            File targetFolder = new File(platform.getInstallationFolder() + "\\KFGame\\Cache\\" + customMap.getIdWorkShop() + "\\0\\" + relativeFolder.toString());
            targetFolder.mkdirs();
            FileUtils.copyFileToDirectory(sourceFile, targetFolder);
            if (sourceFile.getName().toUpperCase().startsWith("KF") && sourceFile.getName().toLowerCase().endsWith(".kfm")) {
                mapFilename = sourceFile.getName();
            }
        }

        return mapFilename;
    }
}

