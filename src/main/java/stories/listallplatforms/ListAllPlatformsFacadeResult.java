package stories.listallplatforms;

import dtos.PlatformDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class ListAllPlatformsFacadeResult extends FacadeResult {

    private ObservableList<PlatformDto> allPlatformList;

    public ListAllPlatformsFacadeResult() {
        super();
    }

    public ListAllPlatformsFacadeResult(ObservableList<PlatformDto> allPlatformList) {
        super();
        this.allPlatformList = allPlatformList;
    }

    public ObservableList<PlatformDto> getAllPlatformList() {
        return allPlatformList;
    }

    public void setAllPlatformList(ObservableList<PlatformDto> allPlatformList) {
        this.allPlatformList = allPlatformList;
    }
}
