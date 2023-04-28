package stories.findprofilebyname;

import framework.ModelContext;

public class FindProfileByNameModelContext extends ModelContext {

    private final String profileName;

    public FindProfileByNameModelContext(String profileName) {
        super();
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }
}
