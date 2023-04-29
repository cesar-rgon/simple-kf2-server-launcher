package stories.updateprofilesetwebport;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetWebPortFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetWebPortModelContext, EmptyFacadeResult>
        implements UpdateProfileSetWebPortFacade {

    public UpdateProfileSetWebPortFacadeImpl(UpdateProfileSetWebPortModelContext updateProfileSetWebPortModelContext) {
        super(updateProfileSetWebPortModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetWebPortModelContext updateProfileSetWebPortModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetWebPortModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating web port in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setWebPort(facadeModelContext.getWebPort());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
