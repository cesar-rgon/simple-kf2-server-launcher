package stories.getplatforminstallationfolder;

import framework.FacadeResult;

public class GetPlatformInstallationFolderFacadeResult extends FacadeResult {

    private String platformInstallationFolder;

    public GetPlatformInstallationFolderFacadeResult() {
        super();
    }

    public GetPlatformInstallationFolderFacadeResult(String platformInstallationFolder) {
        super();
        this.platformInstallationFolder = platformInstallationFolder;
    }

    public String getPlatformInstallationFolder() {
        return platformInstallationFolder;
    }

    public void setPlatformInstallationFolder(String platformInstallationFolder) {
        this.platformInstallationFolder = platformInstallationFolder;
    }
}
