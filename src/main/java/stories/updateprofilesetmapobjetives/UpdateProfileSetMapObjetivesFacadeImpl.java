package stories.updateprofilesetmapobjetives;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetMapObjetivesFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetMapObjetivesModelContext, EmptyFacadeResult>
        implements UpdateProfileSetMapObjetivesFacade {

    public UpdateProfileSetMapObjetivesFacadeImpl(UpdateProfileSetMapObjetivesModelContext updateProfileSetMapObjetivesModelContext) {
        super(updateProfileSetMapObjetivesModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetMapObjetivesModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating map objetives in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setMapObjetives(facadeModelContext.isMapObjetivesSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
