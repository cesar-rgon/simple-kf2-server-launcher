package stories.deletemapfromplatformprofile;

import dtos.AbstractMapDto;
import framework.FacadeResult;

public class DeleteMapFromPlatformProfileFacadeResult extends FacadeResult {

    private AbstractMapDto mapDto;

    public DeleteMapFromPlatformProfileFacadeResult() {
        super();
    }

    public DeleteMapFromPlatformProfileFacadeResult(AbstractMapDto mapDto) {
        super();
        this.mapDto = mapDto;
    }

    public AbstractMapDto getMapDto() {
        return mapDto;
    }

    public void setMapDto(AbstractMapDto mapDto) {
        this.mapDto = mapDto;
    }
}
