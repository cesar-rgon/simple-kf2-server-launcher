package stories.updateprofilesetadmincanpause;

import framework.ModelContext;

public class UpdateProfileSetAdminCanPauseModelContext extends ModelContext {

    private final String profileName;
    private final boolean adminCanPauseSelected;

    public UpdateProfileSetAdminCanPauseModelContext(String profileName, boolean adminCanPauseSelected) {
        super();
        this.profileName = profileName;
        this.adminCanPauseSelected = adminCanPauseSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isAdminCanPauseSelected() {
        return adminCanPauseSelected;
    }
}
