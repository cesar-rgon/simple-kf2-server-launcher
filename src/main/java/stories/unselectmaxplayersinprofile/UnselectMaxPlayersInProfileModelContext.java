package stories.unselectmaxplayersinprofile;

import framework.ModelContext;

public class UnselectMaxPlayersInProfileModelContext extends ModelContext {

    private final String actualProfileName;
    private final String selectedMaxPlayersCode;

    public UnselectMaxPlayersInProfileModelContext(String actualProfileName, String selectedMaxPlayersCode) {
        super();
        this.actualProfileName = actualProfileName;
        this.selectedMaxPlayersCode = selectedMaxPlayersCode;
    }

    public String getActualProfileName() {
        return actualProfileName;
    }

    public String getSelectedMaxPlayersCode() {
        return selectedMaxPlayersCode;
    }
}
