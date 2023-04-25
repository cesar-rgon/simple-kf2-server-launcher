package stories.updateprofilesetwebport;

import framework.ModelContext;

public class UpdateProfileSetWebPortModelContext extends ModelContext {

    private final String profileName;
    private final Integer webPort;

    public UpdateProfileSetWebPortModelContext(String profileName, Integer webPort) {
        super();
        this.profileName = profileName;
        this.webPort = webPort;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getWebPort() {
        return webPort;
    }
}
