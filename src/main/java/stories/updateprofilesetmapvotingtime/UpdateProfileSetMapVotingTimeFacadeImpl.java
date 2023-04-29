package stories.updateprofilesetmapvotingtime;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetMapVotingTimeFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetMapVotingTimeModelContext, EmptyFacadeResult>
        implements UpdateProfileSetMapVotingTimeFacade {

    public UpdateProfileSetMapVotingTimeFacadeImpl(UpdateProfileSetMapVotingTimeModelContext updateProfileSetMapVotingTimeModelContext) {
        super(updateProfileSetMapVotingTimeModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetMapVotingTimeModelContext updateProfileSetMapVotingTimeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetMapVotingTimeModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating map voting time in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setMapVotingTime(facadeModelContext.getMapVotingTime());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
