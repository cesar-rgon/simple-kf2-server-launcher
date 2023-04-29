package stories.updateprofilesetreadyupdelay;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetReadyUpDelayFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetReadyUpDelayModelContext, EmptyFacadeResult>
        implements UpdateProfileSetReadyUpDelayFacade {

    public UpdateProfileSetReadyUpDelayFacadeImpl(UpdateProfileSetReadyUpDelayModelContext updateProfileSetReadyUpDelayModelContext) {
        super(updateProfileSetReadyUpDelayModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetReadyUpDelayModelContext updateProfileSetReadyUpDelayModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetReadyUpDelayModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating ready up delay in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setReadyUpDelay(facadeModelContext.getReadyUpDelay());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
