package stories.updateprofilesetmapvotingtime;

import framework.ModelContext;

public class UpdateProfileSetMapVotingTimeModelContext extends ModelContext {

    private final String profileName;
    private final Double mapVotingTime;

    public UpdateProfileSetMapVotingTimeModelContext(String profileName, Double mapVotingTime) {
        super();
        this.profileName = profileName;
        this.mapVotingTime = mapVotingTime;
    }

    public String getProfileName() {
        return profileName;
    }

    public Double getMapVotingTime() {
        return mapVotingTime;
    }
}
