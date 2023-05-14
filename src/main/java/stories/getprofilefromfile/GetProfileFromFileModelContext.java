package stories.getprofilefromfile;

import framework.ModelContext;

import java.util.Properties;

public class GetProfileFromFileModelContext extends ModelContext {

    private final int profileIndex;
    private final Properties properties;

    public GetProfileFromFileModelContext(int profileIndex, Properties properties) {
        super();
        this.profileIndex = profileIndex;
        this.properties = properties;
    }

    public int getProfileIndex() {
        return profileIndex;
    }

    public Properties getProperties() {
        return properties;
    }
}
