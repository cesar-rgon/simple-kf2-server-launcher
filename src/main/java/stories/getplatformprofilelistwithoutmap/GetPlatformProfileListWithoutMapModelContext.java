package stories.getplatformprofilelistwithoutmap;

import framework.ModelContext;

public class GetPlatformProfileListWithoutMapModelContext extends ModelContext {
    private final Long idWorkShop;

    public GetPlatformProfileListWithoutMapModelContext(Long idWorkShop) {
        super();
        this.idWorkShop = idWorkShop;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }
}
