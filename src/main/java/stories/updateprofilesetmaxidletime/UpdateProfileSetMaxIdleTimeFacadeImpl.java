package stories.updateprofilesetmaxidletime;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetMaxIdleTimeFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetMaxIdleTimeModelContext, EmptyFacadeResult>
        implements UpdateProfileSetMaxIdleTimeFacade {

    public UpdateProfileSetMaxIdleTimeFacadeImpl(UpdateProfileSetMaxIdleTimeModelContext updateProfileSetMaxIdleTimeModelContext) {
        super(updateProfileSetMaxIdleTimeModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetMaxIdleTimeModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating max idle time in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setMaxIdleTime(facadeModelContext.getMaxIdleTime());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
