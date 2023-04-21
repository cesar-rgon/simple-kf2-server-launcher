package stories.listvaluesmaincontent;

import dtos.ProfileDto;
import dtos.SelectDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class ListValuesMainContentFacadeResult extends FacadeResult {

    private ObservableList<ProfileDto> profileDtoList;
    private ObservableList<SelectDto> languageDtoList;

    public ListValuesMainContentFacadeResult() {
        super();
    }

    public ListValuesMainContentFacadeResult(ObservableList<ProfileDto> profileDtoList, ObservableList<SelectDto> languageDtoList) {
        super();
        this.profileDtoList = profileDtoList;
        this.languageDtoList = languageDtoList;
    }

    public ObservableList<ProfileDto> getProfileDtoList() {
        return profileDtoList;
    }

    public void setProfileDtoList(ObservableList<ProfileDto> profileDtoList) {
        this.profileDtoList = profileDtoList;
    }

    public ObservableList<SelectDto> getLanguageDtoList() {
        return languageDtoList;
    }

    public void setLanguageDtoList(ObservableList<SelectDto> languageDtoList) {
        this.languageDtoList = languageDtoList;
    }

}
