package stories.updateprofilesetserverpassword;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetServerPasswordFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetServerPasswordModelContext, EmptyFacadeResult>
        implements UpdateProfileSetServerPasswordFacade {

    public UpdateProfileSetServerPasswordFacadeImpl(UpdateProfileSetServerPasswordModelContext updateProfileSetServerPasswordModelContext) {
        super(updateProfileSetServerPasswordModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetServerPasswordModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating server password in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setServerPassword(facadeModelContext.getServerPassword());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
