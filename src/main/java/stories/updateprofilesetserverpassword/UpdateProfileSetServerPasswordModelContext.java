package stories.updateprofilesetserverpassword;

import framework.ModelContext;

public class UpdateProfileSetServerPasswordModelContext extends ModelContext {
    private final String profileName;
    private final String serverPassword;

    public UpdateProfileSetServerPasswordModelContext(String profileName, String serverPassword) {
        super();
        this.profileName = profileName;
        this.serverPassword = serverPassword;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getServerPassword() {
        return serverPassword;
    }
}
