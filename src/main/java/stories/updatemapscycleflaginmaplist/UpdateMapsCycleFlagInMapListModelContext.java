package stories.updatemapscycleflaginmaplist;

import framework.ModelContext;

import java.util.List;

public class UpdateMapsCycleFlagInMapListModelContext extends ModelContext {

    private final String profileName;
    private final List<String> steamOfficialMapNameListToRemoveFromMapsCycle;
    private final List<String> steamCustomMapNameListToRemoveFromMapsCycle;
    private final List<String> epicOfficialMapNameListToRemoveFromMapsCycle;
    private final List<String> epicCustomMapNameListToRemoveFromMapsCycle;
    private final boolean isInMapsCycle;

    public UpdateMapsCycleFlagInMapListModelContext(String profileName,
                                                    List<String> steamOfficialMapNameListToRemoveFromMapsCycle,
                                                    List<String> steamCustomMapNameListToRemoveFromMapsCycle,
                                                    List<String> epicOfficialMapNameListToRemoveFromMapsCycle,
                                                    List<String> epicCustomMapNameListToRemoveFromMapsCycle,
                                                    boolean isInMapsCycle) {
        super();
        this.profileName = profileName;
        this.steamOfficialMapNameListToRemoveFromMapsCycle = steamOfficialMapNameListToRemoveFromMapsCycle;
        this.steamCustomMapNameListToRemoveFromMapsCycle = steamCustomMapNameListToRemoveFromMapsCycle;
        this.epicOfficialMapNameListToRemoveFromMapsCycle = epicOfficialMapNameListToRemoveFromMapsCycle;
        this.epicCustomMapNameListToRemoveFromMapsCycle = epicCustomMapNameListToRemoveFromMapsCycle;
        this.isInMapsCycle = isInMapsCycle;
    }

    public String getProfileName() {
        return profileName;
    }

    public List<String> getSteamOfficialMapNameListToRemoveFromMapsCycle() {
        return steamOfficialMapNameListToRemoveFromMapsCycle;
    }

    public List<String> getSteamCustomMapNameListToRemoveFromMapsCycle() {
        return steamCustomMapNameListToRemoveFromMapsCycle;
    }

    public List<String> getEpicOfficialMapNameListToRemoveFromMapsCycle() {
        return epicOfficialMapNameListToRemoveFromMapsCycle;
    }

    public List<String> getEpicCustomMapNameListToRemoveFromMapsCycle() {
        return epicCustomMapNameListToRemoveFromMapsCycle;
    }

    public boolean isInMapsCycle() {
        return isInMapsCycle;
    }
}
