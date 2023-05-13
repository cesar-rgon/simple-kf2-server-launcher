package stories.deleteselectedprofile;

import framework.ModelContext;

public class DeleteSelectedProfileModelContext extends ModelContext {
    private final String profileName;

    public DeleteSelectedProfileModelContext(String profileName) {
        super();
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }
}
