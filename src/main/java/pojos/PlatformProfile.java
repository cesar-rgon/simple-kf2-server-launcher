package pojos;

import entities.AbstractPlatform;
import entities.Profile;

public class PlatformProfile {
    private AbstractPlatform platform;
    private Profile profile;

    public PlatformProfile() {
        super();
    }

    public PlatformProfile(AbstractPlatform platform, Profile profile) {
        this.platform = platform;
        this.profile = profile;
    }

    public AbstractPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(AbstractPlatform platform) {
        this.platform = platform;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
