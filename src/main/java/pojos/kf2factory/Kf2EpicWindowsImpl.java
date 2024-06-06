package pojos.kf2factory;

import com.github.tuupertunut.powershelllibjava.PowerShell;
import entities.CustomMapMod;
import entities.Profile;
import jakarta.persistence.EntityManager;
import javafx.concurrent.Task;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import utils.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Kf2EpicWindowsImpl extends Kf2Epic {

    private static final Logger logger = LogManager.getLogger(Kf2EpicWindowsImpl.class);

    public Kf2EpicWindowsImpl(EntityManager em) {
        super(em);
    }

    @Override
    public boolean isValidInstallationFolder() {
        return StringUtils.isNotBlank(this.platform.getInstallationFolder()) && (Files.exists(Paths.get(this.platform.getInstallationFolder() + "/Binaries/Win64/KFServer.exe")));
    }

    @Override
    public boolean isValidInstallationFolder(boolean checkIfInstalled) {
        if (checkIfInstalled) {
            return isValidInstallationFolder();
        }
        return false;
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
    protected String runKf2Server(Profile profile) {
        try {
            StringBuffer command = new StringBuffer();
            command.append("cmd /C \"");
            command.append(this.platform.getInstallationFolder()).append("\\Binaries\\Win64\\KFServer.exe\" ");
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
    public String joinServer(Profile profile) {
        return null;
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
    protected void installUpdateKf2Server() throws Exception {

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

        String tempFolderString = System.getProperty("java.io.tmpdir") + "Kf2_Webadmin_temp_folder";
        File tempFolder = new File(tempFolderString);
        Task<Void> deleteFolderTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (tempFolder.exists()) {
                    FileUtils.deleteDirectory(tempFolder);
                }
                return null;
            }
        };
        deleteFolderTask.setOnSucceeded(deleteFolderWse -> {
            Task<Void> gitCloneTask = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    StringBuffer command = new StringBuffer();
                    command.append("cmd /C start /wait git clone https://github.com/cesar-rgon/kf2-webadmin.git ");
                    command.append(tempFolder.getAbsolutePath());

                    Process installUpdateProcess = Runtime.getRuntime().exec(command.toString(),null, tempFolder.getParentFile());
                    installUpdateProcess.waitFor();
                    return null;
                }
            };
            gitCloneTask.setOnSucceeded(gitCloneTaskWse -> {
                try {
                    File webAdminFolder = new File(tempFolder.getAbsolutePath() + "\\Web");
                    if (!tempFolder.exists() || !webAdminFolder.exists()) {
                        String message = "The installation of the Epic Games server could not be done!";
                        logger.error(message);
                        return;
                    }

                    Task<Void> moveFolderTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            File destFolder = new File(platform.getInstallationFolder() + "\\KFGame\\Web");
                            FileUtils.moveDirectory(webAdminFolder, destFolder );
                            return null;
                        }
                    };
                    moveFolderTask.setOnSucceeded(moveFolderTaskWse -> {
                        try {
                            FileUtils.deleteDirectory(tempFolder);
                        } catch (Exception e) {
                            String message = "Error installing KF2 server";
                            logger.error(message, e);
                            Utils.errorDialog(message, e);
                        }
                    });
                    moveFolderTask.setOnFailed(moveFolderTaskWse -> {
                        String message = "The installation of the Epic Games server could not be done!";
                        logger.error(message);
                    });
                    Thread moveFolderThread = new Thread(moveFolderTask);
                    moveFolderThread.start();

                } catch (Exception e) {
                    String message = "Error installing KF2 server";
                    logger.error(message, e);
                    Utils.errorDialog(message, e);
                }
            });
            gitCloneTask.setOnFailed(gitCloneTaskWse -> {
                String message = "The installation of the Epic Games server could not be done!";
                logger.error(message);
            });
            Thread gitCloneThread = new Thread(gitCloneTask);
            gitCloneThread.start();
        });
        deleteFolderTask.setOnFailed(deleteFolderWse -> {
            String message = "The installation of the Epic Games server could not be done!";
            logger.error(message);
        });
        Thread deleteFolderThread = new Thread(deleteFolderTask);
        deleteFolderThread.start();
    }

    @Override
    protected void applyPatchToDownloadMaps() throws Exception {
        String tempFolder = System.getProperty("java.io.tmpdir");

        StringBuffer command = new StringBuffer("cmd /C start /wait ");
        command.append(tempFolder);
        command.append("steamcmd\\steamcmd.exe +force_install_dir ");
        command.append(tempFolder);
        command.append("KillingFloor2_patch\\Binaries\\Win64");
        command.append(" +login anonymous +app_update 1007 validate ");
        command.append(" +exit");

        Process applyPatch = Runtime.getRuntime().exec(command.toString(),null, new File(tempFolder + "\\steamcmd\\"));
        applyPatch.waitFor();

        File srcFolder = new File(tempFolder + "KillingFloor2_patch\\Binaries\\Win64");
        File targetFolder = new File(this.platform.getInstallationFolder() + "\\Binaries\\Win64");
        File[] sourceDllFiles = srcFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".dll")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        for (File sourceDllFile: sourceDllFiles) {
            FileUtils.copyFileToDirectory(sourceDllFile, targetFolder);
        }
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
