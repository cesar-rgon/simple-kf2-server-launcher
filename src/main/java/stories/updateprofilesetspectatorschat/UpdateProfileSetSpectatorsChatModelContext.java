package stories.updateprofilesetspectatorschat;

import framework.ModelContext;

public class UpdateProfileSetSpectatorsChatModelContext extends ModelContext {

    private final String profileName;
    private final boolean spectatorsChatSelected;

    public UpdateProfileSetSpectatorsChatModelContext(String profileName, boolean spectatorsChatSelected) {
        super();
        this.profileName = profileName;
        this.spectatorsChatSelected = spectatorsChatSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isSpectatorsChatSelected() {
        return spectatorsChatSelected;
    }
}
