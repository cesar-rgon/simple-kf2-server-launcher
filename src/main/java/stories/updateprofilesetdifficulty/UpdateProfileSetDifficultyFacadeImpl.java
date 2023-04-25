package stories.updateprofilesetdifficulty;

import entities.Difficulty;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.DifficultyServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetDifficultyFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetDifficultyModelContext, EmptyFacadeResult>
        implements UpdateProfileSetDifficultyFacade {

    public UpdateProfileSetDifficultyFacadeImpl(UpdateProfileSetDifficultyModelContext updateProfileSetDifficultyModelContext) {
        super(updateProfileSetDifficultyModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }


    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetDifficultyModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        DifficultyServiceImpl difficultyService = new DifficultyServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating difficulty in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        Optional<Difficulty> difficultyOpt = difficultyService.findByCode(facadeModelContext.getDifficultyCode());
        if (!difficultyOpt.isPresent()) {
            throw new RuntimeException("Error updating difficulty in Profile. The difficulty can not be found [difficulty name: " + facadeModelContext.getDifficultyCode() + "]");
        }

        profile.setDifficulty(difficultyOpt.get());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
