package stories.updateplatforminstallationfolder;

import framework.ModelContext;
import pojos.enums.EnumPlatform;

public class UpdatePlatformInstallationFolderModelContext extends ModelContext {
    private final EnumPlatform enumPlatform;
    private final String installationFolder;

    public UpdatePlatformInstallationFolderModelContext(EnumPlatform enumPlatform, String installationFolder) {
        super();
        this.enumPlatform = enumPlatform;
        this.installationFolder = installationFolder;
    }

    public EnumPlatform getEnumPlatform() {
        return enumPlatform;
    }

    public String getInstallationFolder() {
        return installationFolder;
    }
}
