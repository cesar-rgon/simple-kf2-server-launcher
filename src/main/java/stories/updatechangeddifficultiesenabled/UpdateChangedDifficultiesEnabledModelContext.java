package stories.updatechangeddifficultiesenabled;

import framework.ModelContext;

public class UpdateChangedDifficultiesEnabledModelContext extends ModelContext {

    private final String code;
    private final Boolean newDifficultiesEnabled;

    public UpdateChangedDifficultiesEnabledModelContext(String code, Boolean newDifficultiesEnabled) {
        super();
        this.code = code;
        this.newDifficultiesEnabled = newDifficultiesEnabled;
    }

    public String getCode() {
        return code;
    }

    public Boolean getNewDifficultiesEnabled() {
        return newDifficultiesEnabled;
    }
}
