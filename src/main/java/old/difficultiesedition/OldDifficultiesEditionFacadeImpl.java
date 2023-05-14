package stories.console.difficultiesedition;

import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Difficulty;
import entities.Profile;
import services.DifficultyServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import old.OldAEditionFacade;

import java.util.Optional;

public class OldDifficultiesEditionFacadeImpl extends OldAEditionFacade<Difficulty, SelectDto> implements stories.console.difficultiesedition.OldDifficultiesEditionFacade {

    private final ProfileService profileService;

    public OldDifficultiesEditionFacadeImpl() {
        super(
                Difficulty.class,
                new DifficultyDtoFactory(),
                new DifficultyServiceImpl(null)
        );
        this.profileService = new ProfileServiceImpl(em);
    }


    @Override
    public ProfileDto unselectDifficultyInProfile(String profileName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setDifficulty(null);
            profileService.updateItem(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
