package stories.checkstatusservices;

import framework.ModelContext;

public class CheckStatusServicesModelContext extends ModelContext {

    private final String actualSelectedLanguage;

    public CheckStatusServicesModelContext(String actualSelectedLanguage) {
        super();
        this.actualSelectedLanguage = actualSelectedLanguage;
    }

    public String getActualSelectedLanguage() {
        return actualSelectedLanguage;
    }
}
