package stories.updateprofilesetlength;

import framework.ModelContext;

public class UpdateProfileSetLengthModelContext extends ModelContext {

    private final String profileName;
    private final String lengthCode;

    public UpdateProfileSetLengthModelContext(String profileName, String lengthCode) {
        super();
        this.profileName = profileName;
        this.lengthCode = lengthCode;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getLengthCode() {
        return lengthCode;
    }
}
