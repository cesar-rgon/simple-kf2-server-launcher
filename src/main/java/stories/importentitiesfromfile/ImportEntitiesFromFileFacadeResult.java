package stories.importentitiesfromfile;

import framework.FacadeResult;

import java.util.Properties;

public class ImportEntitiesFromFileFacadeResult extends FacadeResult {

    private Properties properties;

    public ImportEntitiesFromFileFacadeResult() {
        super();
    }

    public ImportEntitiesFromFileFacadeResult(Properties properties) {
        super();
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
