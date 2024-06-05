package stories.updateprofilesetinternetmaxclientrate;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetInternetMaxClientRateFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetInternetMaxClientRateModelContext, EmptyFacadeResult>
        implements UpdateProfileSetInternetMaxClientRateFacade {

    public UpdateProfileSetInternetMaxClientRateFacadeImpl(UpdateProfileSetInternetMaxClientRateModelContext updateProfileSetInternetMaxClientRateModelContext) {
        super(updateProfileSetInternetMaxClientRateModelContext, EmptyFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(UpdateProfileSetInternetMaxClientRateModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetInternetMaxClientRateModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating internet maximum client rate in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setInternetMaxClientRate(facadeModelContext.getInternetMaxClientRate());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
