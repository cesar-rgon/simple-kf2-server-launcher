package stories.updateprofilesetgameport;

import framework.ModelContext;

public class UpdateProfileSetGamePortModelContext extends ModelContext {

    private final String profileName;
    private final Integer gamePort;

    public UpdateProfileSetGamePortModelContext(String profileName, Integer gamePort) {
        super();
        this.profileName = profileName;
        this.gamePort = gamePort;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getGamePort() {
        return gamePort;
    }
}
