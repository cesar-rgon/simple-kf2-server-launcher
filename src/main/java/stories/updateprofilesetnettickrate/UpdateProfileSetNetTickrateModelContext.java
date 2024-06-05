package stories.updateprofilesetnettickrate;

import framework.ModelContext;

public class UpdateProfileSetNetTickrateModelContext extends ModelContext {

    private final String profileName;
    private final Integer netTickrate;

    public UpdateProfileSetNetTickrateModelContext(String profileName, Integer netTickrate) {
        super();
        this.profileName = profileName;
        this.netTickrate = netTickrate;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getNetTickrate() {
        return netTickrate;
    }
}
