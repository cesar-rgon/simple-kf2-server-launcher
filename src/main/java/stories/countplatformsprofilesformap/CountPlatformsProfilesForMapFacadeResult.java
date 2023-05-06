package stories.countplatformsprofilesformap;

import framework.FacadeResult;

public class CountPlatformsProfilesForMapFacadeResult extends FacadeResult {

    private int countPlatformsProfilesForMap;

    public CountPlatformsProfilesForMapFacadeResult() {
        super();
    }

    public CountPlatformsProfilesForMapFacadeResult(int countPlatformsProfilesForMap) {
        super();
        this.countPlatformsProfilesForMap = countPlatformsProfilesForMap;
    }

    public int getCountPlatformsProfilesForMap() {
        return countPlatformsProfilesForMap;
    }

    public void setCountPlatformsProfilesForMap(int countPlatformsProfilesForMap) {
        this.countPlatformsProfilesForMap = countPlatformsProfilesForMap;
    }
}
