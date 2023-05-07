package stories.listselectedplatformsprofiles;

import framework.FacadeResult;
import pojos.PlatformProfileToDisplay;

import java.util.List;

public class ListSelectedPlatformsProfilesFacadeResult extends FacadeResult {
    private List<PlatformProfileToDisplay> selectedPlatformProfiles;

    public ListSelectedPlatformsProfilesFacadeResult() {
        super();
    }

    public ListSelectedPlatformsProfilesFacadeResult(List<PlatformProfileToDisplay> selectedPlatformProfiles) {
        super();
        this.selectedPlatformProfiles = selectedPlatformProfiles;
    }

    public List<PlatformProfileToDisplay> getSelectedPlatformProfiles() {
        return selectedPlatformProfiles;
    }

    public void setSelectedPlatformProfiles(List<PlatformProfileToDisplay> selectedPlatformProfiles) {
        this.selectedPlatformProfiles = selectedPlatformProfiles;
    }
}
