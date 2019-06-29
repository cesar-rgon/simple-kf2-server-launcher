package pojos.session;

import dtos.MapDto;
import dtos.ProfileDto;

public class Session {

    private static Session instance = null;

    private ProfileDto actualProfile;
    private String console;
    private MapDto map;

    /**
     * Singleton constructor
     */
    private Session() {
        super();
        console = "";
        map = null;
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
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
}
