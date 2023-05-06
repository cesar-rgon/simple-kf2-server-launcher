package stories.countplatformsprofilesformap;

import framework.ModelContext;

public class CountPlatformsProfilesForMapModelContext extends ModelContext {

    private final String customMapName;

    public CountPlatformsProfilesForMapModelContext(String customMapName) {
        super();
        this.customMapName = customMapName;
    }

    public String getCustomMapName() {
        return customMapName;
    }
}
