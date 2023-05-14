package stories.updateitemcode;

import framework.ModelContext;

public class UpdateItemCodeModelContext extends ModelContext {
    private final String oldCode;
    private final String newCode;

    public UpdateItemCodeModelContext(String oldCode, String newCode) {
        super();
        this.oldCode = oldCode;
        this.newCode = newCode;
    }

    public String getOldCode() {
        return oldCode;
    }

    public String getNewCode() {
        return newCode;
    }
}
