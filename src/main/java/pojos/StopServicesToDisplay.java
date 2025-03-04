package pojos;

import java.util.List;

public class StopServicesToDisplay {

    private List<PlatformProfileToDisplay> selectedProfiles;
    private boolean uninstallServices;

    public StopServicesToDisplay(List<PlatformProfileToDisplay> selectedProfiles, boolean uninstallServices) {
        super();
        this.selectedProfiles = selectedProfiles;
        this.uninstallServices = uninstallServices;
    }

    public List<PlatformProfileToDisplay> getSelectedProfiles() {
        return selectedProfiles;
    }

    public void setSelectedProfiles(List<PlatformProfileToDisplay> selectedProfiles) {
        this.selectedProfiles = selectedProfiles;
    }

    public boolean isUninstallServices() {
        return uninstallServices;
    }

    public void setUninstallServices(boolean uninstallServices) {
        this.uninstallServices = uninstallServices;
    }
}
