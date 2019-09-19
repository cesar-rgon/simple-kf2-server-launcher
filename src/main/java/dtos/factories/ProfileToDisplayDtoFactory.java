package dtos.factories;

import dtos.ProfileToDisplayDto;
import entities.Profile;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileToDisplayDtoFactory {

    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final DifficultyDtoFactory difficultyDtoFactory;
    private final LengthDtoFactory lengthDtoFactory;

    public ProfileToDisplayDtoFactory() {
        super();
        this.gameTypeDtoFactory = new GameTypeDtoFactory();
        this.difficultyDtoFactory = new DifficultyDtoFactory();
        this.lengthDtoFactory = new LengthDtoFactory();
    }

    public ProfileToDisplayDto newDto(Profile profile) {
        return new ProfileToDisplayDto(
                profile.getId(),
                profile.getName(),
                profile.getGametype() != null ? gameTypeDtoFactory.newDto(profile.getGametype()).getValue(): "",
                profile.getMap() != null ? profile.getMap().getCode(): "",
                profile.getDifficulty() != null ? difficultyDtoFactory.newDto(profile.getDifficulty()).getValue(): "",
                profile.getLength() != null ? lengthDtoFactory.newDto(profile.getLength()).getValue(): ""
        );
    }

    public List<ProfileToDisplayDto> newDtos(List<Profile> profiles) {
        return profiles.stream().map(this::newDto).collect(Collectors.toList());
    }
}
