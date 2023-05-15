package stories.unselectlengthinprofile;

import framework.ModelContext;

public class UnselectLengthInProfileModelContext extends ModelContext {

    private final String actualProfileName;
    private final String selectedLengthCode;

    public UnselectLengthInProfileModelContext(String actualProfileName, String selectedLengthCode) {
        super();
        this.actualProfileName = actualProfileName;
        this.selectedLengthCode = selectedLengthCode;
    }

    public String getActualProfileName() {
        return actualProfileName;
    }

    public String getSelectedLengthCode() {
        return selectedLengthCode;
    }
}
