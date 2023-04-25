package old.installupdateepicserver;

import entities.EpicPlatform;

import java.sql.SQLException;
import java.util.Optional;

public interface InstallUpdateEpicServerFacade {
    String getPlatformInstallationFolder() throws SQLException;

    boolean updatePlatformInstallationFolder(String installationFolder) throws Exception;
    Optional<EpicPlatform> findEpicPlatform() throws SQLException;
}
