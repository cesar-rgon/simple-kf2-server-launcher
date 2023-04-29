package stories.updateprofilesetyourclan;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetYourClanFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetYourClanModelContext, EmptyFacadeResult>
        implements UpdateProfileSetYourClanFacade {

    public UpdateProfileSetYourClanFacadeImpl(UpdateProfileSetYourClanModelContext updateProfileSetYourClanModelContext) {
        super(updateProfileSetYourClanModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetYourClanModelContext updateProfileSetYourClanModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetYourClanModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating welcome message in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setYourClan(facadeModelContext.getYourClan());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
