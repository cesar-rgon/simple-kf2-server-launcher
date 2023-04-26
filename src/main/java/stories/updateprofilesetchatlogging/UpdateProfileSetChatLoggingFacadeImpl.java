package stories.updateprofilesetchatlogging;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetChatLoggingFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetChatLoggingModelContext, EmptyFacadeResult>
        implements UpdateProfileSetChatLoggingFacade {

    public UpdateProfileSetChatLoggingFacadeImpl(UpdateProfileSetChatLoggingModelContext updateProfileSetChatLoggingModelContext) {
        super(updateProfileSetChatLoggingModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetChatLoggingModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating chat logging in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setChatLogging(facadeModelContext.isChatLoggingSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
