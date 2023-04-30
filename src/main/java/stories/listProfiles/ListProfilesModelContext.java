package stories.listProfiles;

import framework.ModelContext;

public class ListProfilesModelContext extends ModelContext {

    private final String profileName;

    public ListProfilesModelContext(String profileName) {
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }
}
