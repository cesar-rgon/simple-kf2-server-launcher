package stories.updateprofilesetreadyupdelay;

import framework.ModelContext;

public class UpdateProfileSetReadyUpDelayModelContext extends ModelContext {

    private final String profileName;
    private final Integer readyUpDelay;

    public UpdateProfileSetReadyUpDelayModelContext(String profileName, Integer readyUpDelay) {
        super();
        this.profileName = profileName;
        this.readyUpDelay = readyUpDelay;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getReadyUpDelay() {
        return readyUpDelay;
    }
}
