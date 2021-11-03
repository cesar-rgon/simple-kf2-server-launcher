package dtos.factories;

import dtos.ProfileMapDto;
import entities.CustomMapMod;
import entities.OfficialMap;
import entities.ProfileMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class ProfileMapDtoFactory {

    private final ProfileDtoFactory profileDtoFactory;
    private final MapDtoFactory mapDtoFactory;

    public ProfileMapDtoFactory() {
        super();
        this.profileDtoFactory = new ProfileDtoFactory();
        this.mapDtoFactory = new MapDtoFactory();
    }

    public ProfileMapDto newDto(ProfileMap profileMap) {

        LocalDate releaseDate = profileMap.getReleaseDate() != null ? profileMap.getReleaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null;
        LocalDateTime importedDate = profileMap.getImportedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return new ProfileMapDto(
                profileDtoFactory.newDto(profileMap.getProfile()),
                mapDtoFactory.newDto(profileMap.getMap()),
                profileMap.getAlias(),
                profileMap.getUrlPhoto(),
                releaseDate,
                importedDate,
                profileMap.getUrlInfo()
        );
    }

    public ObservableList<ProfileMapDto> newDtos(List<ProfileMap> profileMapList) {
        List<ProfileMapDto> dtoList = profileMapList.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
