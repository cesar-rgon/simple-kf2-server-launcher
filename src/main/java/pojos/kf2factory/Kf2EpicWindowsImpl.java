package pojos.kf2factory;

import entities.Profile;
import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Kf2EpicWindowsImpl extends Kf2Epic {

    private static final Logger logger = LogManager.getLogger(Kf2EpicWindowsImpl.class);

    @Override
    public boolean isValid(String installationFolder) {
        return !StringUtils.isBlank(installationFolder) && (Files.exists(Paths.get(installationFolder + "/Binaries/Win64/KFServer.exe")));
    }

    @Override
    protected String runKf2Server(String installationFolder, Profile profile) {
        try {
            StringBuffer command = new StringBuffer();
            command.append("cmd /C \"");
            command.append(installationFolder).append("\\Binaries\\Win64\\KFServer.exe\" ");
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
    protected void installUpdateKf2Server(String installationFolder) throws Exception {
        File tempFolder = new File("C:\\Kf2_Webadmin_temp_folder");
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
                            File destFolder = new File(installationFolder + "\\KFGame\\Web");
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
}
