package stories.updatemapseturlphoto;

import framework.ModelContext;

public class UpdateMapSetUrlPhotoModelContext extends ModelContext {

    private final String platformName;
    private final String profileName;
    private final String mapName;
    private final String mapPreviewUrl;

    public UpdateMapSetUrlPhotoModelContext(String platformName, String profileName, String mapName, String mapPreviewUrl) {
        super();
        this.platformName = platformName;
        this.profileName = profileName;
        this.mapName = mapName;
        this.mapPreviewUrl = mapPreviewUrl;
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

    public String getMapPreviewUrl() {
        return mapPreviewUrl;
    }
}
