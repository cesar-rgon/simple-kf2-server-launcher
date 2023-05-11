package stories.importcustommapsfromserver;

import framework.ModelContext;
import pojos.PlatformProfileMapToImport;

import java.util.List;

public class ImportCustomMapsFromServerModelContext extends ModelContext {
    private final List<PlatformProfileMapToImport> ppmToImportList;
    private final String profileName;

    public ImportCustomMapsFromServerModelContext(List<PlatformProfileMapToImport> ppmToImportList, String profileName) {
        this.ppmToImportList = ppmToImportList;
        this.profileName = profileName;
    }

    public List<PlatformProfileMapToImport> getPpmToImportList() {
        return ppmToImportList;
    }

    public String getProfileName() {
        return profileName;
    }
}
