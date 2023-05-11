package stories.prepareimportmapsfromserver;

import dtos.PlatformDto;
import framework.FacadeResult;
import pojos.MapToDisplay;
import pojos.PlatformProfileToDisplay;

import java.util.List;

public class PrepareImportMapsFromServerFacadeResult extends FacadeResult {

    private List<PlatformProfileToDisplay> platformProfileToDisplayList;
    private List<String> fullProfileNameList;
    private PlatformDto steamPlatform;
    private PlatformDto epicPlatform;
    private List<String> steamOfficialMapNameList;
    private List<String> epicOfficialMapNameList;
    private List<MapToDisplay> steamCustomMapModList;
    private List<MapToDisplay> epicCustomMapModList;

    public PrepareImportMapsFromServerFacadeResult() {
        super();
    }

    public PrepareImportMapsFromServerFacadeResult(
            List<PlatformProfileToDisplay> platformProfileToDisplayList,
            List<String> fullProfileNameList,
            PlatformDto steamPlatform,
            PlatformDto epicPlatform,
            List<String> steamOfficialMapNameList,
            List<String> epicOfficialMapNameList,
            List<MapToDisplay> steamCustomMapModList,
            List<MapToDisplay> epicCustomMapModList
    ) {
        super();
        this.platformProfileToDisplayList = platformProfileToDisplayList;
        this.fullProfileNameList = fullProfileNameList;
        this.steamPlatform = steamPlatform;
        this.epicPlatform = epicPlatform;
        this.steamOfficialMapNameList = steamOfficialMapNameList;
        this.epicOfficialMapNameList = epicOfficialMapNameList;
        this.steamCustomMapModList = steamCustomMapModList;
        this.epicCustomMapModList = epicCustomMapModList;
    }

    public List<PlatformProfileToDisplay> getPlatformProfileToDisplayList() {
        return platformProfileToDisplayList;
    }

    public void setPlatformProfileToDisplayList(List<PlatformProfileToDisplay> platformProfileToDisplayList) {
        this.platformProfileToDisplayList = platformProfileToDisplayList;
    }

    public List<String> getFullProfileNameList() {
        return fullProfileNameList;
    }

    public void setFullProfileNameList(List<String> fullProfileNameList) {
        this.fullProfileNameList = fullProfileNameList;
    }

    public PlatformDto getSteamPlatform() {
        return steamPlatform;
    }

    public void setSteamPlatform(PlatformDto steamPlatform) {
        this.steamPlatform = steamPlatform;
    }

    public PlatformDto getEpicPlatform() {
        return epicPlatform;
    }

    public void setEpicPlatform(PlatformDto epicPlatform) {
        this.epicPlatform = epicPlatform;
    }

    public List<String> getSteamOfficialMapNameList() {
        return steamOfficialMapNameList;
    }

    public void setSteamOfficialMapNameList(List<String> steamOfficialMapNameList) {
        this.steamOfficialMapNameList = steamOfficialMapNameList;
    }

    public List<String> getEpicOfficialMapNameList() {
        return epicOfficialMapNameList;
    }

    public void setEpicOfficialMapNameList(List<String> epicOfficialMapNameList) {
        this.epicOfficialMapNameList = epicOfficialMapNameList;
    }

    public List<MapToDisplay> getSteamCustomMapModList() {
        return steamCustomMapModList;
    }

    public void setSteamCustomMapModList(List<MapToDisplay> steamCustomMapModList) {
        this.steamCustomMapModList = steamCustomMapModList;
    }

    public List<MapToDisplay> getEpicCustomMapModList() {
        return epicCustomMapModList;
    }

    public void setEpicCustomMapModList(List<MapToDisplay> epicCustomMapModList) {
        this.epicCustomMapModList = epicCustomMapModList;
    }
}
