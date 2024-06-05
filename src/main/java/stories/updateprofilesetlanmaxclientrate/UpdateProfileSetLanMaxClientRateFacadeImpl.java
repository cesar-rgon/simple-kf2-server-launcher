package stories.updateprofilesetlanmaxclientrate;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetLanMaxClientRateFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetLanMaxClientRateModelContext, EmptyFacadeResult>
        implements UpdateProfileSetLanMaxClientRateFacade {

    public UpdateProfileSetLanMaxClientRateFacadeImpl(UpdateProfileSetLanMaxClientRateModelContext updateProfileSetLanMaxClientRateModelContext) {
        super(updateProfileSetLanMaxClientRateModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateProfileSetLanMaxClientRateModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetLanMaxClientRateModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating lan maximum client rate in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setLanMaxClientRate(facadeModelContext.getLanMaxClientRate());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
