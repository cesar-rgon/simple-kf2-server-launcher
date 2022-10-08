package stories.installupdatesteamserver;

import entities.SteamPlatform;

import java.sql.SQLException;
import java.util.Optional;

public interface InstallUpdateSteamServerFacade {
    boolean saveOrUpdateProperty(String key, String newValue) throws Exception;
    String findPropertyValue(String key) throws Exception;

    String getPlatformInstallationFolder() throws SQLException;

    boolean updatePlatformInstallationFolder(String installationFolder) throws Exception;

    Optional<SteamPlatform> findSteamPlatform() throws SQLException;
}
