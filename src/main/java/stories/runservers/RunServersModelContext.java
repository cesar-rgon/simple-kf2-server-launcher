package stories.runservers;

import framework.ModelContext;

public class RunServersModelContext extends ModelContext {
    private final String platformName;
    private final String actualSelectedProfileName;
    private final String actualSelectedLanguage;

    public RunServersModelContext(String platformName, String actualSelectedProfileName, String actualSelectedLanguage) {
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
