package stories.webadmin;

import framework.ModelContext;

public class WebAdminModelContext extends ModelContext {

    private final String profileName;

    public WebAdminModelContext(String profileName) {
        super();
        this.profileName = profileName;
    }

    public String getProfileName() {
        return profileName;
    }
}
