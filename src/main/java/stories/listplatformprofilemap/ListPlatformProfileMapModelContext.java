package stories.listplatformprofilemap;

import framework.ModelContext;

public class ListPlatformProfileMapModelContext extends ModelContext {
    private final String profileName;

    public ListPlatformProfileMapModelContext(String profileName) {
        super();
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }
}
