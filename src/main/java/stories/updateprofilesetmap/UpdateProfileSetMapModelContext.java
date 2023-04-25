package stories.updateprofilesetmap;

import framework.ModelContext;

public class UpdateProfileSetMapModelContext extends ModelContext {

    private final String profileName;
    private final String mapCode;
    private final boolean isOfficial;

    public UpdateProfileSetMapModelContext(String profileName, String mapCode, boolean isOfficial) {
        super();
        this.profileName = profileName;
        this.mapCode = mapCode;
        this.isOfficial = isOfficial;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getMapCode() {
        return mapCode;
    }

    public boolean isOfficial() {
        return isOfficial;
    }
}
