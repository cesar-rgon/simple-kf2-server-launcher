package stories.updateprofilesetdeadplayerscantalk;

import framework.ModelContext;

public class UpdateProfileSetDeadPlayersCanTalkModelContext extends ModelContext {

    private final String profileName;
    private final boolean deadPlayersCanTalkSelected;

    public UpdateProfileSetDeadPlayersCanTalkModelContext(String profileName, boolean deadPlayersCanTalkSelected) {
        super();
        this.profileName = profileName;
        this.deadPlayersCanTalkSelected = deadPlayersCanTalkSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isDeadPlayersCanTalkSelected() {
        return deadPlayersCanTalkSelected;
    }
}
