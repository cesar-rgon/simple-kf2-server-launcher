package pojos;

import java.util.List;

public class StopServicesToDisplay {

    private List<PlatformProfileToDisplay> selectedPlatformProfileList;
    private boolean uninstallServices;

    public StopServicesToDisplay(List<PlatformProfileToDisplay> selectedPlatformProfileList, boolean uninstallServices) {
        super();
        this.selectedPlatformProfileList = selectedPlatformProfileList;
        this.uninstallServices = uninstallServices;
    }

    public List<PlatformProfileToDisplay> getSelectedPlatformProfileList() {
        return selectedPlatformProfileList;
    }

    public void setSelectedPlatformProfileList(List<PlatformProfileToDisplay> selectedPlatformProfileList) {
        this.selectedPlatformProfileList = selectedPlatformProfileList;
    }

    public boolean isUninstallServices() {
        return uninstallServices;
    }

    public void setUninstallServices(boolean uninstallServices) {
        this.uninstallServices = uninstallServices;
    }
}
