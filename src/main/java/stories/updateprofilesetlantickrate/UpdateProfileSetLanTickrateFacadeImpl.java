package stories.updateprofilesetlantickrate;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetLanTickrateFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetLanTickrateModelContext, EmptyFacadeResult>
        implements UpdateProfileSetLanTickrateFacade {

    public UpdateProfileSetLanTickrateFacadeImpl(UpdateProfileSetLanTickrateModelContext updateProfileSetLanTickrateModelContext) {
        super(updateProfileSetLanTickrateModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateProfileSetLanTickrateModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetLanTickrateModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating lan tickrate in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setLanTickrate(facadeModelContext.getLanTickrate());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
