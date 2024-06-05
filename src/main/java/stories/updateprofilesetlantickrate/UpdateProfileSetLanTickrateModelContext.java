package stories.updateprofilesetlantickrate;

import framework.ModelContext;

public class UpdateProfileSetLanTickrateModelContext extends ModelContext {

    private final String profileName;
    private final Integer lanTickrate;

    public UpdateProfileSetLanTickrateModelContext(String profileName, Integer lanTickrate) {
        super();
        this.profileName = profileName;
        this.lanTickrate = lanTickrate;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getLanTickrate() {
        return lanTickrate;
    }
}
