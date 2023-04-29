package stories.updateprofilesetfriendlyfirepercentage;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetFriendlyFirePercentageFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetFriendlyFirePercentageModelContext, EmptyFacadeResult>
        implements UpdateProfileSetFriendlyFirePercentageFacade {

    public UpdateProfileSetFriendlyFirePercentageFacadeImpl(UpdateProfileSetFriendlyFirePercentageModelContext updateProfileSetFriendlyFirePercentageModelContext) {
        super(updateProfileSetFriendlyFirePercentageModelContext, EmptyFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(UpdateProfileSetFriendlyFirePercentageModelContext updateProfileSetFriendlyFirePercentageModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetFriendlyFirePercentageModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating friendly fire percentage in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        profile.setFriendlyFirePercentage(facadeModelContext.getFriendlyFirePercentage());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
