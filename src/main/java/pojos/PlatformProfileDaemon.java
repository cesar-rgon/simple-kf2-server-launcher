package pojos;

import entities.AbstractPlatform;
import entities.Profile;

public class PlatformProfileDaemon extends PlatformProfile {

    private String serviceStatus;

    public PlatformProfileDaemon() {
        super();
    }

    public PlatformProfileDaemon(AbstractPlatform platform, Profile profile, String serviceStatus) {
        super(platform, profile);
        this.serviceStatus = serviceStatus;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }
}
