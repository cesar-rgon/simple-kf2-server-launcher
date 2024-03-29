package stories.updateprofilesetlength;

import entities.Difficulty;
import entities.Length;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.DifficultyServiceImpl;
import services.LengthServiceImpl;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UpdateProfileSetLengthFacadeImpl
        extends AbstractTransactionalFacade<UpdateProfileSetLengthModelContext, EmptyFacadeResult>
        implements UpdateProfileSetLengthFacade {

    public UpdateProfileSetLengthFacadeImpl(UpdateProfileSetLengthModelContext updateProfileSetLengthModelContext) {
        super(updateProfileSetLengthModelContext, EmptyFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions(UpdateProfileSetLengthModelContext updateProfileSetLengthModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateProfileSetLengthModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        LengthServiceImpl lengthServiceImpl = new LengthServiceImpl(em);

        Optional<Profile> profileOpt = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOpt.isPresent()) {
            throw new RuntimeException("Error updating difficulty in Profile. The profile can not be found [profile name: " + facadeModelContext.getProfileName() + "]");
        }
        Profile profile = profileOpt.get();

        Optional<Length> lengthOpt = lengthServiceImpl.findByCode(facadeModelContext.getLengthCode());
        if (!lengthOpt.isPresent()) {
            throw new RuntimeException("Error updating difficulty in Profile. The length can not be found [length name: " + facadeModelContext.getLengthCode() + "]");
        }

        profile.setLength(lengthOpt.get());
        profileService.updateItem(profile);
        return new EmptyFacadeResult();
    }
}
