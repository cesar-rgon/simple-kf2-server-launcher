package pojos.session;

import dtos.AbstractMapDto;
import dtos.PlatformDto;
import dtos.ProfileDto;
import entities.PlatformProfileMap;
import pojos.enums.EnumMapsTab;
import pojos.enums.EnumSortedMapsCriteria;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static Session instance = null;

    private ProfileDto actualProfile;
    private String console;
    private AbstractMapDto map;
    private List<Process> processList;
    private ProfileDto mapsProfile;
    private EnumSortedMapsCriteria sortedMapsCriteria;
    private EnumMapsTab selectedMapTab;
    private List<PlatformProfileMap> platformProfileMapList;
    private PlatformDto platform;

    /**
     * Singleton constructor
     */
    private Session() {
        super();
        console = "";
        map = null;
        processList = new ArrayList<Process>();
        sortedMapsCriteria = EnumSortedMapsCriteria.NAME_DESC;
        selectedMapTab = EnumMapsTab.STEAM_OFFICIAL_MAPS_TAB;
        platformProfileMapList = new ArrayList<PlatformProfileMap>();
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public boolean isRunningProcess() {
        if (processList.isEmpty()) {
            return false;
        }
        for (Process process: processList) {
            if (process.isAlive()) {
                return true;
            }
        }
        return false;
    }

    public ProfileDto getActualProfile() {
        return actualProfile;
    }

    public void setActualProfile(ProfileDto actualProfile) {
        this.actualProfile = actualProfile;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    public AbstractMapDto getMap() {
        return map;
    }

    public void setMap(AbstractMapDto map) {
        this.map = map;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }

    public ProfileDto getMapsProfile() {
        return mapsProfile;
    }

    public void setMapsProfile(ProfileDto mapsProfile) {
        this.mapsProfile = mapsProfile;
    }

    public EnumSortedMapsCriteria getSortedMapsCriteria() {
        return sortedMapsCriteria;
    }

    public void setSortedMapsCriteria(EnumSortedMapsCriteria sortedMapsCriteria) {
        this.sortedMapsCriteria = sortedMapsCriteria;
    }

    public EnumMapsTab getSelectedMapTab() {
        return selectedMapTab;
    }

    public void setSelectedMapTab(EnumMapsTab selectedMapTab) {
        this.selectedMapTab = selectedMapTab;
    }

    public List<PlatformProfileMap> getProfileMapList() {
        return platformProfileMapList;
    }

    public void setProfileMapList(List<PlatformProfileMap> platformProfileMapList) {
        this.platformProfileMapList = platformProfileMapList;
    }

    public List<PlatformProfileMap> getPlatformProfileMapList() {
        return platformProfileMapList;
    }

    public void setPlatformProfileMapList(List<PlatformProfileMap> platformProfileMapList) {
        this.platformProfileMapList = platformProfileMapList;
    }

    public PlatformDto getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformDto platform) {
        this.platform = platform;
    }
}
