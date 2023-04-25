package stories.updateprofilesetmaxplayers;

import entities.Difficulty;
import entities.MaxPlayers;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.DifficultyServiceImpl;
import services.MaxPlayersServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.updateprofilesetlength.UpdateProfileSetLengthModelContext;

import java.util.Optional;

public class UpdateProfileSetMaxPlayersFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetMaxPlayersModelContext, EmptyFacadeResult>
        implements UpdateProfileSetMaxPlayersFacade {

    public UpdateProfileSetMaxPlayersFacadeImpl(UpdateProfileSetMaxPlayersModelContext updateProfileSetMaxPlayersModelContext) {
        super(updateProfileSetMaxPlayersModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return false;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetMaxPlayersModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        MaxPlayersServiceImpl maxPlayersService = new MaxPlayersServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating max players in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        Optional<MaxPlayers> maxPlayersOpt = maxPlayersService.findByCode(facadeModelContext.getMaxPlayers());
        if (!maxPlayersOpt.isPresent()) {
            throw new RuntimeException("Error updating max players in Profile. The max players can not be found [max players name: " + facadeModelContext.getMaxPlayers() + "]");
        }

        profile.setMaxPlayers(maxPlayersOpt.get());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
