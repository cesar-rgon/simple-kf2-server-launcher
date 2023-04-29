package stories.getplatforminstallationfolder;

import framework.ModelContext;
import pojos.enums.EnumPlatform;

public class GetPlatformInstallationFolderModelContext extends ModelContext {

    private final EnumPlatform enumPlatform;

    public GetPlatformInstallationFolderModelContext(EnumPlatform enumPlatform) {
        super();
        this.enumPlatform = enumPlatform;
    }

    public EnumPlatform getEnumPlatform() {
        return enumPlatform;
    }
}
