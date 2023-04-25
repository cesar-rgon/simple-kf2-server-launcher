package stories.updateprofilesetgametype;

import framework.ModelContext;

public class UpdateProfileSetGameTypeModelContext extends ModelContext {

    private final String profileName;
    private final String gameTypeCode;

    public UpdateProfileSetGameTypeModelContext(String profileName, String gameTypeCode) {
        super();
        this.profileName = profileName;
        this.gameTypeCode = gameTypeCode;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getGameTypeCode() {
        return gameTypeCode;
    }
}
