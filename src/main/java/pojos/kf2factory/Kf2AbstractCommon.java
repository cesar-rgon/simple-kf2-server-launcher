package pojos.kf2factory;

import entities.AbstractPlatform;
import entities.Profile;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Utils;

import java.io.*;
import java.nio.file.Path;

public abstract class Kf2AbstractCommon extends Kf2Utils implements Kf2Common {

    private static final Logger logger = LogManager.getLogger(Kf2Common.class);

    protected AbstractPlatform platform;

    protected Kf2AbstractCommon(EntityManager em) {
        super(em);
    }

    public abstract boolean isValidInstallationFolder();
    protected abstract String runKf2Server(Profile profile);
    protected abstract void executeFileBeforeRunServer(File fileToBeExecuted) throws Exception;
    public abstract Long getIdWorkShopFromPath(Path path);
    public abstract String joinServer(Profile profile);
    protected abstract boolean prepareSteamCmd();

    protected boolean prerequisitesAreValid() {
        return isValidInstallationFolder() && prepareSteamCmd();
    }

    public String runServer(Profile profile) {
        try {
            String errorMessage = validateParameters(profile);
            if (StringUtils.isEmpty(errorMessage)) {
                if (prerequisitesAreValid()) {
                    createConfigFolder(this.platform.getInstallationFolder(), profile.getCode());
                    return runKf2Server(profile);
                } else {
                    installationFolderNotValid();
                }
            } else {
                String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.errorParameters");
                if (!byConsole) {
                    Utils.warningDialog(headerText, errorMessage);
                } else {
                    System.out.println(headerText + "\n" + errorMessage);
                }
            }
        } catch (Exception e) {
            String message = "Error executing Killing Floor 2 server";
            if (!byConsole) {
                logger.error(message, e);
                Utils.errorDialog(message, e);
            } else {
                System.out.println(message + "\n" + e);
            }
        }
        return null;
    }

    public String runServerByConsole(Profile profile) {
        byConsole = true;
        return runServer(profile);
    }

    public void runExecutableFile() {
        try {
            String executeFileBeforeRunKF2ServerStr = propertyService.getPropertyValue("properties/config.properties", "prop.config.enableExecuteFileBeforeRunKF2Server");
            boolean executeFileBeforeRunKF2Server = StringUtils.isNotBlank(executeFileBeforeRunKF2ServerStr) ? Boolean.parseBoolean(executeFileBeforeRunKF2ServerStr): false;
            if (executeFileBeforeRunKF2Server) {
                String fileToBeExecuted = propertyService.getPropertyValue("properties/config.properties", "prop.config.fileToBeExecuted");
                File file = new File(fileToBeExecuted);
                if (file.exists() && file.isFile() && file.canExecute()) {
                    executeFileBeforeRunServer(file);
                } else {
                    String message = "Error: The file does not exits or is not a file or is not executable";
                    logger.error(StringUtils.isNotBlank(fileToBeExecuted) ? message + ": " + fileToBeExecuted: message);
                    String headerText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.executeFile");
                    String contentText = propertyService.getPropertyValue("properties/languages/" + languageCode + ".properties", "prop.message.fileNotValid");
                    Utils.warningDialog(headerText, StringUtils.isNotBlank(fileToBeExecuted) ? contentText + ": " + fileToBeExecuted: contentText);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Utils.errorDialog(e.getMessage(), e);
        }
    }
}
