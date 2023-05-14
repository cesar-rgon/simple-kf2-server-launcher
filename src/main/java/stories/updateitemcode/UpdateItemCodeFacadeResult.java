package stories.updateitemcode;

import dtos.SelectDto;
import framework.FacadeResult;

public class UpdateItemCodeFacadeResult<S extends SelectDto> extends FacadeResult {

    private S updatedItem;

    public UpdateItemCodeFacadeResult() {
        super();
    }

    public UpdateItemCodeFacadeResult(S updatedItem) {
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
