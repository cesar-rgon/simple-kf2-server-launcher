package stories.updateprofilesetchatloggingfile;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetChatLoggingFileFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetChatLoggingFileModelContext, EmptyFacadeResult>
        implements UpdateProfileSetChatLoggingFileFacade {

    public UpdateProfileSetChatLoggingFileFacadeImpl(UpdateProfileSetChatLoggingFileModelContext updateProfileSetChatLoggingFileModelContext) {
        super(updateProfileSetChatLoggingFileModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetChatLoggingFileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating chat logging file in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setChatLoggingFile(facadeModelContext.getChatLoggingFile());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
