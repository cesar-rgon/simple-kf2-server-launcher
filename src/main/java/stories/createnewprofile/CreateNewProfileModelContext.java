package stories.createnewprofile;

import framework.ModelContext;

public class CreateNewProfileModelContext extends ModelContext {

    private final String profileName;

    public CreateNewProfileModelContext(String profileName) {
        super();
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }
}
