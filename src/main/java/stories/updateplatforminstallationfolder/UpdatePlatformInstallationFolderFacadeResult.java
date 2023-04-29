package stories.updateplatforminstallationfolder;

import framework.FacadeResult;

public class UpdatePlatformInstallationFolderFacadeResult extends FacadeResult {

    private boolean updated;

    public UpdatePlatformInstallationFolderFacadeResult() {
        super();
        this.updated = false;
    }

    public UpdatePlatformInstallationFolderFacadeResult(boolean updated) {
        super();
        this.updated = updated;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }
}
