package pojos.kf2factory;

import entities.Profile;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Utils;

import java.io.File;

public abstract class Kf2Steam extends Kf2AbstractCommon {

    private static final Logger logger = LogManager.getLogger(Kf2Steam.class);

    protected abstract boolean prepareSteamCmd(String installationFolder);

    protected abstract File getExeFile();
    protected abstract void installUpdateKf2Server(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) throws Exception;


    public void installOrUpdateServer(String installationFolder, boolean validateFiles, boolean isBeta, String betaBrunch) {
        if (prerequisitesAreValid(installationFolder)) {
            try {
                installUpdateKf2Server(installationFolder, validateFiles, isBeta, betaBrunch);
            } catch (Exception e) {
                String message = "Error installing KF2 server";
                logger.error(message, e);
                Utils.errorDialog(message, e);
            }
        }
    }

    @Override
    protected String getInstallationFolder() throws Exception {
        return propertyService.getPropertyValue("properties/config.properties", "prop.config.steamInstallationFolder");
    }

    @Override
    protected boolean prerequisitesAreValid(String installationFolder) {
        return isValid(installationFolder) && prepareSteamCmd(installationFolder);
    }

    @Override
    public String joinServer(Profile profile) {
        File steamExeFile = getExeFile();
        if (steamExeFile != null) {
            return joinToKf2Server(steamExeFile, profile);
        } else {
            try {
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.steamInstallDirInvalid");
                Utils.warningDialog(headerText, contentText);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                Utils.errorDialog(e.getMessage(), e);
            } finally {
                return "";
            }
        }
    }

    private String joinToKf2Server(File steamExeFile, Profile profile) {
        try {
            String serverPassword = Utils.decryptAES(profile.getServerPassword());
            String passwordParam = "";
            if (StringUtils.isNotEmpty(serverPassword)) {
                passwordParam = "?password=" + serverPassword;
            }
            String gamePortParam = "";
            if (profile.getGamePort() != null) {
                gamePortParam = ":" + profile.getGamePort();
            }
            StringBuffer command = new StringBuffer(steamExeFile.getAbsolutePath());
            command.append(" -applaunch 232090 ").append(Utils.getPublicIp()).append(gamePortParam).append(passwordParam).append(" -nostartupmovies");
            Runtime.getRuntime().exec(command.toString(), null, steamExeFile.getParentFile());
            StringBuffer consoleCommand = new StringBuffer();
            if (StringUtils.isBlank(passwordParam)) {
                consoleCommand = command;
            } else {
                consoleCommand.append(steamExeFile.getAbsolutePath()).append(" -applaunch 232090 ").append(Utils.getPublicIp()).append(gamePortParam).append("?password=*****").append(" -nostartupmovies");
            }
            return consoleCommand.toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
            return "";
        }

    }
}
