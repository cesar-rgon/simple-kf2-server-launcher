package stories.mapsinitialize;

import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

import java.util.List;

public class MapsInitializeFacadeResult extends FacadeResult {

    private boolean steamHasCorrectInstallationFolder;
    private boolean epicHasCorrectInstallationFolder;
    private List<PlatformProfileMapDto> steamPlatformProfileMapDtoList;
    private List<PlatformProfileMapDto> epicPlatformProfileMapDtoList;

    public MapsInitializeFacadeResult() {
        super();
    }

    public MapsInitializeFacadeResult(
            boolean steamHasCorrectInstallationFolder,
            boolean epicHasCorrectInstallationFolder,
            List<PlatformProfileMapDto> steamPlatformProfileMapDtoList,
            List<PlatformProfileMapDto> epicPlatformProfileMapDtoList
    ) {
        super();
        this.steamHasCorrectInstallationFolder = steamHasCorrectInstallationFolder;
        this.epicHasCorrectInstallationFolder = epicHasCorrectInstallationFolder;
        this.steamPlatformProfileMapDtoList = steamPlatformProfileMapDtoList;
        this.epicPlatformProfileMapDtoList = epicPlatformProfileMapDtoList;
    }

    public boolean isSteamHasCorrectInstallationFolder() {
        return steamHasCorrectInstallationFolder;
    }

    public void setSteamHasCorrectInstallationFolder(boolean steamHasCorrectInstallationFolder) {
        this.steamHasCorrectInstallationFolder = steamHasCorrectInstallationFolder;
    }

    public boolean isEpicHasCorrectInstallationFolder() {
        return epicHasCorrectInstallationFolder;
    }

    public void setEpicHasCorrectInstallationFolder(boolean epicHasCorrectInstallationFolder) {
        this.epicHasCorrectInstallationFolder = epicHasCorrectInstallationFolder;
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
