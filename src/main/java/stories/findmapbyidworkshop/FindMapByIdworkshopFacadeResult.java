package stories.findmapbyidworkshop;

import dtos.CustomMapModDto;
import framework.FacadeResult;

public class FindMapByIdworkshopFacadeResult extends FacadeResult {
    private CustomMapModDto customMapDto;

    public FindMapByIdworkshopFacadeResult() {
        super();
    }

    public FindMapByIdworkshopFacadeResult(CustomMapModDto customMapDto) {
        super();
        this.customMapDto = customMapDto;
    }

    public CustomMapModDto getCustomMapDto() {
        return customMapDto;
    }

    public void setCustomMapDto(CustomMapModDto customMapDto) {
        this.customMapDto = customMapDto;
    }
}
