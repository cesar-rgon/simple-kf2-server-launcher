package stories.updateprofilesetwelcomemessage;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetWelcomeMessageFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetWelcomeMessageModelContext, EmptyFacadeResult>
        implements UpdateProfileSetWelcomeMessageFacade {

    public UpdateProfileSetWelcomeMessageFacadeImpl(UpdateProfileSetWelcomeMessageModelContext updateProfileSetWelcomeMessageModelContext) {
        super(updateProfileSetWelcomeMessageModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetWelcomeMessageModelContext updateProfileSetWelcomeMessageModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetWelcomeMessageModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating welcome message in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setWelcomeMessage(facadeModelContext.getWelcomeMessage());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
