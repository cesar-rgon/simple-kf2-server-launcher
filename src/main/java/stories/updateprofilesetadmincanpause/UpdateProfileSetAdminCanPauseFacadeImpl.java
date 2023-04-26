package stories.updateprofilesetadmincanpause;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetAdminCanPauseFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetAdminCanPauseModelContext, EmptyFacadeResult>
        implements UpdateProfileSetAdminCanPauseFacade {

    public UpdateProfileSetAdminCanPauseFacadeImpl(UpdateProfileSetAdminCanPauseModelContext updateProfileSetAdminCanPauseModelContext) {
        super(updateProfileSetAdminCanPauseModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetAdminCanPauseModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating admin can pause in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setAdminCanPause(facadeModelContext.isAdminCanPauseSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
