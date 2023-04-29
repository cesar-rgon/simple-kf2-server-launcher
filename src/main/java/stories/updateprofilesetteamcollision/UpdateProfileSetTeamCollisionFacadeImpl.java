package stories.updateprofilesetteamcollision;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetTeamCollisionFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetTeamCollisionModelContext, EmptyFacadeResult>
        implements UpdateProfileSetTeamCollisionFacade {

    public UpdateProfileSetTeamCollisionFacadeImpl(UpdateProfileSetTeamCollisionModelContext updateProfileSetTeamCollisionModelContext) {
        super(updateProfileSetTeamCollisionModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetTeamCollisionModelContext updateProfileSetTeamCollisionModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetTeamCollisionModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating team collision in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setTeamCollision(facadeModelContext.isTeamCollisionSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
