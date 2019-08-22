package pojos.kf2factory;

import entities.Profile;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import utils.Utils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class Kf2LinuxImpl extends Kf2Common {

    private static final Logger logger = LogManager.getLogger(Kf2LinuxImpl.class);

    @Override
    protected boolean prepareSteamCmd(String installationFolder) {
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
            Utils.errorDialog(message, "See stacktrace for more details", e);
            return false;
        }
    }

    @Override
    protected void installUpdateKf2Server(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) {
        try {
            File cacheFolder = new File(installationFolder + "/KFGame/Cache");
            if (!cacheFolder.exists()) {
                cacheFolder.mkdir();
            }
            StringBuffer command = new StringBuffer("/usr/games/steamcmd +login anonymous +force_install_dir ");
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
            Process process = Runtime.getRuntime().exec(new String[]{"xterm",
                    "-T", "Installing/Updating the server",
                    "-fa", "DejaVu Sans Mono",
                    "-fs", "11",
                    "-geometry", "120x25+0-0",
                    "-xrm", "XTerm.vt100.allowTitleOps: false",
                    "-e", command.toString()});
            process.waitFor();

            // If it's the first time, run the server to create needed config files
            File kfEngineIni = new File(installationFolder + "/KFGame/Config/LinuxServer-KFEngine.ini");
            File kfGameIni = new File(installationFolder + "/KFGame/Config/LinuxServer-KFGame.ini");
            if (!kfEngineIni.exists() || !kfGameIni.exists()) {
                Process processTwo = Runtime.getRuntime().exec(new String[]{"xterm",
                                "-T", "Wait until config files are generated",
                                "-fa", "DejaVu Sans Mono",
                                "-fs", "11",
                                "-geometry", "120x25+0-0",
                                "-xrm", "XTerm.vt100.allowTitleOps: false",
                                "-e", installationFolder + "/Binaries/Win64/KFGameSteamServer.bin.x86_64 KF-BioticsLab"},
                        null, new File(installationFolder));
                while (processTwo.isAlive() && (!kfEngineIni.exists() || !kfGameIni.exists())) {
                    processTwo.waitFor(5, TimeUnit.SECONDS);
                }
                if (processTwo.isAlive()) {
                    processTwo.destroy();
                }
            }
        } catch (Exception e) {
            String message = "Error installing KF2 server";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
        }
    }

    @Override
    protected String runKf2Server(String installationFolder, Profile profile) {
        try {
            StringBuffer command = new StringBuffer();
            command.append(installationFolder).append("/Binaries/Win64/KFGameSteamServer.bin.x86_64 ");
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
                if (profile.getCustomParameters().startsWith("?")) {
                    command.append(profile.getCustomParameters());
                } else {
                    command.append("?").append(profile.getCustomParameters());
                }
            }
            command.append("?ConfigSubDir=").append(profile.getName());

            replaceInFileKfEngineIni(installationFolder, profile, "LinuxServer-KFEngine.ini");
            replaceInFileKfWebIni(installationFolder, profile, StandardCharsets.UTF_8);
            replaceInFileKfGameIni(installationFolder, profile, "LinuxServer-KFGame.ini");

            Process proccess = Runtime.getRuntime().exec(new String[]{"xterm",
                    "-T", "Running the server",
                    "-fa", "DejaVu Sans Mono",
                    "-fs", "11",
                    "-geometry", "120x25+0-0",
                    "-xrm", "XTerm.vt100.allowTitleOps: false",
                    "-e", command.toString()},
                    null, new File(installationFolder));
            Session.getInstance().getProcessList().add(proccess);

            return command.toString();
        } catch (Exception e) {
            String message = "Error executing Killing Floor 2 server";
            logger.error(message, e);
            Utils.errorDialog(message, "See stacktrace for more details", e);
            return null;
        }
    }

    @Override
    protected File getSteamExeFile() {
        File steamExeFile = new File("/usr/games/steam");
        if (steamExeFile.exists() && steamExeFile.canExecute()) {
            return steamExeFile;
        }
        return null;
    }

    @Override
    public Long getIdWorkShopFromPath(Path path, String installationFolder) {
        try {
            String[] array = path.toString().replace(installationFolder, "").replace("/KFGame/Cache/", "").split("/");
            return Long.parseLong(array[0]);
        } catch (Exception e) {
            logger.error("Error getting idWorkShop from path: " + path.toString(), e);
            return null;
        }
    }
}
