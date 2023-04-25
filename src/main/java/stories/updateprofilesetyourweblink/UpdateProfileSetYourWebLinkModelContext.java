package stories.updateprofilesetyourweblink;

import framework.ModelContext;

public class UpdateProfileSetYourWebLinkModelContext extends ModelContext {
    private final String profileName;
    private final String yourWebLink;

    public UpdateProfileSetYourWebLinkModelContext(String profileName, String yourWebLink) {
        super();
        this.profileName = profileName;
        this.yourWebLink = yourWebLink;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getYourWebLink() {
        return yourWebLink;
    }
}
