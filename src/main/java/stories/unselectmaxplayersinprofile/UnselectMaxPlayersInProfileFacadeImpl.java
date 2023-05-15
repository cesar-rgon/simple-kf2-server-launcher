package stories.unselectmaxplayersinprofile;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UnselectMaxPlayersInProfileFacadeImpl
        extends AbstractTransactionalFacade<UnselectMaxPlayersInProfileModelContext, EmptyFacadeResult>
        implements UnselectMaxPlayersInProfileFacade {

    public UnselectMaxPlayersInProfileFacadeImpl(UnselectMaxPlayersInProfileModelContext unselectMaxPlayersInProfileModelContext) {
        super(unselectMaxPlayersInProfileModelContext, EmptyFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(UnselectMaxPlayersInProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UnselectMaxPlayersInProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> actualProfileOptional = profileService.findProfileByCode(facadeModelContext.getActualProfileName());
        if (!actualProfileOptional.isPresent()) {
            return new EmptyFacadeResult();
        }

        if (actualProfileOptional.get().getMaxPlayers() != null &&
                facadeModelContext.getSelectedMaxPlayersCode().equals(actualProfileOptional.get().getMaxPlayers().getCode())) {

            actualProfileOptional.get().setMaxPlayers(null);
            profileService.updateItem(actualProfileOptional.get());
        }

        return new EmptyFacadeResult();
    }
}
