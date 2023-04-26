package stories.updateprofileseturlimageserver;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetUrlImageServerFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetUrlImageServerModelContext, EmptyFacadeResult>
        implements UpdateProfileSetUrlImageServerFacade {

    public UpdateProfileSetUrlImageServerFacadeImpl(UpdateProfileSetUrlImageServerModelContext updateProfileSetUrlImageServerModelContext) {
        super(updateProfileSetUrlImageServerModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetUrlImageServerModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating url of image server in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setUrlImageServer(facadeModelContext.getUrlImageServer());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}

