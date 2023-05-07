package stories.getplatformprofilelistwithoutmap;

import framework.FacadeResult;
import pojos.PlatformProfile;

import java.util.List;

public class GetPlatformProfileListWithoutMapFacadeResult extends FacadeResult {

    private List<PlatformProfile> platformProfileListWithoutMap;

    public GetPlatformProfileListWithoutMapFacadeResult() {
        super();
    }

    public GetPlatformProfileListWithoutMapFacadeResult(List<PlatformProfile> platformProfileListWithoutMap) {
        super();
        this.platformProfileListWithoutMap = platformProfileListWithoutMap;
    }

    public List<PlatformProfile> getPlatformProfileListWithoutMap() {
        return platformProfileListWithoutMap;
    }

    public void setPlatformProfileListWithoutMap(List<PlatformProfile> platformProfileListWithoutMap) {
        this.platformProfileListWithoutMap = platformProfileListWithoutMap;
    }
}
