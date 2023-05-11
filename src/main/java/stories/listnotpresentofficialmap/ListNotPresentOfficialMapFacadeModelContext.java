package stories.listnotpresentofficialmap;

import framework.ModelContext;

import java.util.List;

public class ListNotPresentOfficialMapFacadeModelContext extends ModelContext {
    private final List<String> officialMapNameList;
    private final String platformName;
    private final String profileName;

    public ListNotPresentOfficialMapFacadeModelContext(List<String> officialMapNameList, String platformName, String profileName) {
        super();
        this.officialMapNameList = officialMapNameList;
        this.platformName = platformName;
        this.profileName = profileName;
    }

    public List<String> getOfficialMapNameList() {
        return officialMapNameList;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getProfileName() {
        return profileName;
    }
}
