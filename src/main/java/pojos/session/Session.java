package pojos.session;

import dtos.ProfileDto;

public class Session {

    private static Session instance = null;

    private ProfileDto actualProfile;
    private String console;

    /**
     * Singleton constructor
     */
    private Session() {
        super();
        console = "";
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
}
