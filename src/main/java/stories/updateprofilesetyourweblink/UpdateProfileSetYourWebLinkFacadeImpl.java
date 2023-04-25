package stories.updateprofilesetyourweblink;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.updateprofilesetyourclan.UpdateProfileSetYourClanModelContext;

import java.util.Optional;

public class UpdateProfileSetYourWebLinkFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetYourWebLinkModelContext, EmptyFacadeResult>
        implements UpdateProfileSetYourWebLinkFacade {

    public UpdateProfileSetYourWebLinkFacadeImpl(UpdateProfileSetYourWebLinkModelContext updateProfileSetYourWebLinkModelContext) {
        super(updateProfileSetYourWebLinkModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetYourWebLinkModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating your web link in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setYourWebLink(facadeModelContext.getYourWebLink());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
