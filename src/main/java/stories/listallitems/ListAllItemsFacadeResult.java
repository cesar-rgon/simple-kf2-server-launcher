package stories.listallitems;

import dtos.SelectDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class ListAllItemsFacadeResult<D extends SelectDto> extends FacadeResult {

    private ObservableList<D> allItemList;

    public ListAllItemsFacadeResult() {
        super();
    }

    public ListAllItemsFacadeResult(ObservableList<D> allItemList) {
        super();
        this.allItemList = allItemList;
    }

    public ObservableList<D> getAllItemList() {
        return allItemList;
    }

    public void setAllItemList(ObservableList<D> allItemList) {
        this.allItemList = allItemList;
    }
}
