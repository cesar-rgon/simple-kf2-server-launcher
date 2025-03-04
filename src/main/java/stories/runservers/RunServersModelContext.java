package stories.runservers;

import framework.ModelContext;
import pojos.enums.EnumRunServer;

public class RunServersModelContext extends ModelContext {
    private final String platformName;
    private final String actualSelectedProfileName;
    private final String actualSelectedLanguage;
    private final EnumRunServer enumRunServer;

    public RunServersModelContext(String platformName, String actualSelectedProfileName, String actualSelectedLanguage, EnumRunServer enumRunServer) {
        super();
        this.platformName = platformName;
        this.actualSelectedProfileName = actualSelectedProfileName;
        this.actualSelectedLanguage = actualSelectedLanguage;
        this.enumRunServer = enumRunServer;
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

    public EnumRunServer getEnumRunServer() {
        return enumRunServer;
    }
}
