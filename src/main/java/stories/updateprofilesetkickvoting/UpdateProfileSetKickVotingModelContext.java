package stories.updateprofilesetkickvoting;

import framework.ModelContext;

public class UpdateProfileSetKickVotingModelContext extends ModelContext {

    private final String profileName;
    private final boolean kickVotingSelected;

    public UpdateProfileSetKickVotingModelContext(String profileName, boolean kickVotingSelected) {
        super();
        this.profileName = profileName;
        this.kickVotingSelected = kickVotingSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isKickVotingSelected() {
        return kickVotingSelected;
    }
}
