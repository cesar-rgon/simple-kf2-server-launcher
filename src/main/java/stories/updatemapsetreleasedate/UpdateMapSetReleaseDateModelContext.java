package stories.updatemapsetreleasedate;

import framework.ModelContext;

import java.time.LocalDate;

public class UpdateMapSetReleaseDateModelContext extends ModelContext {
    private final String platformName;
    private final String profileName;
    private final String mapName;
    private final LocalDate releaseDate;

    public UpdateMapSetReleaseDateModelContext(String platformName, String profileName, String mapName, LocalDate releaseDate) {
        super();
        this.platformName = platformName;
        this.profileName = profileName;
        this.mapName = mapName;
        this.releaseDate = releaseDate;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getMapName() {
        return mapName;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }
}
