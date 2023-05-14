package stories.importprofilesfromfile;

import dtos.ProfileDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class ImportProfilesFromFileFacadeResult extends FacadeResult {

    private ObservableList<ProfileDto> importedProfilesFromFile;

    public ImportProfilesFromFileFacadeResult() {
        super();
    }

    public ImportProfilesFromFileFacadeResult(ObservableList<ProfileDto> importedProfilesFromFile) {
        super();
        this.importedProfilesFromFile = importedProfilesFromFile;
    }

    public ObservableList<ProfileDto> getImportedProfilesFromFile() {
        return importedProfilesFromFile;
    }

    public void setImportedProfilesFromFile(ObservableList<ProfileDto> importedProfilesFromFile) {
        this.importedProfilesFromFile = importedProfilesFromFile;
    }
}
