package stories.updateprofilesetwebpage;

import framework.ModelContext;

public class UpdateProfileSetWebPageModelContext extends ModelContext {

    private final String profileName;
    private final boolean webPage;

    public UpdateProfileSetWebPageModelContext(String profileName, boolean webPage) {
        super();
        this.profileName = profileName;
        this.webPage = webPage;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean getWebPage() {
        return webPage;
    }
}
