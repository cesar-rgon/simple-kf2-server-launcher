package stories.updateprofilesetpickupitems;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetPickupItemsFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetPickupItemsModelContext, EmptyFacadeResult>
        implements UpdateProfileSetPickupItemsFacade {

    public UpdateProfileSetPickupItemsFacadeImpl(UpdateProfileSetPickupItemsModelContext updateProfileSetPickupItemsModelContext) {
        super(updateProfileSetPickupItemsModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetPickupItemsModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating pickup items in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setPickupItems(facadeModelContext.isPickupItemsSelected());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
