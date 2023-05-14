package stories.exportprofilestofile;

import framework.ModelContext;
import pojos.ProfileToDisplay;

import java.io.File;
import java.util.List;

public class ExportProfilesToFileModelContext extends ModelContext {

    private final List<ProfileToDisplay> profilesToExportDto;
    private final File file;

    public ExportProfilesToFileModelContext(List<ProfileToDisplay> profilesToExportDto, File file) {
        super();
        this.profilesToExportDto = profilesToExportDto;
        this.file = file;
    }

    public List<ProfileToDisplay> getProfilesToExportDto() {
        return profilesToExportDto;
    }

    public File getFile() {
        return file;
    }
}
