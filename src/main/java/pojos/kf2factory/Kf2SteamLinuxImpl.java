package pojos.kf2factory;

import entities.Profile;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import utils.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Kf2SteamLinuxImpl extends Kf2Steam {

    private static final Logger logger = LogManager.getLogger(Kf2SteamLinuxImpl.class);

    public Kf2SteamLinuxImpl(EntityManager em) {
        super(em);
    }

    @Override
    public boolean isValidInstallationFolder() {
        return StringUtils.isNotBlank(this.platform.getInstallationFolder()) && !this.platform.getInstallationFolder().contains(" ") && (Files.exists(Paths.get(this.platform.getInstallationFolder() + "/Binaries/Win64/KFGameSteamServer.bin.x86_64")));
    }


    @Override
    protected boolean prepareSteamCmd() {
        try {
            File xtermFile = new File("/usr/bin/xterm");
            if (!xtermFile.exists()) {
                Runtime.getRuntime().exec(new String[]{"x-terminal-emulator","-e","echo Installing package xterm && sudo apt -y install xterm"});
                Utils.warningDialog("Install package xterm", "When installation is completed, press this button");
            }
            File steamcmdFile = new File("/usr/games/steamcmd");
            if (!steamcmdFile.exists()) {
                Process process = Runtime.getRuntime().exec(new String[]{"xterm",
                        "-T", "Installing SteamCmd",
                        "-fa", "DejaVu Sans Mono",
                        "-fs", "11",
                        "-geometry", "120x25+0-0",
                        "-xrm", "XTerm.vt100.allowTitleOps: false",
                        "-e","sudo apt -y install steamcmd"});
                process.waitFor();
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
        File cacheFolder = new File(this.platform.getInstallationFolder() + "/KFGame/Cache");
        if (!cacheFolder.exists()) {
            cacheFolder.mkdir();
        }
        StringBuffer command = new StringBuffer("/usr/games/steamcmd +force_install_dir ");
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
        Process process = Runtime.getRuntime().exec(new String[]{"xterm",
                "-T", "Installing/Updating the server",
                "-fa", "DejaVu Sans Mono",
                "-fs", "11",
                "-geometry", "120x25+0-0",
                "-xrm", "XTerm.vt100.allowTitleOps: false",
                "-e", command.toString()});
        process.waitFor();

        // If it's the first time, run the server to create needed config files
        File kfEngineIni = new File(this.platform.getInstallationFolder() + "/KFGame/Config/LinuxServer-KFEngine.ini");
        File kfGameIni = new File(this.platform.getInstallationFolder() + "/KFGame/Config/LinuxServer-KFGame.ini");
        if (!kfEngineIni.exists() || !kfGameIni.exists()) {
            Process processTwo = Runtime.getRuntime().exec(new String[]{"xterm",
                            "-T", "Wait until config files are generated",
                            "-fa", "DejaVu Sans Mono",
                            "-fs", "11",
                            "-geometry", "120x25+0-0",
                            "-xrm", "XTerm.vt100.allowTitleOps: false",
                            "-e", this.platform.getInstallationFolder() + "/Binaries/Win64/KFGameSteamServer.bin.x86_64 KF-BioticsLab"},
                    null, new File(this.platform.getInstallationFolder()));
            while (processTwo.isAlive() && (!kfEngineIni.exists() || !kfGameIni.exists())) {
                processTwo.waitFor(5, TimeUnit.SECONDS);
            }
            if (processTwo.isAlive()) {
                processTwo.destroy();
            }
        }
    }

    @Override
    protected void applyPatchToDownloadMaps() throws Exception {
        String homeFolder = System.getProperty("user.home");
        StringBuffer command = new StringBuffer("/usr/games/steamcmd +force_install_dir ");
        command.append(homeFolder);
        command.append("/kf2_patch");
        command.append(" +login anonymous +app_update 1007 validate ");
        command.append(" +exit");

        // Execute steamcmd and install the patch
        Process applyPatch = Runtime.getRuntime().exec(new String[]{"xterm",
                "-T", "Installing/Updating the server",
                "-fa", "DejaVu Sans Mono",
                "-fs", "11",
                "-geometry", "120x25+0-0",
                "-xrm", "XTerm.vt100.allowTitleOps: false",
                "-e", command.toString()});
        applyPatch.waitFor();

        File srcFolder = new File(homeFolder + "/kf2_patch");
        File targetFolder = new File(this.platform.getInstallationFolder());
        File[] sourceSoFiles = srcFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".so")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        for (File sourceSoFile: sourceSoFiles) {
            FileUtils.copyFileToDirectory(sourceSoFile, targetFolder);
        }

        File srcLinux64Folder = new File(homeFolder + "/kf2_patch/linux64");
        File targetLinux64Folder = new File(this.platform.getInstallationFolder() + "/linux64");
        File[] sourceLinux64SoFiles = srcLinux64Folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".so")) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        for (File sourceSoFile: sourceLinux64SoFiles) {
            FileUtils.copyFileToDirectory(sourceSoFile, targetLinux64Folder);
        }

        File targetLib64Folder = new File(this.platform.getInstallationFolder() + "/Binaries/Win64/lib64");
        for (File sourceSoFile: sourceSoFiles) {
            FileUtils.copyFileToDirectory(sourceSoFile, targetLib64Folder);
        }

        FileUtils.deleteDirectory(srcFolder);
    }

    @Override
    protected String runKf2Server(Profile profile) {
        try {
            StringBuffer command = new StringBuffer();
            command.append(this.platform.getInstallationFolder()).append("/Binaries/Win64/KFGameSteamServer.bin.x86_64 ");
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

            replaceInFileKfEngineIni(this.platform.getInstallationFolder(), profile, "LinuxServer-KFEngine.ini");
            replaceInFileKfWebIni(this.platform.getInstallationFolder(), profile, StandardCharsets.UTF_8);
            replaceInFileKfGameIni(this.platform.getInstallationFolder(), profile, "LinuxServer-KFGame.ini");
            replaceInFileKfWebAdminIni(this.platform.getInstallationFolder(), profile);

            Process proccess = null;
            if (!byConsole) {
                proccess = Runtime.getRuntime().exec(new String[]{"xterm",
                                "-T", "Running the server",
                                "-fa", "DejaVu Sans Mono",
                                "-fs", "11",
                                "-geometry", "120x25+0-0",
                                "-xrm", "XTerm.vt100.allowTitleOps: false",
                                "-e", command.toString()},
                        null, new File(this.platform.getInstallationFolder()));
            } else {
                proccess = Runtime.getRuntime().exec(command.toString(), null, new File(this.platform.getInstallationFolder()));
            }
            Session.getInstance().getProcessList().add(proccess);

            return command.toString();
        } catch (Exception e) {
            String message = "Error executing Killing Floor 2 server";
            logger.error(message, e);
            Utils.errorDialog(message, e);
            return null;
        }
    }

    @Override
    protected File getExeFile() {
        File steamExeFile = new File("/usr/games/steam");
        if (steamExeFile.exists() && steamExeFile.canExecute()) {
            return steamExeFile;
        }
        return null;
    }

    @Override
    public Long getIdWorkShopFromPath(Path path) {
        try {
            String[] array = path.toString().replace(this.platform.getInstallationFolder(), "").replace("/KFGame/Cache/", "").split("/");
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
        if (executeFileViaConsole) {
            Runtime.getRuntime().exec(new String[]{"xterm",
                            "-T", "Running executable file",
                            "-fa", "DejaVu Sans Mono",
                            "-fs", "11",
                            "-geometry", "120x25+0-0",
                            "-xrm", "XTerm.vt100.allowTitleOps: false",
                            "-e", fileToBeExecuted.getAbsolutePath()},
                    null, fileToBeExecuted.getParentFile());
        } else {
            Runtime.getRuntime().exec(fileToBeExecuted.getAbsolutePath(), null, fileToBeExecuted.getParentFile());
        }
    }
}
