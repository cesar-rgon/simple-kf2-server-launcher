package stories.updateprofilesetvoip;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;
import stories.updateprofilesetwebport.UpdateProfileSetWebPortModelContext;

import java.util.Optional;

public class UpdateProfileSetVoipFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetVoipModelContext, EmptyFacadeResult>
        implements UpdateProfileSetVoipFacade {

    public UpdateProfileSetVoipFacadeImpl(UpdateProfileSetVoipModelContext updateProfileSetVoipModelContext) {
        super(updateProfileSetVoipModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetVoipModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating voip in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setVoip(facadeModelContext.isVoipSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
