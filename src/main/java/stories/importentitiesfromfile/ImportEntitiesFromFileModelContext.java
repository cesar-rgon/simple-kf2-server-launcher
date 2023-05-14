package stories.importentitiesfromfile;

import framework.ModelContext;

import java.io.File;

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
