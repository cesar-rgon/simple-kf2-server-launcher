package stories.getplatformprofilelistbyidworkshop;

import framework.FacadeResult;
import pojos.PlatformProfile;

import java.util.List;

public class GetPlatformProfileListByIdworkshopFacadeResult extends FacadeResult {

    private List<PlatformProfile> platformProfileList;

    public GetPlatformProfileListByIdworkshopFacadeResult() {
        super();
    }

    public GetPlatformProfileListByIdworkshopFacadeResult(List<PlatformProfile> platformProfileList) {
        super();
        this.platformProfileList = platformProfileList;
    }

    public List<PlatformProfile> getPlatformProfileList() {
        return platformProfileList;
    }

    public void setPlatformProfileList(List<PlatformProfile> platformProfileList) {
        this.platformProfileList = platformProfileList;
    }
}
