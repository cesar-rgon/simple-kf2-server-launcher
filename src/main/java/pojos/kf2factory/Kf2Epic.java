package pojos.kf2factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Utils;

public abstract class Kf2Epic extends Kf2AbstractCommon {

    private static final Logger logger = LogManager.getLogger(Kf2Epic.class);

    @Override
    protected boolean prerequisitesAreValid(String installationFolder) {
        return isValid(installationFolder);
    }
    protected abstract void installUpdateKf2Server(String installationFolder) throws Exception;

    @Override
    protected String getInstallationFolder() throws Exception  {
        return propertyService.getPropertyValue("properties/config.properties", "prop.config.epicInstallationFolder");
    }


    public void installOrUpdateServer(String installationFolder) {
        if (prerequisitesAreValid(installationFolder)) {
            try {
                installUpdateKf2Server(installationFolder);
            } catch (Exception e) {
                String message = "Error installing KF2 server";
                logger.error(message, e);
                Utils.errorDialog(message, e);
            }
        }
    }

}
