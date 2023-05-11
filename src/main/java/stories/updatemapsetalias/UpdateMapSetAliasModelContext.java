package stories.updatemapsetalias;

import framework.ModelContext;

public class UpdateMapSetAliasModelContext extends ModelContext {

    private final String platformName;
    private final String profileName;
    private final String mapName;
    private final String newAlias;

    public UpdateMapSetAliasModelContext(String platformName, String profileName, String mapName, String newAlias) {
        super();
        this.platformName = platformName;
        this.profileName = profileName;
        this.mapName = mapName;
        this.newAlias = newAlias;
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

    public String getNewAlias() {
        return newAlias;
    }
}
