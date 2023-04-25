package stories.updateprofilesetservername;

import framework.ModelContext;

public class UpdateProfileSetServerNameModelContext extends ModelContext {

    private final String profileName;
    private final String serverName;

    public UpdateProfileSetServerNameModelContext(String profileName, String serverName) {
        super();
        this.profileName = profileName;
        this.serverName = serverName;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getServerName() {
        return serverName;
    }
}
