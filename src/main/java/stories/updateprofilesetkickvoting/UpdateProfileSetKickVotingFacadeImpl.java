package stories.updateprofilesetkickvoting;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetKickVotingFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetKickVotingModelContext, EmptyFacadeResult>
        implements UpdateProfileSetKickVotingFacade {

    public UpdateProfileSetKickVotingFacadeImpl(UpdateProfileSetKickVotingModelContext updateProfileSetKickVotingModelContext) {
        super(updateProfileSetKickVotingModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetKickVotingModelContext updateProfileSetKickVotingModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetKickVotingModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating kick voting in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setKickVoting(facadeModelContext.isKickVotingSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
