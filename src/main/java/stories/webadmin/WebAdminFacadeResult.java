package stories.webadmin;

import dtos.ProfileDto;
import framework.FacadeResult;

public class WebAdminFacadeResult extends FacadeResult {

    private ProfileDto profileDto;

    public WebAdminFacadeResult() {
        super();
    }

    public WebAdminFacadeResult(ProfileDto profileDto) {
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
