package stories.mapsinitialize;

import framework.ModelContext;

public class MapsInitializeModelContext extends ModelContext {
    private final String profileName;

    public MapsInitializeModelContext(String profileName) {
        super();
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }
}
