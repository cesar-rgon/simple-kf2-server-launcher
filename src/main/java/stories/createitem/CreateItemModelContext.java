package stories.createitem;

import framework.ModelContext;

public class CreateItemModelContext extends ModelContext {

    private final String code;
    private final String description;
    private final String languageCode;

    public CreateItemModelContext(String code, String description, String languageCode) {
        super();
        this.code = code;
        this.description = description;
        this.languageCode = languageCode;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguageCode() {
        return languageCode;
    }
}
