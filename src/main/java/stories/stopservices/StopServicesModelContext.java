package stories.stopservices;

import framework.ModelContext;

public class StopServicesModelContext extends ModelContext {
    private final String platformName;
    private final String actualSelectedProfileName;
    private final String actualSelectedLanguage;

    public StopServicesModelContext(String platformName, String actualSelectedProfileName, String actualSelectedLanguage) {
        super();
        this.platformName = platformName;
        this.actualSelectedProfileName = actualSelectedProfileName;
        this.actualSelectedLanguage = actualSelectedLanguage;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getActualSelectedProfileName() {
        return actualSelectedProfileName;
    }

    public String getActualSelectedLanguage() {
        return actualSelectedLanguage;
    }
}
