package stories.updateprofilesetmapvoting;

import framework.ModelContext;

public class UpdateProfileSetMapVotingModelContext extends ModelContext {

    private final String profileName;
    private final boolean mapVotingSelected;

    public UpdateProfileSetMapVotingModelContext(String profileName, boolean mapVotingSelected) {
        super();
        this.profileName = profileName;
        this.mapVotingSelected = mapVotingSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isMapVotingSelected() {
        return mapVotingSelected;
    }
}
