package stories.unselectgametypeinprofile;

import framework.ModelContext;

public class UnselectGametypeInProfileModelContext extends ModelContext {

    private final String actualProfileName;
    private final String selectedGametypeCode;

    public UnselectGametypeInProfileModelContext(String actualProfileName, String selectedGametypeCode) {
        super();
        this.actualProfileName = actualProfileName;
        this.selectedGametypeCode = selectedGametypeCode;
    }

    public String getActualProfileName() {
        return actualProfileName;
    }

    public String getSelectedGametypeCode() {
        return selectedGametypeCode;
    }
}
