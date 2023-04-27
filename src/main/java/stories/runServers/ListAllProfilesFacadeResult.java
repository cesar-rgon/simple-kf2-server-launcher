package stories.runServers;

import dtos.ProfileDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class ListAllProfilesFacadeResult extends FacadeResult {

    private ObservableList<ProfileDto> profileDtoList;

    public ListAllProfilesFacadeResult() {
        super();
    }

    public ListAllProfilesFacadeResult(ObservableList<ProfileDto> profileDtoList) {
        super();
        this.profileDtoList = profileDtoList;
    }

    public ObservableList<ProfileDto> getProfileDtoList() {
        return profileDtoList;
    }

    public void setProfileDtoList(ObservableList<ProfileDto> profileDtoList) {
        this.profileDtoList = profileDtoList;
    }
}
