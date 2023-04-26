package stories.updateprofilesetmaxspectators;

import framework.ModelContext;

public class UpdateProfileSetMaxSpectatorsModelContext extends ModelContext {

    private final String profileName;
    private final Integer maxSpectators;

    public UpdateProfileSetMaxSpectatorsModelContext(String profileName, Integer maxSpectators) {
        super();
        this.profileName = profileName;
        this.maxSpectators = maxSpectators;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getMaxSpectators() {
        return maxSpectators;
    }
}
