package stories.updateprofilesetgametype;

import entities.GameType;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.GameTypeServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetGameTypeFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetGameTypeModelContext, EmptyFacadeResult>
        implements UpdateProfileSetGameTypeFacade {

    public UpdateProfileSetGameTypeFacadeImpl(UpdateProfileSetGameTypeModelContext updateProfileSetGameTypeModelContext) {
        super(updateProfileSetGameTypeModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetGameTypeModelContext updateProfileSetGameTypeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetGameTypeModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        GameTypeServiceImpl gameTypeService = new GameTypeServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating game type in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        Optional<GameType> gameTypeOpt = gameTypeService.findByCode(facadeModelContext.getGameTypeCode());
        if (!gameTypeOpt.isPresent()) {
            throw new RuntimeException("Error updating game type in Profile. The game type can not be found [game type name: " + facadeModelContext.getGameTypeCode() + "]");
        }

        profile.setGametype(gameTypeOpt.get());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }

}
