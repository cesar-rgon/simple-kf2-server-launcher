package stories.updateprofilename;

import dtos.ProfileDto;
import framework.FacadeResult;

public class UpdateProfileNameFacadeResult extends FacadeResult {

    private ProfileDto updatedProfile;

    public UpdateProfileNameFacadeResult() {
        super();
    }

    public UpdateProfileNameFacadeResult(ProfileDto updatedProfile) {
        super();
        this.updatedProfile = updatedProfile;
    }

    public ProfileDto getUpdatedProfile() {
        return updatedProfile;
    }

    public void setUpdatedProfile(ProfileDto updatedProfile) {
        this.updatedProfile = updatedProfile;
    }
}
