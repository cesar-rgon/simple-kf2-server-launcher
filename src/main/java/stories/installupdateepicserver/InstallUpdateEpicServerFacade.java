package stories.installupdateepicserver;

import entities.EpicPlatform;

import java.sql.SQLException;
import java.util.Optional;

public interface InstallUpdateEpicServerFacade {
    String getPlatformInstallationFolder() throws SQLException;

    boolean updatePlatformInstallationFolder(String installationFolder) throws SQLException;
    Optional<EpicPlatform> findEpicPlatform() throws SQLException;
}
