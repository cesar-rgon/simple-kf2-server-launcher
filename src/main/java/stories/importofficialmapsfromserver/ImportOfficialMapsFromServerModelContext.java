package stories.importofficialmapsfromserver;

import framework.ModelContext;
import pojos.PlatformProfileMapToImport;

import java.util.List;

public class ImportOfficialMapsFromServerModelContext extends ModelContext {
    private final List<PlatformProfileMapToImport> ppmToImportList;
    private final String profileName;

    public ImportOfficialMapsFromServerModelContext(List<PlatformProfileMapToImport> ppmToImportList, String profileName) {
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
