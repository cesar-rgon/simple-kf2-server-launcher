package stories.updateprofilesetannounceadminlogin;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetAnnounceAdminLoginFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetAnnounceAdminLoginModelContext, EmptyFacadeResult>
        implements UpdateProfileSetAnnounceAdminLoginFacade {

    public UpdateProfileSetAnnounceAdminLoginFacadeImpl(UpdateProfileSetAnnounceAdminLoginModelContext updateProfileSetAnnounceAdminLoginModelContext) {
        super(updateProfileSetAnnounceAdminLoginModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetAnnounceAdminLoginModelContext updateProfileSetAnnounceAdminLoginModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetAnnounceAdminLoginModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating announce admin login in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setAnnounceAdminLogin(facadeModelContext.isAnnounceAdminLoginSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
