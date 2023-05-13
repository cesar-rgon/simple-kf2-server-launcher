package stories.createnewprofile;

import dtos.ProfileDto;
import framework.FacadeResult;

public class CreateNewProfileFacadeResult extends FacadeResult {

    private ProfileDto newProfile;

    public CreateNewProfileFacadeResult() {
        super();
    }

    public CreateNewProfileFacadeResult(ProfileDto newProfile) {
        super();
        this.newProfile = newProfile;
    }

    public ProfileDto getNewProfile() {
        return newProfile;
    }

    public void setNewProfile(ProfileDto newProfile) {
        this.newProfile = newProfile;
    }
}
