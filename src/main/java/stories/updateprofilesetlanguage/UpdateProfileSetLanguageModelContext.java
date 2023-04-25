package stories.updateprofilesetlanguage;

import framework.ModelContext;

public class UpdateProfileSetLanguageModelContext extends ModelContext {

    private final String profileName;
    private final String languageCode;

    public UpdateProfileSetLanguageModelContext(String profileName, String languageCode) {
        super();
        this.profileName = profileName;
        this.languageCode = languageCode;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
