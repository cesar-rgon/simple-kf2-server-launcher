package pojos.kf2factory;

import constants.PropertyKey;
import dtos.ProfileDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import stories.installupdateserver.InstallUpdateServerFacade;
import stories.installupdateserver.InstallUpdateServerFacadeImpl;
import utils.Utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public abstract class Kf2Common {

    protected final InstallUpdateServerFacade installUpdateServerFacade;

    protected Kf2Common() {
        this.installUpdateServerFacade = new InstallUpdateServerFacadeImpl();
    }

    public void installOrUpdateServer(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) {
        if (prepareSteamCmd(installationFolder)) {
            installUpdateKf2Server(installationFolder, validateFiles, isBeta, betaBrunch);
        } else {
            Utils.errorDialog("Error preparing SteamCmd to be able to install KF2 server", "The installation process is aborted.", null);
        }
    }

    protected abstract boolean prepareSteamCmd(String installationFolder);
    protected abstract void installUpdateKf2Server(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch);

    public String runServer(ProfileDto profileDto) {
        try {
            String errorMessage = validateParameters(profileDto);
            if (StringUtils.isEmpty(errorMessage)) {
                String installationFolder = installUpdateServerFacade.findPropertyValue(PropertyKey.INSTALLATION_FOLDER);
                createConfigFolder(installationFolder, profileDto);
                return runKf2Server(installationFolder, profileDto);
            } else {
                Utils.errorDialog("Error validating parameters. The server can not be launched!", errorMessage, null);
            }
        } catch (SQLException e) {
            Utils.errorDialog("Error executing Killing Floor 2 server", "See stacktrace for more details", e);
        }
        return null;
    }

    protected String validateParameters(ProfileDto profileDto) {
        StringBuffer errorMessage = new StringBuffer();

        if (profileDto == null || StringUtils.isEmpty(profileDto.getName())) {
            return errorMessage.append("The profile name can not be empty.").toString();
        }
        if (profileDto.getLanguage() == null) {
            errorMessage.append("The language can not be empty.\n");
        }
        if (profileDto.getGametype() == null) {
            errorMessage.append("The game type can not be empty.\n");
        }
        if (profileDto.getMap() == null) {
            errorMessage.append("The map can not be empty.\n");
        }
        if (profileDto.getDifficulty() == null) {
            errorMessage.append("The difficulty can not be empty.\n");
        }
        if (profileDto.getLength() == null) {
            errorMessage.append("The length can not be empty.\n");
        }
        if (profileDto.getMaxPlayers() == null) {
            errorMessage.append("The max.players can not be empty.\n");
        }
        if (StringUtils.isEmpty(profileDto.getServerName())) {
            errorMessage.append("The server name can not be empty.\n");
        }
        return errorMessage.toString();
    }

    protected void createConfigFolder(String installationFolder, ProfileDto profileDto) {
        try {
            File configFolder = new File(installationFolder + "\\KFGame\\Config");
            File profileFolder = new File(configFolder.getAbsolutePath() + "\\" + profileDto.getName());
            if (!profileFolder.isDirectory() || !profileFolder.exists()) {
                if (profileFolder.mkdir()) {
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
                        FileUtils.copyFileToDirectory(sourceFile, profileFolder);
                    }
                }
            }
        } catch (IOException e) {
            Utils.errorDialog("Error copying files to profiles's config folder", "See stacktrace for more details", e);
        }
    }

    protected abstract String runKf2Server(String installationFolder, ProfileDto profile);

    public void joinServer(ProfileDto profile) {
        File steamExeFile = getSteamExeFile();
        if (steamExeFile != null) {
            joinToKf2Server(steamExeFile, profile);
        } else {
            Utils.errorDialog("Error validating Steam installation directory", "The process is aborted.", null);
        }
    }

    protected abstract File getSteamExeFile();
    protected abstract void joinToKf2Server(File steamExeFile, ProfileDto profile);
}
