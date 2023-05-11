package stories.prepareimportmapsfromserver;

import framework.ModelContext;

public class PrepareImportMapsFromServerModelContext extends ModelContext {

    private final String profileName;

    public PrepareImportMapsFromServerModelContext(String profileName) {
        super();
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }
}
