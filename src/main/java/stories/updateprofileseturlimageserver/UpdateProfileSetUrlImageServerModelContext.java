package stories.updateprofileseturlimageserver;

import framework.ModelContext;

public class UpdateProfileSetUrlImageServerModelContext extends ModelContext {

    private final String profileName;
    private final String urlImageServer;

    public UpdateProfileSetUrlImageServerModelContext(String profileName, String urlImageServer) {
        super();
        this.profileName = profileName;
        this.urlImageServer = urlImageServer;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getUrlImageServer() {
        return urlImageServer;
    }
}
