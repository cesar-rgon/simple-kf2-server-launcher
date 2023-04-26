package stories.updateprofilesettimebetweenkicks;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetTimeBetweenKicksFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetTimeBetweenKicksModelContext, EmptyFacadeResult>
        implements UpdateProfileSetTimeBetweenKicksFacade {

    public UpdateProfileSetTimeBetweenKicksFacadeImpl(UpdateProfileSetTimeBetweenKicksModelContext updateProfileSetTimeBetweenKicksModelContext) {
        super(updateProfileSetTimeBetweenKicksModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetTimeBetweenKicksModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating time between kicks in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setTimeBetweenKicks(facadeModelContext.getTimeBetweenKicks());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
