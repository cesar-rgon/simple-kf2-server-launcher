package stories.installupdateepicserver;

import stories.getplatforminstallationfolder.GetPlatformInstallationFolderFacadeResult;

public interface InstallUpdateEpicServerManagerFacade {
    GetPlatformInstallationFolderFacadeResult execute() throws Exception;
    boolean saveOrUpdateProperty(String propertyFilePath, String key, String newValue) throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;
    boolean updatePlatformInstallationFolder(String installationFolder) throws Exception;
    public void installUpdateServer() throws Exception;
}
