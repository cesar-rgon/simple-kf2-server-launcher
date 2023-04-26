package stories.updateprofilesetteamcollision;

import framework.ModelContext;

public class UpdateProfileSetTeamCollisionModelContext extends ModelContext {

    private final String profileName;
    private final boolean teamCollisionSelected;

    public UpdateProfileSetTeamCollisionModelContext(String profileName, boolean teamCollisionSelected) {
        super();
        this.profileName = profileName;
        this.teamCollisionSelected = teamCollisionSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isTeamCollisionSelected() {
        return teamCollisionSelected;
    }
}
