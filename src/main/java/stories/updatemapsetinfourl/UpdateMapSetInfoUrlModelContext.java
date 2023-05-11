package stories.updatemapsetinfourl;

import framework.ModelContext;

public class UpdateMapSetInfoUrlModelContext extends ModelContext {

    private final String platformName;
    private final String profileName;
    private final String mapName;
    private final String infoUrl;

    public UpdateMapSetInfoUrlModelContext(String platformName, String profileName, String mapName, String infoUrl) {
        super();
        this.platformName = platformName;
        this.profileName = profileName;
        this.mapName = mapName;
        this.infoUrl = infoUrl;
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

    public String getInfoUrl() {
        return infoUrl;
    }
}
