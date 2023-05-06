package stories.addplatformprofilestomap;

import framework.ModelContext;

import java.util.List;

public class AddPlatformProfilesToMapModelContext extends ModelContext {

    private final List<String> platformNameList;
    private final String mapName;
    private final String strUrlMapImage;
    private final List<String> profileNameList;

    public AddPlatformProfilesToMapModelContext(List<String> platformNameList, String mapName, String strUrlMapImage, List<String> profileNameList) {
        super();
        this.platformNameList = platformNameList;
        this.mapName = mapName;
        this.strUrlMapImage = strUrlMapImage;
        this.profileNameList = profileNameList;
    }

    public List<String> getPlatformNameList() {
        return platformNameList;
    }

    public String getMapName() {
        return mapName;
    }

    public String getStrUrlMapImage() {
        return strUrlMapImage;
    }

    public List<String> getProfileNameList() {
        return profileNameList;
    }

}
