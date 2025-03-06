package stories.runservers;

import framework.ModelContext;
import pojos.enums.EnumRunServer;

public class RunServersModelContext extends ModelContext {
    private final String actualSelectedProfileName;
    private final String actualSelectedLanguage;
    private final EnumRunServer enumRunServer;

    public RunServersModelContext(String actualSelectedProfileName, String actualSelectedLanguage, EnumRunServer enumRunServer) {
        super();
        this.actualSelectedProfileName = actualSelectedProfileName;
        this.actualSelectedLanguage = actualSelectedLanguage;
        this.enumRunServer = enumRunServer;
    }

    public String getActualSelectedProfileName() {
        return actualSelectedProfileName;
    }

    public String getActualSelectedLanguage() {
        return actualSelectedLanguage;
    }

    public EnumRunServer getEnumRunServer() {
        return enumRunServer;
    }
}
