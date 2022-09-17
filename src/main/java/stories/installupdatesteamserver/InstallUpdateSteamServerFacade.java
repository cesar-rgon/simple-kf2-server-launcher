package stories.installupdatesteamserver;

import java.sql.SQLException;

public interface InstallUpdateSteamServerFacade {
    boolean saveOrUpdateProperty(String key, String newValue) throws Exception;
    String findPropertyValue(String key) throws Exception;

    String getPlatformInstallationFolder() throws SQLException;

    boolean updatePlatformInstallationFolder(String installationFolder) throws SQLException;
}
