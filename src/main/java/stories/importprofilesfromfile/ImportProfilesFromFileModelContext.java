package stories.importprofilesfromfile;

import entities.Profile;
import framework.ModelContext;

import java.util.List;
import java.util.Properties;

public class ImportProfilesFromFileModelContext extends ModelContext {

    private final List<Profile> selectedProfileList;
    private final Properties properties;
    private final StringBuffer errorMessage;

    public ImportProfilesFromFileModelContext(List<Profile> selectedProfileList, Properties properties, StringBuffer errorMessage) {
        super();
        this.selectedProfileList = selectedProfileList;
        this.properties = properties;
        this.errorMessage = errorMessage;
    }

    public List<Profile> getSelectedProfileList() {
        return selectedProfileList;
    }

    public Properties getProperties() {
        return properties;
    }

    public StringBuffer getErrorMessage() {
        return errorMessage;
    }
}
