package stories.installupdatesteamserver;

import daos.SteamPlatformDao;
import entities.AbstractPlatform;
import entities.SteamPlatform;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.sql.SQLException;
import java.util.Optional;

public class InstallUpdateSteamServerFacadeImpl implements InstallUpdateSteamServerFacade {

    private static final Logger logger = LogManager.getLogger(InstallUpdateSteamServerFacadeImpl.class);

    private final PropertyService propertyService;

    public InstallUpdateSteamServerFacadeImpl() {
        super();
        propertyService = new PropertyServiceImpl();
    }

    @Override
    public boolean saveOrUpdateProperty(String key, String newValue) throws Exception {
        propertyService.setProperty("properties/config.properties", key, newValue);
        return true;
    }

    @Override
    public String findPropertyValue(String key) throws Exception {
        return propertyService.getPropertyValue("properties/config.properties", key);
    }

    @Override
    public String getPlatformInstallationFolder() throws SQLException {
        Optional<SteamPlatform> platformOptional = SteamPlatformDao.getInstance().findByCode(EnumPlatform.STEAM.name());
        if (!platformOptional.isPresent()) {
            logger.error("The platform Steam has not been found in database");
            return StringUtils.EMPTY;
        }
        return platformOptional.get().getInstallationFolder();
    }

    @Override
    public boolean updatePlatformInstallationFolder(String installationFolder) throws SQLException {
        Optional<SteamPlatform> platformOptional = SteamPlatformDao.getInstance().findByCode(EnumPlatform.STEAM.name());
        if (!platformOptional.isPresent()) {
            logger.error("The platform Steam has not been found in database");
            return false;
        }
        platformOptional.get().setInstallationFolder(installationFolder);
        return SteamPlatformDao.getInstance().update(platformOptional.get());
    }
}