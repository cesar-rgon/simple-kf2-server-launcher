package stories.installupdateepicserver;

import java.sql.SQLException;

public interface InstallUpdateEpicServerFacade {
    String getPlatformInstallationFolder() throws SQLException;

    boolean updatePlatformInstallationFolder(String installationFolder) throws SQLException;
}
