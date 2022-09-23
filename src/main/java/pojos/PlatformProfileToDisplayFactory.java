package pojos;

import dtos.factories.DifficultyDtoFactory;
import dtos.factories.GameTypeDtoFactory;
import dtos.factories.LengthDtoFactory;
import entities.Profile;

import java.util.List;
import java.util.stream.Collectors;

public class PlatformProfileToDisplayFactory {

    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final DifficultyDtoFactory difficultyDtoFactory;
    private final LengthDtoFactory lengthDtoFactory;

    public PlatformProfileToDisplayFactory() {
        super();
        this.gameTypeDtoFactory = new GameTypeDtoFactory();
        this.difficultyDtoFactory = new DifficultyDtoFactory();
        this.lengthDtoFactory = new LengthDtoFactory();
    }

    public PlatformProfileToDisplay newOne(Profile profile) {
        return new PlatformProfileToDisplay(
                profile.getId(),
                profile.getCode(),
                profile.getGametype() != null ? gameTypeDtoFactory.newDto(profile.getGametype()).getValue(): "",
                profile.getMap() != null ? profile.getMap().getCode(): "",
                profile.getDifficulty() != null ? difficultyDtoFactory.newDto(profile.getDifficulty()).getValue(): "",
                profile.getLength() != null ? lengthDtoFactory.newDto(profile.getLength()).getValue(): ""
        );
    }

    public List<PlatformProfileToDisplay> newOnes(List<Profile> profileList) {
        return profileList.stream().map(this::newOne).collect(Collectors.toList());
    }
}
