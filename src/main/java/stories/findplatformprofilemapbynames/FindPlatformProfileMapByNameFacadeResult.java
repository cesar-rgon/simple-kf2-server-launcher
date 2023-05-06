package stories.findplatformprofilemapbynames;

import dtos.PlatformProfileMapDto;
import framework.FacadeResult;

import java.util.Optional;

public class FindPlatformProfileMapByNameFacadeResult extends FacadeResult {

    private Optional<PlatformProfileMapDto> platformProfileMapDtoOptional;

    public FindPlatformProfileMapByNameFacadeResult() {
        super();
    }

    public FindPlatformProfileMapByNameFacadeResult(Optional<PlatformProfileMapDto> platformProfileMapDtoOptional) {
        super();
        this.platformProfileMapDtoOptional = platformProfileMapDtoOptional;
    }

    public Optional<PlatformProfileMapDto> getPlatformProfileMapDtoOptional() {
        return platformProfileMapDtoOptional;
    }

    public void setPlatformProfileMapDtoOptional(Optional<PlatformProfileMapDto> platformProfileMapDtoOptional) {
        this.platformProfileMapDtoOptional = platformProfileMapDtoOptional;
    }
}
