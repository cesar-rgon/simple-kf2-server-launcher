package stories.updateprofilesetwebpassword;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetWebPasswordFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetWebPasswordModelContext, EmptyFacadeResult>
        implements UpdateProfileSetWebPasswordFacade {


    public UpdateProfileSetWebPasswordFacadeImpl(UpdateProfileSetWebPasswordModelContext updateProfileSetWebPasswordModelContext) {
        super(updateProfileSetWebPasswordModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetWebPasswordModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating web password in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setWebPassword(facadeModelContext.getWebPassword());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
