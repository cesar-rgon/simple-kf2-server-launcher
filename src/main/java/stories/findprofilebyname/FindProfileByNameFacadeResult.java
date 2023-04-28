package stories.findprofilebyname;

import dtos.ProfileDto;
import framework.FacadeResult;

public class FindProfileByNameFacadeResult extends FacadeResult {

    private ProfileDto profileDto;

    public FindProfileByNameFacadeResult() {
        super();
    }

    public FindProfileByNameFacadeResult(ProfileDto profileDto) {
        super();
        this.profileDto = profileDto;
    }

    public ProfileDto getProfileDto() {
        return profileDto;
    }

    public void setProfileDto(ProfileDto profileDto) {
        this.profileDto = profileDto;
    }
}
