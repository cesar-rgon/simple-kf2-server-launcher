package stories.stopservices;

import framework.ModelContext;

public class StopServicesModelContext extends ModelContext {
    private final String actualSelectedProfileName;
    private final String actualSelectedLanguage;

    public StopServicesModelContext(String actualSelectedProfileName, String actualSelectedLanguage) {
        super();
        this.actualSelectedProfileName = actualSelectedProfileName;
        this.actualSelectedLanguage = actualSelectedLanguage;
    }

    public String getActualSelectedProfileName() {
        return actualSelectedProfileName;
    }

    public String getActualSelectedLanguage() {
        return actualSelectedLanguage;
    }
}
