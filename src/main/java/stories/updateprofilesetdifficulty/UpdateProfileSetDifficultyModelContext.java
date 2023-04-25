package stories.updateprofilesetdifficulty;

import framework.ModelContext;

public class UpdateProfileSetDifficultyModelContext extends ModelContext {

    private final String profileName;
    private final String difficultyCode;

    public UpdateProfileSetDifficultyModelContext(String profileName, String difficultyCode) {
        super();
        this.profileName = profileName;
        this.difficultyCode = difficultyCode;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getDifficultyCode() {
        return difficultyCode;
    }
}
