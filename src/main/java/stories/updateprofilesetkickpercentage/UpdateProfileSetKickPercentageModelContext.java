package stories.updateprofilesetkickpercentage;

import framework.ModelContext;

public class UpdateProfileSetKickPercentageModelContext extends ModelContext {

    private final String profileName;
    private final Double kickPercentage;

    public UpdateProfileSetKickPercentageModelContext(String profileName, Double kickPercentage) {
        super();
        this.profileName = profileName;
        this.kickPercentage = kickPercentage;
    }

    public String getProfileName() {
        return profileName;
    }

    public Double getKickPercentage() {
        return kickPercentage;
    }
}
