package stories.listallprofiles;

import dtos.ProfileDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class ListAllProfilesFacadeResult extends FacadeResult {

    private ObservableList<ProfileDto> allProfileList;

    public ListAllProfilesFacadeResult() {
        super();
    }

    public ListAllProfilesFacadeResult(ObservableList<ProfileDto> allProfileList) {
        super();
        this.allProfileList = allProfileList;
    }

    public ObservableList<ProfileDto> getAllProfileList() {
        return allProfileList;
    }

    public void setAllProfileList(ObservableList<ProfileDto> allProfileList) {
        this.allProfileList = allProfileList;
    }
}
