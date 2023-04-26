package stories.updateprofilesetlength;

import entities.Difficulty;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.DifficultyServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetLengthFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetLengthModelContext, EmptyFacadeResult>
        implements UpdateProfileSetLengthFacade {

    public UpdateProfileSetLengthFacadeImpl(UpdateProfileSetLengthModelContext updateProfileSetLengthModelContext) {
        super(updateProfileSetLengthModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetLengthModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        DifficultyServiceImpl difficultyService = new DifficultyServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating difficulty in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        Optional<Difficulty> difficultyOpt = difficultyService.findByCode(facadeModelContext.getLengthCode());
        if (!difficultyOpt.isPresent()) {
            throw new RuntimeException("Error updating difficulty in Profile. The difficulty can not be found [difficulty name: " + facadeModelContext.getLengthCode() + "]");
        }

        profile.setDifficulty(difficultyOpt.get());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
