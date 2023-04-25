package stories.updateprofilesetyourclan;

import framework.ModelContext;

public class UpdateProfileSetYourClanModelContext extends ModelContext {

    private final String profileName;
    private final String yourClan;

    public UpdateProfileSetYourClanModelContext(String profileName, String yourClan) {
        super();
        this.profileName = profileName;
        this.yourClan = yourClan;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getYourClan() {
        return yourClan;
    }
}
