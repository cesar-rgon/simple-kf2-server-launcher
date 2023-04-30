package pojos.kf2factory;

import daos.SteamPlatformDao;
import entities.Profile;
import entities.SteamPlatform;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import services.PlatformService;
import services.PlatformServiceImpl;
import utils.Utils;

import java.io.File;
import java.sql.SQLException;
import java.util.Optional;

public abstract class Kf2Steam extends Kf2AbstractCommon {

    private static final Logger logger = LogManager.getLogger(Kf2Steam.class);
    private final PlatformService platformService;

    protected Kf2Steam(EntityManager em) {
        super(em);
        this.platformService = new PlatformServiceImpl(em);

        try {
            Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();;
            this.platform = steamPlatformOptional.orElse(null);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            this.platform = null;
        }
    }

    protected abstract File getExeFile();
    protected abstract void installUpdateKf2Server(boolean validateFiles, boolean isBeta, String betaBrunch) throws Exception;
    protected abstract void applyPatchToDownloadMaps() throws Exception;

    public void installOrUpdateServer(boolean validateFiles, boolean isBeta, String betaBrunch) {
        if (!prerequisitesAreValid()) {
            try {
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.notOperationDone");
                String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.installationFolderNotValid");
                Utils.warningDialog(headerText, contentText);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                Utils.errorDialog(e.getMessage(), e);
            }
            return;
        }

        try {
            installUpdateKf2Server(validateFiles, isBeta, betaBrunch);
            applyPatchToDownloadMaps();
        } catch (Exception e) {
            String message = "Error installing KF2 server";
            logger.error(message, e);
            Utils.errorDialog(message, e);
        }
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
