package stories.updateprofilesetfriendlyfirepercentage;

import framework.ModelContext;

public class UpdateProfileSetFriendlyFirePercentageModelContext extends ModelContext {

    private final String profileName;
    private final Double friendlyFirePercentage;

    public UpdateProfileSetFriendlyFirePercentageModelContext(String profileName, Double friendlyFirePercentage) {
        super();
        this.profileName = profileName;
        this.friendlyFirePercentage = friendlyFirePercentage;
    }

    public String getProfileName() {
        return profileName;
    }

    public Double getFriendlyFirePercentage() {
        return friendlyFirePercentage;
    }
}
