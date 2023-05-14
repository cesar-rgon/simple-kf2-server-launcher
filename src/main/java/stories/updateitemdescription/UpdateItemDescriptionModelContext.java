package stories.updateitemdescription;

import framework.ModelContext;

public class UpdateItemDescriptionModelContext extends ModelContext {

    private final String code;
    private final String oldDescription;
    private final String newDescription;
    private final String languageCode;

    public UpdateItemDescriptionModelContext(String code, String oldDescription, String newDescription, String languageCode) {
        super();
        this.code = code;
        this.oldDescription = oldDescription;
        this.newDescription = newDescription;
        this.languageCode = languageCode;
    }

    public String getCode() {
        return code;
    }

    public String getOldDescription() {
        return oldDescription;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
