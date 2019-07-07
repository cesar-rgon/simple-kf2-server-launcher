package pojos.kf2factory;

import dtos.ProfileDto;
import org.apache.commons.lang3.StringUtils;
import utils.Utils;

import java.io.File;
import java.io.IOException;

public class Kf2LinuxImpl extends Kf2Common {

    @Override
    protected boolean prepareSteamCmd(String installationFolder) {
        try {
            File steamcmdFile = new File("/usr/games/steamcmd");
            if (!steamcmdFile.exists()) {
                Runtime.getRuntime().exec(new String[]{"x-terminal-emulator","-e","sudo apt -y install steamcmd"});
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
            StringBuffer command = new StringBuffer("x-terminal-emulator -e ");
            command.append("/usr/games/steamcmd +login anonymous +force_install_dir ");
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
            Runtime.getRuntime().exec(command.toString());
        } catch (IOException e) {
            Utils.errorDialog("Error preparing SteamCmd to be able to install KF2 server", "See stacktrace for more details", e);
        }
    }

    @Override
    protected String runKf2Server(String installationFolder, ProfileDto profile) {
        try {
            StringBuffer command = new StringBuffer();
            command.append(installationFolder).append("/Binaries/Win64/KFGameSteamServer.bin.x86_64 ");
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
            replaceInFileKfGameIni("LinuxServer-KFGame.ini", installationFolder, profile);

            Runtime.getRuntime().exec(new String[]{"x-terminal-emulator","-e",command.toString()},null, new File(installationFolder));

            return command.toString();
        } catch (Exception e) {
            Utils.errorDialog("Error executing Killing Floor 2 server", "See stacktrace for more details", e);
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

}
