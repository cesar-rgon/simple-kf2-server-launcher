package stories.updateprofilesetkickpercentage;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetKickPercentageFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetKickPercentageModelContext, EmptyFacadeResult>
        implements UpdateProfileSetKickPercentageFacade {

    public UpdateProfileSetKickPercentageFacadeImpl(UpdateProfileSetKickPercentageModelContext updateProfileSetKickPercentageModelContext) {
        super(updateProfileSetKickPercentageModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetKickPercentageModelContext updateProfileSetKickPercentageModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetKickPercentageModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating kick percentage in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setKickPercentage(facadeModelContext.getKickPercentage());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
