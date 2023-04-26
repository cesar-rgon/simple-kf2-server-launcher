package stories.updateprofilesetmaxspectators;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetMaxSpectatorsFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetMaxSpectatorsModelContext, EmptyFacadeResult>
        implements UpdateProfileSetMaxSpectatorsFacade {

    public UpdateProfileSetMaxSpectatorsFacadeImpl(UpdateProfileSetMaxSpectatorsModelContext updateProfileSetMaxSpectatorsModelContext) {
        super(updateProfileSetMaxSpectatorsModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetMaxSpectatorsModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating max spectators in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setMaxSpectators(facadeModelContext.getMaxSpectators());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
