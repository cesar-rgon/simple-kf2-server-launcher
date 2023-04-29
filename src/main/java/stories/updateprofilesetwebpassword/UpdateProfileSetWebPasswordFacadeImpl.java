package stories.updateprofilesetwebpassword;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;
import utils.Utils;

import java.util.Optional;

public class UpdateProfileSetWebPasswordFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetWebPasswordModelContext, EmptyFacadeResult>
        implements UpdateProfileSetWebPasswordFacade {


    public UpdateProfileSetWebPasswordFacadeImpl(UpdateProfileSetWebPasswordModelContext updateProfileSetWebPasswordModelContext) {
        super(updateProfileSetWebPasswordModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetWebPasswordModelContext updateProfileSetWebPasswordModelContext, EntityManager em) throws Exception {
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

        profile.setWebPassword(Utils.encryptAES(facadeModelContext.getWebPassword()));
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
