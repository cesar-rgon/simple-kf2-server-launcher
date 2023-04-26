package stories.updateprofilesetgameport;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetGamePortFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetGamePortModelContext, EmptyFacadeResult>
        implements UpdateProfileSetGamePortFacade {

    public UpdateProfileSetGamePortFacadeImpl(UpdateProfileSetGamePortModelContext updateProfileSetGamePortModelContext) {
        super(updateProfileSetGamePortModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetGamePortModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating game port in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setWebPort(facadeModelContext.getGamePort());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
