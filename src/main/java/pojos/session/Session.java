package pojos.session;

import dtos.AbstractMapDto;
import dtos.ProfileDto;
import pojos.enums.EnumMasTab;
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
    private EnumMasTab selectedMapTab;

    /**
     * Singleton constructor
     */
    private Session() {
        super();
        console = "";
        map = null;
        processList = new ArrayList<Process>();
        sortedMapsCriteria = EnumSortedMapsCriteria.NOMBRE_DESC;
        selectedMapTab = EnumMasTab.CUSTOM_MAPS_TAB;
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

    public EnumMasTab getSelectedMapTab() {
        return selectedMapTab;
    }

    public void setSelectedMapTab(EnumMasTab selectedMapTab) {
        this.selectedMapTab = selectedMapTab;
    }
}
