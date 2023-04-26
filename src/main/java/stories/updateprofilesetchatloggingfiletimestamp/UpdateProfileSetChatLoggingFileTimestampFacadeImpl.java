package stories.updateprofilesetchatloggingfiletimestamp;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetChatLoggingFileTimestampFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetChatLoggingFileTimestampModelContext, EmptyFacadeResult>
        implements UpdateProfileSetChatLoggingFileTimestampFacade {

    public UpdateProfileSetChatLoggingFileTimestampFacadeImpl(UpdateProfileSetChatLoggingFileTimestampModelContext updateProfileSetChatLoggingFileTimestampModelContext) {
        super(updateProfileSetChatLoggingFileTimestampModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetChatLoggingFileTimestampModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating chat logging file timestamp in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setChatLoggingFileTimestamp(facadeModelContext.isChatLoggingFileTimestampSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
