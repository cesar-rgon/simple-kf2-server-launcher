package stories.updateprofilesetservername;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetServerNameFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetServerNameModelContext, EmptyFacadeResult>
        implements UpdateProfileSetServerNameFacade {

    public UpdateProfileSetServerNameFacadeImpl(UpdateProfileSetServerNameModelContext updateProfileSetServerNameModelContext) {
        super(updateProfileSetServerNameModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return false;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetServerNameModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating server name in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setServerName(facadeModelContext.getServerName());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
