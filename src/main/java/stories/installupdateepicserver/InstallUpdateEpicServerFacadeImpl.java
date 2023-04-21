package stories.installupdateepicserver;

import daos.EpicPlatformDao;
import daos.SteamPlatformDao;
import entities.AbstractPlatform;
import entities.EpicPlatform;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import services.PlatformService;
import services.PlatformServiceImpl;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.Optional;

public class InstallUpdateEpicServerFacadeImpl implements InstallUpdateEpicServerFacade {

    private static final Logger logger = LogManager.getLogger(InstallUpdateEpicServerFacadeImpl.class);

    private final PlatformService platformService;

    public InstallUpdateEpicServerFacadeImpl() {
        super();
        EntityManager em = null;
        platformService = new PlatformServiceImpl(em);
    }

    @Override
    public String getPlatformInstallationFolder() throws SQLException {
        Optional<EpicPlatform> platformOptional = platformService.findEpicPlatform();
        if (!platformOptional.isPresent()) {
            logger.error("The platform Epic has not been found in database");
            return StringUtils.EMPTY;
        }
        return platformOptional.get().getInstallationFolder();
    }

    @Override
    public boolean updatePlatformInstallationFolder(String installationFolder) throws Exception {
        Optional<EpicPlatform> platformOptional = platformService.findEpicPlatform();
        if (!platformOptional.isPresent()) {
            logger.error("The platform Epic Games has not been found in database");
            return false;
        }
        platformOptional.get().setInstallationFolder(installationFolder);
        return platformService.updateEpicPlatform(platformOptional.get());
    }

    @Override
    public Optional<EpicPlatform> findEpicPlatform() throws SQLException {
        return platformService.findEpicPlatform();
    }
}
