package stories.mapsinitialize;

import dtos.ProfileDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class MapsInitializeFacadeResult extends FacadeResult {

    private ObservableList<ProfileDto> allProfileDtoList;
    private ProfileDto actualProfileDto;
    private boolean steamHasCorrectInstallationFolder;
    private boolean epicHasCorrectInstallationFolder;

    public MapsInitializeFacadeResult() {
        super();
    }

    public MapsInitializeFacadeResult(ObservableList<ProfileDto> allProfileDtoList,
                                      ProfileDto actualProfileDto,
                                      boolean steamHasCorrectInstallationFolder,
                                      boolean epicHasCorrectInstallationFolder) {
        super();
        this.allProfileDtoList = allProfileDtoList;
        this.actualProfileDto = actualProfileDto;
        this.steamHasCorrectInstallationFolder = steamHasCorrectInstallationFolder;
        this.epicHasCorrectInstallationFolder = epicHasCorrectInstallationFolder;
    }

    public ObservableList<ProfileDto> getAllProfileDtoList() {
        return allProfileDtoList;
    }

    public void setAllProfileDtoList(ObservableList<ProfileDto> allProfileDtoList) {
        this.allProfileDtoList = allProfileDtoList;
    }

    public ProfileDto getActualProfileDto() {
        return actualProfileDto;
    }

    public void setActualProfileDto(ProfileDto actualProfileDto) {
        this.actualProfileDto = actualProfileDto;
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
}
