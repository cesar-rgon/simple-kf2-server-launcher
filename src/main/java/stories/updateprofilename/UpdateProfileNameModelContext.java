package stories.updateprofilename;

import framework.ModelContext;

public class UpdateProfileNameModelContext extends ModelContext {

    private final String oldProfileName;
    private final String newProfileName;

    public UpdateProfileNameModelContext(String oldProfileName, String newProfileName) {
        super();
        this.oldProfileName = oldProfileName;
        this.newProfileName = newProfileName;
    }

    public String getOldProfileName() {
        return oldProfileName;
    }

    public String getNewProfileName() {
        return newProfileName;
    }
}
