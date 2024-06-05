package stories.updateprofilesetnettickrate;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetNetTickrateFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetNetTickrateModelContext, EmptyFacadeResult>
        implements UpdateProfileSetNetTickrateFacade {

    public UpdateProfileSetNetTickrateFacadeImpl(UpdateProfileSetNetTickrateModelContext updateProfileSetNetTickrateModelContext) {
        super(updateProfileSetNetTickrateModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateProfileSetNetTickrateModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetNetTickrateModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating net tickrate in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setNetTickrate(facadeModelContext.getNetTickrate());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
