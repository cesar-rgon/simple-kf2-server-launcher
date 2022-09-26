package pojos;

import dtos.factories.DifficultyDtoFactory;
import dtos.factories.GameTypeDtoFactory;
import dtos.factories.LengthDtoFactory;
import entities.AbstractPlatform;
import entities.Profile;
import pojos.enums.EnumPlatform;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public PlatformProfileToDisplay newOne(Profile profile, EnumPlatform enumPlatform) {
        return new PlatformProfileToDisplay(
                enumPlatform,
                profile.getId(),
                profile.getCode(),
                profile.getGametype() != null ? gameTypeDtoFactory.newDto(profile.getGametype()).getValue(): "",
                profile.getMap() != null ? profile.getMap().getCode(): "",
                profile.getDifficulty() != null ? difficultyDtoFactory.newDto(profile.getDifficulty()).getValue(): "",
                profile.getLength() != null ? lengthDtoFactory.newDto(profile.getLength()).getValue(): ""
        );
    }

    public List<PlatformProfileToDisplay> newOnes(List<PlatformProfile> platformProfileList) {

        List<Profile> profileList = platformProfileList.stream().
                map(PlatformProfile::getProfile).
                distinct().
                collect(Collectors.toList());

        List<PlatformProfileToDisplay> result = new ArrayList<PlatformProfileToDisplay>();
        for (Profile profile: profileList) {

            List<AbstractPlatform> platformList = platformProfileList.stream().
                    filter(pp -> pp.getProfile().equals(profile)).
                    map(PlatformProfile::getPlatform).
                    collect(Collectors.toList());
            if (platformList == null || platformList.isEmpty()) {
                continue;
            }

            EnumPlatform enumPlatform = null;
            if (platformList.size() == EnumPlatform.listAll().size()) {
                enumPlatform = EnumPlatform.ALL;
            } else {
                enumPlatform = EnumPlatform.getByName(platformList.get(0).getCode());
            }
            if (enumPlatform == null) {
                continue;
            }

            result.add(newOne(profile, enumPlatform));
        }

        return result;
    }
}
