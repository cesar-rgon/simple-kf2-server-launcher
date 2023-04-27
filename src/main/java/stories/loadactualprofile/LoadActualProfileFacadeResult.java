package stories.loadactualprofile;

import dtos.PlatformDto;
import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import framework.FacadeResult;
import javafx.collections.ObservableList;

import java.util.Optional;

public class LoadActualProfileFacadeResult extends FacadeResult {
    private ProfileDto profileDto;
    private ObservableList<PlatformProfileMapDto> filteredMapDtoList;
    private Optional<PlatformProfileMapDto> selectedProfileMapOptional;

    public LoadActualProfileFacadeResult() {
        super();
    }

    public LoadActualProfileFacadeResult(ProfileDto profileDto,
                                         ObservableList<PlatformProfileMapDto> filteredMapDtoList,
                                         Optional<PlatformProfileMapDto> selectedProfileMapOptional) {
        super();
        this.profileDto = profileDto;
        this.filteredMapDtoList = filteredMapDtoList;
        this.selectedProfileMapOptional = selectedProfileMapOptional;
    }

    public ProfileDto getProfileDto() {
        return profileDto;
    }

    public void setProfileDto(ProfileDto profileDto) {
        this.profileDto = profileDto;
    }

    public ObservableList<PlatformProfileMapDto> getFilteredMapDtoList() {
        return filteredMapDtoList;
    }

    public void setFilteredMapDtoList(ObservableList<PlatformProfileMapDto> filteredMapDtoList) {
        this.filteredMapDtoList = filteredMapDtoList;
    }

    public Optional<PlatformProfileMapDto> getSelectedProfileMapOptional() {
        return selectedProfileMapOptional;
    }

    public void setSelectedProfileMapOptional(Optional<PlatformProfileMapDto> selectedProfileMapOptional) {
        this.selectedProfileMapOptional = selectedProfileMapOptional;
    }
}
