package pojos.session;

import dtos.ProfileDto;

public class Session {

    private static Session instance = null;

    private ProfileDto actualProfile;
    private boolean showWebAdmin;
    private Integer webPort;
    private String console;

    /**
     * Singleton constructor
     */
    private Session() {
        super();
        showWebAdmin = false;
        webPort = 8080;
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

    public boolean isShowWebAdmin() {
        return showWebAdmin;
    }

    public void setShowWebAdmin(boolean showWebAdmin) {
        this.showWebAdmin = showWebAdmin;
    }

    public Integer getWebPort() {
        return webPort;
    }

    public void setWebPort(Integer webPort) {
        this.webPort = webPort;
    }

    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }
}
