package stories.listselectedplatformsprofiles;

import framework.ModelContext;
import pojos.PlatformProfile;

import java.util.List;

public class ListSelectedPlatformsProfilesModelContext extends ModelContext {

    private final List<PlatformProfile> platformProfileList;

    public ListSelectedPlatformsProfilesModelContext(List<PlatformProfile> platformProfileList) {
        super();
        this.platformProfileList = platformProfileList;
    }

    public List<PlatformProfile> getPlatformProfileList() {
        return platformProfileList;
    }
}
