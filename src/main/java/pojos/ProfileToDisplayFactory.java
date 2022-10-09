package pojos;

import dtos.factories.DifficultyDtoFactory;
import dtos.factories.GameTypeDtoFactory;
import dtos.factories.LengthDtoFactory;
import entities.Profile;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileToDisplayFactory {

    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final DifficultyDtoFactory difficultyDtoFactory;
    private final LengthDtoFactory lengthDtoFactory;

    public ProfileToDisplayFactory() {
        super();
        this.gameTypeDtoFactory = new GameTypeDtoFactory();
        this.difficultyDtoFactory = new DifficultyDtoFactory();
        this.lengthDtoFactory = new LengthDtoFactory();
    }

    public ProfileToDisplay newOne(Profile profile) {
        return new ProfileToDisplay(
                profile.getId(),
                profile.getCode(),
                profile.getGametype() != null ? gameTypeDtoFactory.newDto(profile.getGametype()).getValue(): "",
                profile.getMap() != null ? profile.getMap().getCode(): "",
                profile.getDifficulty() != null ? difficultyDtoFactory.newDto(profile.getDifficulty()).getValue(): "",
                profile.getLength() != null ? lengthDtoFactory.newDto(profile.getLength()).getValue(): ""
        );
    }

    public List<ProfileToDisplay> newOnes(List<Profile> profiles) {
        return profiles.stream().map(this::newOne).collect(Collectors.toList());
    }
}
