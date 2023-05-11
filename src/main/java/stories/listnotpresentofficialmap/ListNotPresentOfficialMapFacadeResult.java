package stories.listnotpresentofficialmap;

import framework.FacadeResult;
import pojos.MapToDisplay;

import java.util.List;

public class ListNotPresentOfficialMapFacadeResult extends FacadeResult {

    private List<MapToDisplay> notPresentOfficialMapList;

    public ListNotPresentOfficialMapFacadeResult() {
        super();
    }

    public ListNotPresentOfficialMapFacadeResult(List<MapToDisplay> notPresentOfficialMapList) {
        super();
        this.notPresentOfficialMapList = notPresentOfficialMapList;
    }

    public List<MapToDisplay> getNotPresentOfficialMapList() {
        return notPresentOfficialMapList;
    }

    public void setNotPresentOfficialMapList(List<MapToDisplay> notPresentOfficialMapList) {
        this.notPresentOfficialMapList = notPresentOfficialMapList;
    }
}
