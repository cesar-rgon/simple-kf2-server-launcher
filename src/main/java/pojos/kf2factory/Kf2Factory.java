package pojos.kf2factory;

import entities.AbstractPlatform;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import services.PropertyService;
import services.PropertyServiceImpl;
import utils.Utils;

public class Kf2Factory {

    private static final Logger logger = LogManager.getLogger(Kf2Factory.class);

    public static Kf2Common getInstance(AbstractPlatform platform) {
        return getInstance(platform, null);
    }

    public static Kf2Common getInstance(AbstractPlatform platform, EntityManager em) {
        if (platform == null || StringUtils.isBlank(platform.getCode())) {
            logger.error("The platform can not be empty");
            return null;
        }
        if (EnumPlatform.EPIC.name().equals(platform.getCode())) {
            return new Kf2EpicWindowsImpl(em);
        }
        String os = System.getProperty("os.name");
        if (StringUtils.isEmpty(os)) {
            logger.error("Operating System not detected");
            return null;
        }

        if (os.contains("Windows")) {
            return new Kf2SteamWindowsImpl(em);
        } else {
            if (os.contains("Linux")) {
                return new Kf2SteamLinuxImpl(em);
            }
        }
        return null;
    }
}
