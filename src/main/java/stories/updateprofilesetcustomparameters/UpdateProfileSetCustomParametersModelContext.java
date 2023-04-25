package stories.updateprofilesetcustomparameters;

import framework.ModelContext;

public class UpdateProfileSetCustomParametersModelContext extends ModelContext {

    private final String profileName;
    private final String customParameters;

    public UpdateProfileSetCustomParametersModelContext(String profileName, String customParameters) {
        super();
        this.profileName = profileName;
        this.customParameters = customParameters;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getCustomParameters() {
        return customParameters;
    }
}
