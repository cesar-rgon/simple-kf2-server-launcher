package stories.installationfolder;

import framework.ModelContext;

public class InstallationFolderModelContext extends ModelContext {

    private final String platformName;

    public InstallationFolderModelContext(String platformName) {
        super();
        this.platformName = platformName;
    }

    public String getPlatformName() {
        return platformName;
    }
}
