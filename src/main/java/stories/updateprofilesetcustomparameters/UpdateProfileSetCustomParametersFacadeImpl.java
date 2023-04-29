package stories.updateprofilesetcustomparameters;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.updateprofilesetdifficulty.UpdateProfileSetDifficultyModelContext;

import java.util.Optional;

public class UpdateProfileSetCustomParametersFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetCustomParametersModelContext, EmptyFacadeResult>
        implements UpdateProfileSetCustomParametersFacade {

    public UpdateProfileSetCustomParametersFacadeImpl(UpdateProfileSetCustomParametersModelContext updateProfileSetCustomParametersModelContext) {
        super(updateProfileSetCustomParametersModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetCustomParametersModelContext updateProfileSetCustomParametersModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetCustomParametersModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating custom parameters name in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setCustomParameters(facadeModelContext.getCustomParameters());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
