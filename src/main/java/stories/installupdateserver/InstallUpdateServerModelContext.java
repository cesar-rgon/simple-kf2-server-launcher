package stories.installupdateserver;

import framework.ModelContext;
import pojos.enums.EnumPlatform;

public class InstallUpdateServerModelContext extends ModelContext {

    private final EnumPlatform enumPlatform;
    private final boolean validateFiles;
    private final boolean beta;
    private final String betaBrunch;

    public InstallUpdateServerModelContext(EnumPlatform enumPlatform, boolean validateFiles, boolean beta, String betaBrunch) {
        super();
        this.enumPlatform = enumPlatform;
        this.validateFiles = validateFiles;
        this.beta = beta;
        this.betaBrunch = betaBrunch;
    }

    public EnumPlatform getEnumPlatform() {
        return enumPlatform;
    }

    public boolean isValidateFiles() {
        return validateFiles;
    }

    public boolean isBeta() {
        return beta;
    }

    public String getBetaBrunch() {
        return betaBrunch;
    }
}
