package stories.updateprofilesetmaxidletime;

import framework.ModelContext;

public class UpdateProfileSetMaxIdleTimeModelContext extends ModelContext {

    private final String profileName;
    private final Double maxIdleTime;

    public UpdateProfileSetMaxIdleTimeModelContext(String profileName, Double maxIdleTime) {
        super();
        this.profileName = profileName;
        this.maxIdleTime = maxIdleTime;
    }

    public String getProfileName() {
        return profileName;
    }

    public Double getMaxIdleTime() {
        return maxIdleTime;
    }
}
