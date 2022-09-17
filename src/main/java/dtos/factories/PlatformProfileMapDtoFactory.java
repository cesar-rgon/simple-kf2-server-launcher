package dtos.factories;

import dtos.PlatformDto;
import dtos.PlatformProfileMapDto;
import entities.PlatformProfileMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pojos.enums.EnumPlatform;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class PlatformProfileMapDtoFactory {

    private final PlatformDtoFactory platformDtoFactory;
    private final ProfileDtoFactory profileDtoFactory;
    private final MapDtoFactory mapDtoFactory;

    public PlatformProfileMapDtoFactory() {
        super();
        this.platformDtoFactory = new PlatformDtoFactory();
        this.profileDtoFactory = new ProfileDtoFactory();
        this.mapDtoFactory = new MapDtoFactory();
    }

    public PlatformProfileMapDto newDto(PlatformProfileMap platformProfileMap) {

        LocalDate releaseDate = platformProfileMap.getReleaseDate() != null ? platformProfileMap.getReleaseDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(): null;
        LocalDateTime importedDate = platformProfileMap.getImportedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        PlatformDto platformDto = EnumPlatform.STEAM.name().equals(platformProfileMap.getPlatform().getCode()) ?
                platformDtoFactory.newSteamDto(platformProfileMap.getPlatform()): platformDtoFactory.newEpicDto(platformProfileMap.getPlatform());

        return new PlatformProfileMapDto(
                platformDto,
                profileDtoFactory.newDto(platformProfileMap.getProfile()),
                mapDtoFactory.newDto(platformProfileMap.getMap()),
                platformProfileMap.getAlias(),
                platformProfileMap.getUrlPhoto(),
                releaseDate,
                importedDate,
                platformProfileMap.getUrlInfo()
        );
    }

    public ObservableList<PlatformProfileMapDto> newDtos(List<PlatformProfileMap> platformProfileMapList) {
        List<PlatformProfileMapDto> dtoList = platformProfileMapList.stream().map(this::newDto).collect(Collectors.toList());
        return FXCollections.observableArrayList(dtoList);
    }

}
