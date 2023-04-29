package stories.updateprofilesetmapvoting;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.updateprofilesetserverpassword.UpdateProfileSetServerPasswordModelContext;

import java.util.Optional;

public class UpdateProfileSetMapVotingFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetMapVotingModelContext, EmptyFacadeResult>
        implements UpdateProfileSetMapVotingFacade {

    public UpdateProfileSetMapVotingFacadeImpl(UpdateProfileSetMapVotingModelContext UpdateProfileSetMapVotingModelContext) {
        super(UpdateProfileSetMapVotingModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetMapVotingModelContext UpdateProfileSetMapVotingModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetMapVotingModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating map voting in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setMapVoting(facadeModelContext.isMapVotingSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
