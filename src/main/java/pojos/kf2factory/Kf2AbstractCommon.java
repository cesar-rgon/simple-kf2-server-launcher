package pojos.kf2factory;

import entities.AbstractPlatform;
import entities.CustomMapMod;
import entities.Profile;
import jakarta.persistence.EntityManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumRunServer;
import utils.Utils;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;

public abstract class Kf2AbstractCommon extends Kf2Utils implements Kf2Common {

    private static final Logger logger = LogManager.getLogger(Kf2Common.class);

    protected AbstractPlatform platform;

    protected Kf2AbstractCommon(EntityManager em) {
        super(em);
    }

    public abstract boolean isValidInstallationFolder();
    public abstract boolean isValidInstallationFolder(boolean checkIfInstalled);
    protected abstract void executeFileBeforeRunServer(File fileToBeExecuted) throws Exception;
    public abstract Long getIdWorkShopFromPath(Path path);
    public abstract String joinServer(Profile profile);
    protected abstract boolean prepareSteamCmd();
    public abstract boolean downloadMapFromSteamCmd(CustomMapMod customMap) throws Exception;
    public abstract String copyMapToCachePlatform(CustomMapMod customMap) throws Exception;
    protected abstract String runServerInTerminal(Profile profile);
    protected abstract String runServerAsService(Profile profile);
    public abstract String stopService(Profile profile, boolean uninstallService);

    protected boolean prerequisitesAreValid() {
        return prerequisitesAreValid(true);
    }

    protected boolean prerequisitesAreValid(boolean checkIfInstalled) {
        return isValidInstallationFolder(checkIfInstalled) && prepareSteamCmd();
    }

    protected String getParameters(Profile profile) {
        StringBuffer parameters = new StringBuffer();
        parameters.append(profile.getMap().getCode());
        parameters.append("?Game=").append(profile.getGametype().getCode());
        if (profile.getGametype().isDifficultyEnabled()) {
            parameters.append("?Difficulty=").append(profile.getDifficulty().getCode());
        }
        parameters.append("?MaxPlayers=").append(profile.getMaxPlayers().getCode());
        if (profile.getGamePort() != null) {
            parameters.append("?Port=").append(profile.getGamePort());
        }
        if (profile.getQueryPort() != null) {
            parameters.append("?QueryPort=").append(profile.getQueryPort());
        }
        if (StringUtils.isNotEmpty(profile.getCustomParameters())) {
            if (profile.getCustomParameters().startsWith("?")) {
                parameters.append(profile.getCustomParameters());
            } else {
                parameters.append("?").append(profile.getCustomParameters());
            }
        }
        parameters.append("?ConfigSubDir=").append(profile.getCode());
        return parameters.toString();
    }

    public String runServer(Profile profile, EnumRunServer enumRunServer) {
        try {
            String errorMessage = validateParameters(profile);
            if (StringUtils.isEmpty(errorMessage)) {
                if (prerequisitesAreValid()) {
                    createConfigFolder(this.platform.getInstallationFolder(), profile.getCode());
                    if (EnumRunServer.TERMINAL.equals(enumRunServer)) {
                        return runServerInTerminal(profile);
                    } else {
                        return runServerAsService(profile);
                    }
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

        /*

         */
    }

    public String runServerByConsole(Profile profile) {
        byConsole = true;
        return runServer(profile, EnumRunServer.TERMINAL);
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

    protected void downloadMapImages() throws Exception {
        String tempFolder = System.getProperty("java.io.tmpdir");
        String downloadConnectionTimeOut = propertyService.getPropertyValue("properties/config.properties", "prop.config.downloadConnectionTimeout");
        String downloadReadTimeOut = propertyService.getPropertyValue("properties/config.properties", "prop.config.downloadReadTimeout");

        Integer totalMaps = Integer.parseInt(propertyService.getPropertyValue("properties/maps.properties", "prop.maps.total_maps"));
        for (int i = 1; i <= totalMaps; i++) {
            String mapTitle = propertyService.getPropertyValue("properties/maps.properties", "prop.maps." + i + ".title");
            File mapImageInServer = new File(this.platform.getInstallationFolder() + "/KFGame/Web/images/maps/" + mapTitle + ".jpg");

            if (!mapImageInServer.exists()) {
                String sourceImageUrl = propertyService.getPropertyValue("properties/maps.properties", "prop.maps." + i + ".source_image_path");

                if (sourceImageUrl.endsWith(".png")) {
                    File tempPngFile = new File(tempFolder + "/" + mapTitle + ".png");
                    FileUtils.copyURLToFile(
                            new URL(sourceImageUrl),
                            tempPngFile,
                            Integer.parseInt(downloadConnectionTimeOut),
                            Integer.parseInt(downloadReadTimeOut)
                    );
                    Utils.convertPngToJpg(tempPngFile, mapImageInServer);
                    tempPngFile.delete();

                } else { // jpg
                    FileUtils.copyURLToFile(
                            new URL(sourceImageUrl),
                            mapImageInServer,
                            Integer.parseInt(downloadConnectionTimeOut),
                            Integer.parseInt(downloadReadTimeOut)
                    );
                }
            }
        }
    }
}
