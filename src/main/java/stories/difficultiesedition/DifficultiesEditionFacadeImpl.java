package stories.difficultiesedition;

import daos.DifficultyDao;
import daos.ProfileDao;
import dtos.ProfileDto;
import dtos.SelectDto;
import dtos.factories.DifficultyDtoFactory;
import dtos.factories.ProfileDtoFactory;
import entities.Difficulty;
import entities.Profile;
import services.DifficultyServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.AbstractEditionFacade;

import java.sql.SQLException;
import java.util.Optional;

public class DifficultiesEditionFacadeImpl extends AbstractEditionFacade<Difficulty, SelectDto> implements DifficultiesEditionFacade {

    private final ProfileService profileService;

    public DifficultiesEditionFacadeImpl() {
        super(
                Difficulty.class,
                new DifficultyDtoFactory(),
                new DifficultyServiceImpl()
        );
        this.profileService = new ProfileServiceImpl();
    }


    @Override
    public ProfileDto unselectDifficultyInProfile(String profileName) throws Exception {
        Optional<Profile> profileOpt = profileService.findProfileByCode(profileName);
        if (profileOpt.isPresent()) {
            profileOpt.get().setDifficulty(null);
            profileService.updateItem(profileOpt.get());
            ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory();
            return profileDtoFactory.newDto(profileOpt.get());
        }
        return null;
    }
}
