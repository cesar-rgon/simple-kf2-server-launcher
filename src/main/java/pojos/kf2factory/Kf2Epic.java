package pojos.kf2factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.Utils;

public abstract class Kf2Epic extends Kf2Common {

    private static final Logger logger = LogManager.getLogger(Kf2Epic.class);

    protected abstract boolean isValid(String installationFolder);

    @Override
    protected boolean prerequisitesAreValid(String installationFolder) {
        return isValid(installationFolder);
    }
    protected abstract void installUpdateKf2Server(String installationFolder);

    @Override
    protected String getInstallationFolder() throws Exception  {
        return propertyService.getPropertyValue("properties/config.properties", "prop.config.epicInstallationFolder");
    }


    public void installOrUpdateServer(String installationFolder) {
        if (prerequisitesAreValid(installationFolder)) {
            installUpdateKf2Server(installationFolder);
        }
    }

}
