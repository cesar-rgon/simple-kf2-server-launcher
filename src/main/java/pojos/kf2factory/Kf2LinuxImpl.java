package pojos.kf2factory;

import entities.Map;
import entities.Profile;
import org.apache.commons.lang3.StringUtils;
import pojos.session.Session;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
                command.append("?").append(profile.getCustomParameters());
            }
            command.append("?ConfigSubDir=").append(profile.getName());

            replaceInFileKfWebIni(installationFolder, profile);
            replaceInFileKfGameIni("LinuxServer-KFGame.ini", installationFolder, profile);

            Process proccess = Runtime.getRuntime().exec(new String[]{"x-terminal-emulator","-e",command.toString()},null, new File(installationFolder));
            Session.getInstance().getProcessList().add(proccess);

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

    @Override
    public void addCustomMapToKfEngineIni(Long idWorkShop, String installationFolder, String profileName) {
        addCustomMapToKfEngineIni(idWorkShop, installationFolder, profileName, "LinuxServer-KFEngine.ini");
    }

    @Override
    public void removeCustomMapsFromKfEngineIni(List<Long> idWorkShopList, String installationFolder, String profileName) {
        removeCustomMapsFromKfEngineIni(idWorkShopList, installationFolder, profileName, "LinuxServer-KFEngine.ini");
    }

    @Override
    public void addCustomMapToKfGameIni(String mapName, String installationFolder, String profileName, List<Map> mapList) {
        addCustomMapToKfGameIni(mapName, installationFolder, profileName, mapList, "LinuxServer-KFGame.ini");
    }

    @Override
    public void removeCustomMapsFromKfGameIni(List<String> mapNameList, String installationFolder, String profileName, List<Map> mapList) {
        removeCustomMapsFromKfGameIni(mapNameList, installationFolder, profileName, mapList, "LinuxServer-KFGame.ini");
    }
}
