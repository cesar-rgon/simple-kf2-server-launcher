package stories.getplatformprofilelistbyidworkshop;

import framework.ModelContext;

public class GetPlatformProfileListByIdworkshopModelContext extends ModelContext {
    private final Long idWorkShop;

    public GetPlatformProfileListByIdworkshopModelContext(Long idWorkShop) {
        super();
        this.idWorkShop = idWorkShop;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }
}
