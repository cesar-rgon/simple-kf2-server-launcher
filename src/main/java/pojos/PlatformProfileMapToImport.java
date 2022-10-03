package pojos;

public class PlatformProfileMapToImport {

    private final String platformName;
    private final String profileName;
    private final MapToDisplay mapToDisplay;

    public PlatformProfileMapToImport(String platformName, String profileName, MapToDisplay mapToDisplay) {
        super();
        this.platformName = platformName;
        this.profileName = profileName;
        this.mapToDisplay = mapToDisplay;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getProfileName() {
        return profileName;
    }

    public MapToDisplay getMapToDisplay() {
        return mapToDisplay;
    }
}
