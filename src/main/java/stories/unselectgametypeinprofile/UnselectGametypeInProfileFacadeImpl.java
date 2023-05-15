package stories.unselectgametypeinprofile;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UnselectGametypeInProfileFacadeImpl
        extends AbstractTransactionalFacade<UnselectGametypeInProfileModelContext, EmptyFacadeResult>
        implements UnselectGametypeInProfileFacade {

    public UnselectGametypeInProfileFacadeImpl(UnselectGametypeInProfileModelContext unselectGametypeInProfileModelContext) {
        super(unselectGametypeInProfileModelContext, EmptyFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(UnselectGametypeInProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UnselectGametypeInProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> actualProfileOptional = profileService.findProfileByCode(facadeModelContext.getActualProfileName());
        if (!actualProfileOptional.isPresent()) {
            return new EmptyFacadeResult();
        }

        if (actualProfileOptional.get().getGametype() != null &&
                facadeModelContext.getSelectedGametypeCode().equals(actualProfileOptional.get().getGametype().getCode())) {

            actualProfileOptional.get().setGametype(null);
            profileService.updateItem(actualProfileOptional.get());
        }

        return new EmptyFacadeResult();
    }
}
