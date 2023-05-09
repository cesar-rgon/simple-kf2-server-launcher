package stories.deletemapfromplatformprofile;

import framework.ModelContext;

public class DeleteMapFromPlatformProfileModelContext extends ModelContext {
    private final String mapName;
    private final String platformName;
    private final String profileName;

    public DeleteMapFromPlatformProfileModelContext(String mapName, String platformName, String profileName) {
        super();
        this.mapName = mapName;
        this.platformName = platformName;
        this.profileName = profileName;
    }

    public String getMapName() {
        return mapName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getProfileName() {
        return profileName;
    }
}
