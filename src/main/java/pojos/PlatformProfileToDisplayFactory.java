package pojos;

import dtos.factories.DifficultyDtoFactory;
import dtos.factories.GameTypeDtoFactory;
import dtos.factories.LengthDtoFactory;
import entities.AbstractPlatform;
import entities.Profile;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlatformProfileToDisplayFactory {

    private static final Logger logger = LogManager.getLogger(PlatformProfileToDisplayFactory.class);

    private final GameTypeDtoFactory gameTypeDtoFactory;
    private final DifficultyDtoFactory difficultyDtoFactory;
    private final LengthDtoFactory lengthDtoFactory;
    private final ProfileService profileService;

    public PlatformProfileToDisplayFactory(EntityManager em) {
        super();
        profileService = new ProfileServiceImpl(em);
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
                map(pp -> {
                    try {
                        Optional<Profile> databaseProfileOptional = profileService.findProfileByCode(pp.getProfile().getCode());
                        return databaseProfileOptional.orElse(null);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                    return null;
                }).
                distinct().
                collect(Collectors.toList());

        List<PlatformProfileToDisplay> result = new ArrayList<PlatformProfileToDisplay>();
        for (Profile profile: profileList) {

            List<AbstractPlatform> platformList = platformProfileList.stream().
                    filter(pp -> {
                        // Profile pProfile = (Profile) Hibernate.unproxy(pp.getProfile());
                        // return pProfile.getName().equals(profile.getName());
                        return pp.getProfile().getName().equals(profile.getName());
                    }).
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

            result.add(newOne(profile,enumPlatform));
        }

        return result;
    }
}
