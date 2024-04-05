package stories.updateprofilesetmapobjetives;

import framework.ModelContext;

public class UpdateProfileSetMapObjetivesModelContext extends ModelContext {

    private final String profileName;
    private final boolean mapObjetivesSelected;

    public UpdateProfileSetMapObjetivesModelContext(String profileName, boolean mapObjetivesSelected) {
        super();
        this.profileName = profileName;
        this.mapObjetivesSelected = mapObjetivesSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public Boolean isMapObjetivesSelected() {
        return mapObjetivesSelected;
    }
}
