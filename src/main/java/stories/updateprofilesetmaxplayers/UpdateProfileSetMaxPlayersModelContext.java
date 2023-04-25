package stories.updateprofilesetmaxplayers;

import framework.ModelContext;

public class UpdateProfileSetMaxPlayersModelContext extends ModelContext {

    private final String profileName;
    private final String maxPlayers;

    public UpdateProfileSetMaxPlayersModelContext(String profileName, String maxPlayers) {
        super();
        this.profileName = profileName;
        this.maxPlayers = maxPlayers;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getMaxPlayers() {
        return maxPlayers;
    }
}
