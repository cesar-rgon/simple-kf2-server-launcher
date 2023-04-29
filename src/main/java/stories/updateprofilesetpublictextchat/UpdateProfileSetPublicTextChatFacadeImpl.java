package stories.updateprofilesetpublictextchat;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetPublicTextChatFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetPublicTextChatModelContext, EmptyFacadeResult>
        implements UpdateProfileSetPublicTextChatFacade {

    public UpdateProfileSetPublicTextChatFacadeImpl(UpdateProfileSetPublicTextChatModelContext updateProfileSetPublicTextChatModelContext) {
        super(updateProfileSetPublicTextChatModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetPublicTextChatModelContext updateProfileSetPublicTextChatModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetPublicTextChatModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating public text chat in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setPublicTextChat(facadeModelContext.isPublicTextChatSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
