package stories.unselectdifficultyinprofile;

import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import pojos.session.Session;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UnselectDifficultyInProfileFacadeImpl
        extends AbstractTransactionalFacade<UnselectDifficultyInProfileModelContext, EmptyFacadeResult>
        implements UnselectDifficultyInProfileFacade {

    public UnselectDifficultyInProfileFacadeImpl(UnselectDifficultyInProfileModelContext unselectDifficultyInProfileModelContext) {
        super(unselectDifficultyInProfileModelContext, EmptyFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(UnselectDifficultyInProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UnselectDifficultyInProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> actualProfileOptional = profileService.findProfileByCode(facadeModelContext.getActualProfileName());
        if (!actualProfileOptional.isPresent()) {
            return new EmptyFacadeResult();
        }

        if (actualProfileOptional.get().getDifficulty() != null &&
                facadeModelContext.getSelectedDifficultyCode().equals(actualProfileOptional.get().getDifficulty().getCode())) {

            actualProfileOptional.get().setDifficulty(null);
            profileService.updateItem(actualProfileOptional.get());
        }

        return new EmptyFacadeResult();
    }
}
