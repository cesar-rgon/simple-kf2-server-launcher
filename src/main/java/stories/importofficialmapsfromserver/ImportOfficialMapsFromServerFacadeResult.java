package stories.importofficialmapsfromserver;

import framework.FacadeResult;
import pojos.ImportMapResultToDisplay;

import java.util.List;

public class ImportOfficialMapsFromServerFacadeResult extends FacadeResult {
    private final List<ImportMapResultToDisplay> importMapResultToDisplayList;

    public ImportOfficialMapsFromServerFacadeResult(List<ImportMapResultToDisplay> importMapResultToDisplayList) {
        this.importMapResultToDisplayList = importMapResultToDisplayList;
    }

    public List<ImportMapResultToDisplay> getImportMapResultToDisplayList() {
        return importMapResultToDisplayList;
    }
}
