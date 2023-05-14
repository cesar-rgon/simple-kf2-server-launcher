package stories.deleteitem;

import framework.ModelContext;

public class DeleteItemModelContext extends ModelContext {

    private final String code;

    public DeleteItemModelContext(String code) {
        super();
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
