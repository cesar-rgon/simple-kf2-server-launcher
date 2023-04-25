package stories.updateprofilesetwebpassword;

import framework.ModelContext;

public class UpdateProfileSetWebPasswordModelContext extends ModelContext {

    private final String profileName;
    private final String webPassword;

    public UpdateProfileSetWebPasswordModelContext(String profileName, String webPassword) {
        super();
        this.profileName = profileName;
        this.webPassword = webPassword;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getWebPassword() {
        return webPassword;
    }
}
