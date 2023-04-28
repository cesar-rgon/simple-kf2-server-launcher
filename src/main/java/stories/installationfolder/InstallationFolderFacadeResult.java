package stories.installationfolder;

import framework.FacadeResult;

public class InstallationFolderFacadeResult extends FacadeResult {

    private boolean isCorrectInstallationFolder;

    public InstallationFolderFacadeResult() {
        super();
    }

    public InstallationFolderFacadeResult(boolean isCorrectInstallationFolder) {
        super();
        this.isCorrectInstallationFolder = isCorrectInstallationFolder;
    }

    public boolean isCorrectInstallationFolder() {
        return isCorrectInstallationFolder;
    }

    public void setCorrectInstallationFolder(boolean correctInstallationFolder) {
        isCorrectInstallationFolder = correctInstallationFolder;
    }
}
