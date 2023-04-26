package stories.updateprofilesettakeover;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetTakeoverFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetTakeoverModelContext, EmptyFacadeResult>
        implements UpdateProfileSetTakeoverFacade {

    public UpdateProfileSetTakeoverFacadeImpl(UpdateProfileSetTakeoverModelContext updateProfileSetTakeoverModelContext) {
        super(updateProfileSetTakeoverModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetTakeoverModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating takeover in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setTakeover(facadeModelContext.isTakeoverSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
