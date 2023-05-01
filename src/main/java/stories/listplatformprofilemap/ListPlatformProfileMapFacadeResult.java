package stories.listplatformprofilemap;

import dtos.PlatformProfileMapDto;
import framework.FacadeResult;

import java.util.List;

public class ListPlatformProfileMapFacadeResult extends FacadeResult {


    private List<PlatformProfileMapDto> steamPlatformProfileMapDtoList;
    private List<PlatformProfileMapDto> epicPlatformProfileMapDtoList;

    public ListPlatformProfileMapFacadeResult() {
        super();
    }

    public ListPlatformProfileMapFacadeResult(
            List<PlatformProfileMapDto> steamPlatformProfileMapDtoList,
            List<PlatformProfileMapDto> epicPlatformProfileMapDtoList
    ) {
        super();
        this.steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList;
        this.epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList;
    }

    public List<PlatformProfileMapDto> getSteamPlatformProfileMapDtoList() {
        return steamPlatformProfileMapDtoList;
    }

    public void setSteamPlatformProfileMapDtoList(List<PlatformProfileMapDto> steamPlatformProfileMapDtoList) {
        this.steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList;
    }

    public List<PlatformProfileMapDto> getEpicPlatformProfileMapDtoList() {
        return epicPlatformProfileMapDtoList;
    }

    public void setEpicPlatformProfileMapDtoList(List<PlatformProfileMapDto> epicPlatformProfileMapDtoList) {
        this.epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList;
    }
}
