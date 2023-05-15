package stories.updatechangedlengthsenabled;

import framework.ModelContext;

public class UpdateChangedLengthsEnabledModelContext extends ModelContext {

    private final String code;
    private final Boolean newLengthsEnabled;

    public UpdateChangedLengthsEnabledModelContext(String code, Boolean newLengthsEnabled) {
        super();
        this.code = code;
        this.newLengthsEnabled = newLengthsEnabled;
    }

    public String getCode() {
        return code;
    }

    public Boolean getNewLengthsEnabled() {
        return newLengthsEnabled;
    }
}
