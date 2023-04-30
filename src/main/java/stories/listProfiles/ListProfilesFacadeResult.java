package stories.listProfiles;

import dtos.ProfileDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class ListProfilesFacadeResult extends FacadeResult {

    private ObservableList<ProfileDto> allProfileDtoList;
    private ProfileDto actualProfileDto;

    public ListProfilesFacadeResult() {
        super();
    }

    public ListProfilesFacadeResult(ObservableList<ProfileDto> allProfileDtoList,
                                    ProfileDto actualProfileDto) {
        super();
        this.allProfileDtoList = allProfileDtoList;
        this.actualProfileDto = actualProfileDto;
    }

    public ObservableList<ProfileDto> getAllProfileDtoList() {
        return allProfileDtoList;
    }

    public void setAllProfileDtoList(ObservableList<ProfileDto> allProfileDtoList) {
        this.allProfileDtoList = allProfileDtoList;
    }

    public ProfileDto getActualProfileDto() {
        return actualProfileDto;
    }

    public void setActualProfileDto(ProfileDto actualProfileDto) {
        this.actualProfileDto = actualProfileDto;
    }
}
