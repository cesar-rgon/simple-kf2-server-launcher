package stories.updateprofilesetqueryport;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetQueryPortFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetQueryPortModelContext, EmptyFacadeResult>
        implements UpdateProfileSetQueryPortFacade {

    public UpdateProfileSetQueryPortFacadeImpl(UpdateProfileSetQueryPortModelContext UpdateProfileSetQueryPortModelContext) {
        super(UpdateProfileSetQueryPortModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetQueryPortModelContext UpdateProfileSetQueryPortModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetQueryPortModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating query port in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setQueryPort(facadeModelContext.getQueryPort());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
