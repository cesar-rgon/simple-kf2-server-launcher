package stories.listprofiles;

import dtos.ProfileDto;
import framework.FacadeResult;

import java.util.List;

public class ListProfilesFacadeResult extends FacadeResult {

    private List<ProfileDto> profileDtoList;

    public ListProfilesFacadeResult() {
        super();
    }

    public ListProfilesFacadeResult(List<ProfileDto> profileDtoList) {
        super();
        this.profileDtoList = profileDtoList;
    }

    public List<ProfileDto> getProfileDtoList() {
        return profileDtoList;
    }

    public void setProfileDtoList(List<ProfileDto> profileDtoList) {
        this.profileDtoList = profileDtoList;
    }
}
