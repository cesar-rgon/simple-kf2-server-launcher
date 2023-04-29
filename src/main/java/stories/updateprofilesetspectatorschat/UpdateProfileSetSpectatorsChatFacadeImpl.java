package stories.updateprofilesetspectatorschat;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetSpectatorsChatFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetSpectatorsChatModelContext, EmptyFacadeResult>
        implements UpdateProfileSetSpectatorsChatFacade {

    public UpdateProfileSetSpectatorsChatFacadeImpl(UpdateProfileSetSpectatorsChatModelContext updateProfileSetSpectatorsChatModelContext) {
        super(updateProfileSetSpectatorsChatModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetSpectatorsChatModelContext updateProfileSetSpectatorsChatModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetSpectatorsChatModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating spectators chat in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setSpectatorsOnlyChatToOtherSpectators(facadeModelContext.isSpectatorsChatSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
