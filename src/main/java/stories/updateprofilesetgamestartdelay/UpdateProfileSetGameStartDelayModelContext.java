package stories.updateprofilesetgamestartdelay;

import framework.ModelContext;

public class UpdateProfileSetGameStartDelayModelContext extends ModelContext {

    private final String profileName;
    private final Integer gameStartDelay;

    public UpdateProfileSetGameStartDelayModelContext(String profileName, Integer gameStartDelay) {
        super();
        this.profileName = profileName;
        this.gameStartDelay = gameStartDelay;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getGameStartDelay() {
        return gameStartDelay;
    }
}
