package stories.addcustommapstoprofile;

import framework.ModelContext;

import java.util.List;

public class AddCustomMapsToProfileModelContext extends ModelContext {

    private final List<String> platformNameList;
    private final String profileName;
    private final String mapNameList;
    private final String languageCode;
    private final String actualSelectedProfile;


    public AddCustomMapsToProfileModelContext(List<String> platformNameList, String profileName, String mapNameList, String languageCode, String actualSelectedProfile) {
        super();
        this.platformNameList = platformNameList;
        this.profileName = profileName;
        this.mapNameList = mapNameList;
        this.languageCode = languageCode;
        this.actualSelectedProfile = actualSelectedProfile;
    }

    public List<String> getPlatformNameList() {
        return platformNameList;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getMapNameList() {
        return mapNameList;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getActualSelectedProfile() {
        return actualSelectedProfile;
    }

}
