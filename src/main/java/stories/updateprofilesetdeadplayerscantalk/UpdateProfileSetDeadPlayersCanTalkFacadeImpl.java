package stories.updateprofilesetdeadplayerscantalk;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetDeadPlayersCanTalkFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetDeadPlayersCanTalkModelContext, EmptyFacadeResult>
        implements UpdateProfileSetDeadPlayersCanTalkFacade {

    public UpdateProfileSetDeadPlayersCanTalkFacadeImpl(UpdateProfileSetDeadPlayersCanTalkModelContext updateProfileSetDeadPlayersCanTalkModelContext) {
        super(updateProfileSetDeadPlayersCanTalkModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetDeadPlayersCanTalkModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating dead players can talk in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setDeadPlayersCanTalk(facadeModelContext.isDeadPlayersCanTalkSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
