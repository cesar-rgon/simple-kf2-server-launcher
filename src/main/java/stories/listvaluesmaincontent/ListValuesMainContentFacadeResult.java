package stories.listvaluesmaincontent;

import dtos.GameTypeDto;
import dtos.PlatformDto;
import dtos.ProfileDto;
import dtos.SelectDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

public class ListValuesMainContentFacadeResult extends FacadeResult {

    private ObservableList<ProfileDto> profileDtoList;
    private ObservableList<SelectDto> languageDtoList;
    private ObservableList<GameTypeDto> gameTypeDtoList;
    private ObservableList<SelectDto> difficultyDtoList;
    private ObservableList<SelectDto> lengthDtoList;
    private ObservableList<SelectDto> playerDtoList;
    private ObservableList<PlatformDto> platformDtoList;
    private ProfileDto lastSelectedProfile;

    public ListValuesMainContentFacadeResult() {
        super();
    }

    public ListValuesMainContentFacadeResult(
            ObservableList<ProfileDto> profileDtoList,
            ObservableList<SelectDto> languageDtoList,
            ObservableList<GameTypeDto> gameTypeDtoList,
            ObservableList<SelectDto> difficultyDtoList,
            ObservableList<SelectDto> lengthDtoList,
            ObservableList<SelectDto> playerDtoList,
            ObservableList<PlatformDto> platformDtoList,
            ProfileDto lastSelectedProfile
    ) {
        super();
        this.profileDtoList = profileDtoList;
        this.languageDtoList = languageDtoList;
        this.gameTypeDtoList = gameTypeDtoList;
        this.difficultyDtoList = difficultyDtoList;
        this.lengthDtoList = lengthDtoList;
        this.playerDtoList = playerDtoList;
        this.platformDtoList = platformDtoList;
        this.lastSelectedProfile = lastSelectedProfile;
    }

    public ObservableList<ProfileDto> getProfileDtoList() {
        return profileDtoList;
    }

    public void setProfileDtoList(ObservableList<ProfileDto> profileDtoList) {
        this.profileDtoList = profileDtoList;
    }

    public ObservableList<SelectDto> getLanguageDtoList() {
        return languageDtoList;
    }

    public void setLanguageDtoList(ObservableList<SelectDto> languageDtoList) {
        this.languageDtoList = languageDtoList;
    }

    public ObservableList<GameTypeDto> getGameTypeDtoList() {
        return gameTypeDtoList;
    }

    public void setGameTypeDtoList(ObservableList<GameTypeDto> gameTypeDtoList) {
        this.gameTypeDtoList = gameTypeDtoList;
    }

    public ObservableList<SelectDto> getDifficultyDtoList() {
        return difficultyDtoList;
    }

    public void setDifficultyDtoList(ObservableList<SelectDto> difficultyDtoList) {
        this.difficultyDtoList = difficultyDtoList;
    }

    public ObservableList<SelectDto> getLengthDtoList() {
        return lengthDtoList;
    }

    public void setLengthDtoList(ObservableList<SelectDto> lengthDtoList) {
        this.lengthDtoList = lengthDtoList;
    }

    public ObservableList<SelectDto> getPlayerDtoList() {
        return playerDtoList;
    }

    public void setPlayerDtoList(ObservableList<SelectDto> playerDtoList) {
        this.playerDtoList = playerDtoList;
    }

    public ObservableList<PlatformDto> getPlatformDtoList() {
        return platformDtoList;
    }

    public void setPlatformDtoList(ObservableList<PlatformDto> platformDtoList) {
        this.platformDtoList = platformDtoList;
    }

    public ProfileDto getLastSelectedProfile() {
        return lastSelectedProfile;
    }

    public void setLastSelectedProfile(ProfileDto lastSelectedProfile) {
        this.lastSelectedProfile = lastSelectedProfile;
    }
}
