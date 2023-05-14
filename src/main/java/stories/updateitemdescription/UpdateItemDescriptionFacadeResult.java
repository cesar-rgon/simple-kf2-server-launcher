package stories.updateitemdescription;

import dtos.SelectDto;
import framework.FacadeResult;

public class UpdateItemDescriptionFacadeResult<S extends SelectDto> extends FacadeResult {

    private S updatedItem;

    public UpdateItemDescriptionFacadeResult() {
        super();
    }

    public UpdateItemDescriptionFacadeResult(S updatedItem) {
        super();
        this.updatedItem = updatedItem;
    }

    public S getUpdatedItem() {
        return updatedItem;
    }

    public void setUpdatedItem(S updatedItem) {
        this.updatedItem = updatedItem;
    }
}
