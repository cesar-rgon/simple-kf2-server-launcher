package stories.findplatformprofilemapbynames;

import framework.ModelContext;

public class FindPlatformProfileMapByNameModelContext extends ModelContext {

    private final String platformName;
    private final String profileName;
    private final String mapName;

    public FindPlatformProfileMapByNameModelContext(String platformName, String profileName, String mapName) {
        super();
        this.platformName = platformName;
        this.profileName = profileName;
        this.mapName = mapName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getMapName() {
        return mapName;
    }
}
