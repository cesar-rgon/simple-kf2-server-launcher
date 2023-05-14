package stories.createitem;

import dtos.SelectDto;
import framework.FacadeResult;

public class CreateItemFacadeResult<D extends SelectDto> extends FacadeResult {

    private D newItem;

    public CreateItemFacadeResult() {
        super();
    }

    public CreateItemFacadeResult(D newItem) {
        super();
        this.newItem = newItem;
    }

    public D getNewItem() {
        return newItem;
    }

    public void setNewItem(D newItem) {
        this.newItem = newItem;
    }
}
