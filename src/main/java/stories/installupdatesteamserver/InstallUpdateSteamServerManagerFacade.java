package stories.installupdatesteamserver;

import pojos.enums.EnumPlatform;
import stories.getplatforminstallationfolder.GetPlatformInstallationFolderFacadeResult;

public interface InstallUpdateSteamServerManagerFacade {

    GetPlatformInstallationFolderFacadeResult execute() throws Exception;
    boolean saveOrUpdateProperty(String propertyFilePath, String key, String newValue) throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;
    boolean updatePlatformInstallationFolder(String installationFolder) throws Exception;
    void installUpdateServer(boolean validateFiles, boolean beta, String betaBrunch) throws Exception;
}
