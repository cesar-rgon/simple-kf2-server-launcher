package stories.selectprofilestobeexported;

import framework.FacadeResult;
import pojos.ProfileToDisplay;

import java.util.List;

public class SelectProfilesToBeExportedFacadeResult extends FacadeResult {

    private List<ProfileToDisplay> profileToDisplayList;

    public SelectProfilesToBeExportedFacadeResult() {
        super();
    }

    public SelectProfilesToBeExportedFacadeResult(List<ProfileToDisplay> profileToDisplayList) {
        super();
        this.profileToDisplayList = profileToDisplayList;
    }

    public List<ProfileToDisplay> getProfileToDisplayList() {
        return profileToDisplayList;
    }

    public void setProfileToDisplayList(List<ProfileToDisplay> profileToDisplayList) {
        this.profileToDisplayList = profileToDisplayList;
    }
}
