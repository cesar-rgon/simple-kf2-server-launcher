package pojos.session;

import dtos.MapDto;
import dtos.ProfileDto;

import java.util.ArrayList;
import java.util.List;

public class Session {

    private static Session instance = null;

    private ProfileDto actualProfile;
    private String console;
    private MapDto map;
    private List<Process> processList;

    /**
     * Singleton constructor
     */
    private Session() {
        super();
        console = "";
        map = null;
        processList = new ArrayList<Process>();
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

    public MapDto getMap() {
        return map;
    }

    public void setMap(MapDto map) {
        this.map = map;
    }

    public List<Process> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Process> processList) {
        this.processList = processList;
    }
}
