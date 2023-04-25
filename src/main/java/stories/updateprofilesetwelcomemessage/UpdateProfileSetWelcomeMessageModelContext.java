package stories.updateprofilesetwelcomemessage;

import framework.ModelContext;

public class UpdateProfileSetWelcomeMessageModelContext extends ModelContext {

    private final String profileName;
    private final String welcomeMessage;

    public UpdateProfileSetWelcomeMessageModelContext(String profileName, String welcomeMessage) {
        super();
        this.profileName = profileName;
        this.welcomeMessage = welcomeMessage;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }
}
