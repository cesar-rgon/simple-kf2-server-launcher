package stories.cloneselectedprofile;

import framework.ModelContext;

public class CloneSelectedProfileModelContext extends ModelContext {

    private final String profileName;
    private final String newProfileName;

    public CloneSelectedProfileModelContext(String profileName, String newProfileName) {
        super();
        this.profileName = profileName;
        this.newProfileName = newProfileName;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getNewProfileName() {
        return newProfileName;
    }
}
