package stories.cloneselectedprofile;

import dtos.ProfileDto;
import framework.FacadeResult;

public class CloneSelectedProfileFacadeResult extends FacadeResult {
    private ProfileDto clonedProfile;

    public CloneSelectedProfileFacadeResult() {
        super();
    }

    public CloneSelectedProfileFacadeResult(ProfileDto clonedProfile) {
        super();
        this.clonedProfile = clonedProfile;
    }

    public ProfileDto getClonedProfile() {
        return clonedProfile;
    }

    public void setClonedProfile(ProfileDto clonedProfile) {
        this.clonedProfile = clonedProfile;
    }
}
