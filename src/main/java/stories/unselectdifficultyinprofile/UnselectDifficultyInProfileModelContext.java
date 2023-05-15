package stories.unselectdifficultyinprofile;

import framework.ModelContext;

public class UnselectDifficultyInProfileModelContext extends ModelContext {

    private final String actualProfileName;
    private final String selectedDifficultyCode;

    public UnselectDifficultyInProfileModelContext(String actualProfileName, String selectedDifficultyCode) {
        super();
        this.actualProfileName = actualProfileName;
        this.selectedDifficultyCode = selectedDifficultyCode;
    }

    public String getActualProfileName() {
        return actualProfileName;
    }

    public String getSelectedDifficultyCode() {
        return selectedDifficultyCode;
    }
}
