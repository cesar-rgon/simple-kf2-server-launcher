package stories.updateprofilesetqueryport;

import framework.ModelContext;

public class UpdateProfileSetQueryPortModelContext extends ModelContext {

    private final String profileName;
    private final Integer queryPort;

    public UpdateProfileSetQueryPortModelContext(String profileName, Integer queryPort) {
        super();
        this.profileName = profileName;
        this.queryPort = queryPort;
    }

    public String getProfileName() {
        return profileName;
    }

    public Integer getQueryPort() {
        return queryPort;
    }
}
