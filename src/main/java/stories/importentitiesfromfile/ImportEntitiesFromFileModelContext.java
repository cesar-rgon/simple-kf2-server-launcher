package stories.importentitiesfromfile;

import entities.Language;
import framework.ModelContext;

import java.io.File;
import java.util.List;

public class ImportEntitiesFromFileModelContext extends ModelContext {

    private final File file;

    public ImportEntitiesFromFileModelContext(File file) {
        super();
        this.file = file;
    }

    public File getFile() {
        return file;
    }

}
