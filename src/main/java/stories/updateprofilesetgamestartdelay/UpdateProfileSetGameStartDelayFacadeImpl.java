package stories.updateprofilesetgamestartdelay;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetGameStartDelayFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetGameStartDelayModelContext, EmptyFacadeResult>
        implements UpdateProfileSetGameStartDelayFacade {

    public UpdateProfileSetGameStartDelayFacadeImpl(UpdateProfileSetGameStartDelayModelContext updateProfileSetGameStartDelayModelContext) {
        super(updateProfileSetGameStartDelayModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetGameStartDelayModelContext updateProfileSetGameStartDelayModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetGameStartDelayModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating game start delay in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setGameStartDelay(facadeModelContext.getGameStartDelay());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
