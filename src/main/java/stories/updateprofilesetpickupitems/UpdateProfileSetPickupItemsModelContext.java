package stories.updateprofilesetpickupitems;

import framework.ModelContext;

public class UpdateProfileSetPickupItemsModelContext extends ModelContext {

    private final String profileName;
    private final boolean pickupItemsSelected;

    public UpdateProfileSetPickupItemsModelContext(String profileName, boolean pickupItemsSelected) {
        super();
        this.profileName = profileName;
        this.pickupItemsSelected = pickupItemsSelected;
    }

    public String getProfileName() {
        return profileName;
    }

    public boolean isPickupItemsSelected() {
        return pickupItemsSelected;
    }
}
