package pojos.kf2factory;

import daos.EpicPlatformDao;
import entities.EpicPlatform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import services.PlatformService;
import services.PlatformServiceImpl;
import utils.Utils;

import java.sql.SQLException;
import java.util.Optional;

public abstract class Kf2Epic extends Kf2AbstractCommon {

    private static final Logger logger = LogManager.getLogger(Kf2Epic.class);
    private final PlatformService platformService;

    protected Kf2Epic() {
        super();
        this.platformService = new PlatformServiceImpl();
        try {
            Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
            this.platform = epicPlatformOptional.isPresent() ? epicPlatformOptional.get() : null;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            this.platform = null;
        }
    }

    protected abstract void installUpdateKf2Server() throws Exception;
    protected abstract void applyPatchToDownloadMaps() throws Exception;

    public void installOrUpdateServer() {
        if (prerequisitesAreValid()) {
            try {
                installUpdateKf2Server();
                applyPatchToDownloadMaps();
            } catch (Exception e) {
                String message = "Error installing KF2 server";
                logger.error(message, e);
                Utils.errorDialog(message, e);
            }
        }
    }

}
