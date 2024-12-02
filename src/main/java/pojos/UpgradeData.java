package pojos;

import java.io.File;

public class UpgradeData {

    private final File targetFolder;
    private final File upgradeTemporalFile;

    public UpgradeData(File targetFolder, File upgradeTemporalFile) {
        super();
        this.targetFolder = targetFolder;
        this.upgradeTemporalFile = upgradeTemporalFile;
    }

    public File getTargetFolder() {
        return targetFolder;
    }

    public File getUpgradeTemporalFile() {
        return upgradeTemporalFile;
    }
}
