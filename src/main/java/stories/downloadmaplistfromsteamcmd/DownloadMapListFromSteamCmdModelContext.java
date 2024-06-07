package stories.downloadmaplistfromsteamcmd;

import framework.ModelContext;

import java.util.List;

public class DownloadMapListFromSteamCmdModelContext extends ModelContext {

    private final List<String> platformNameList;
    private final List<String> mapNameList;

    public DownloadMapListFromSteamCmdModelContext(List<String> platformNameList, List<String> mapNameList) {
        super();
        this.platformNameList = platformNameList;
        this.mapNameList = mapNameList;
    }

    public List<String> getPlatformNameList() {
        return platformNameList;
    }

    public List<String> getMapNameList() {
        return mapNameList;
    }
}
