package stories.updateprofilesettimebetweenkicks;

import framework.ModelContext;

public class UpdateProfileSetTimeBetweenKicksModelContext extends ModelContext {

    private final String profileName;
    private final Double timeBetweenKicks;

    public UpdateProfileSetTimeBetweenKicksModelContext(String profileName, Double timeBetweenKicks) {
        super();
        this.profileName = profileName;
        this.timeBetweenKicks = timeBetweenKicks;
    }

    public String getProfileName() {
        return profileName;
    }

    public Double getTimeBetweenKicks() {
        return timeBetweenKicks;
    }
}
