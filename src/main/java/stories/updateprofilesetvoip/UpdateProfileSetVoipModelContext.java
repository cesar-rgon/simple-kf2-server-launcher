package stories.updateprofilesetvoip;

import framework.ModelContext;

public class UpdateProfileSetVoipModelContext extends ModelContext {

    private final String profileName;
    private final boolean voipSelected;

    public UpdateProfileSetVoipModelContext(String profileName, boolean voipSelected) {
        super();
        this.profileName = profileName;
        this.voipSelected = voipSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isVoipSelected() {
        return voipSelected;
    }
}
