package stories.importcustommapsfromserver;

import framework.FacadeResult;
import pojos.ImportMapResultToDisplay;

import java.util.List;

public class ImportCustomMapsFromServerFacadeResult extends FacadeResult {
    private final List<ImportMapResultToDisplay> importMapResultToDisplayList;

    public ImportCustomMapsFromServerFacadeResult(List<ImportMapResultToDisplay> importMapResultToDisplayList) {
        this.importMapResultToDisplayList = importMapResultToDisplayList;
    }

    public List<ImportMapResultToDisplay> getImportMapResultToDisplayList() {
        return importMapResultToDisplayList;
    }
}
