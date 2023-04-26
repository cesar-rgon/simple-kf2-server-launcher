package stories.updateprofilesettakeover;

import framework.ModelContext;

public class UpdateProfileSetTakeoverModelContext extends ModelContext {

    private final String profileName;
    private final boolean takeoverSelected;

    public UpdateProfileSetTakeoverModelContext(String profileName, boolean takeoverSelected) {
        super();
        this.profileName = profileName;
        this.takeoverSelected = takeoverSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isTakeoverSelected() {
        return takeoverSelected;
    }
}
