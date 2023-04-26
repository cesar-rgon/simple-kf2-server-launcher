package stories.updateprofilesetannounceadminlogin;

import framework.ModelContext;

public class UpdateProfileSetAnnounceAdminLoginModelContext extends ModelContext {

    private final String profileName;
    private final boolean announceAdminLoginSelected;

    public UpdateProfileSetAnnounceAdminLoginModelContext(String profileName, boolean announceAdminLoginSelected) {
        super();
        this.profileName = profileName;
        this.announceAdminLoginSelected = announceAdminLoginSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isAnnounceAdminLoginSelected() {
        return announceAdminLoginSelected;
    }
}
