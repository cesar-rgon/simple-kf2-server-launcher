package stories.findmapbyidworkshop;

import framework.ModelContext;

public class FindMapByIdworkshopModelContext extends ModelContext {

    private final Long idWorkShop;

    public FindMapByIdworkshopModelContext(Long idWorkShop) {
        super();
        this.idWorkShop = idWorkShop;
    }

    public Long getIdWorkShop() {
        return idWorkShop;
    }
}
