package stories.loadactualprofile;

import framework.ModelContext;

public class LoadActualProfileModelContext extends ModelContext {

    private final String platformName;
    private final String profileName;

    public LoadActualProfileModelContext(String platformName, String profileName) {
        super();
        this.platformName = platformName;
        this.profileName = profileName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getProfileName() {
        return profileName;
    }
}
