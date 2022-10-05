package pojos.kf2factory;

import entities.Profile;
import javafx.concurrent.Task;
import net.lingala.zip4j.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import utils.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Kf2EpicWindowsImpl extends Kf2Epic {

    private static final Logger logger = LogManager.getLogger(Kf2EpicWindowsImpl.class);

    public Kf2EpicWindowsImpl() {
        super();
    }

    @Override
    public boolean isValidInstallationFolder() {
        return !StringUtils.isBlank(this.platform.getInstallationFolder()) && (Files.exists(Paths.get(this.platform.getInstallationFolder() + "/Binaries/Win64/KFServer.exe")));
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
                if(name.endsWith(".dll")) {
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
}
