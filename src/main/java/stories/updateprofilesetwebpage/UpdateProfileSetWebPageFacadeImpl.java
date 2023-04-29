package stories.updateprofilesetwebpage;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.updateprofileseturlimageserver.UpdateProfileSetUrlImageServerModelContext;

import java.util.Optional;

public class UpdateProfileSetWebPageFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetWebPageModelContext, EmptyFacadeResult>
        implements UpdateProfileSetWebPageFacade {

    public UpdateProfileSetWebPageFacadeImpl(UpdateProfileSetWebPageModelContext updateProfileSetWebPageModelContext) {
        super(updateProfileSetWebPageModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetWebPageModelContext updateProfileSetWebPageModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetWebPageModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating webpage in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setWebPage(facadeModelContext.getWebPage());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
