package stories.unselectlengthinprofile;

import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class UnselectLengthInProfileFacadeImpl
        extends AbstractTransactionalFacade<UnselectLengthInProfileModelContext, EmptyFacadeResult>
        implements UnselectLengthInProfileFacade {

    public UnselectLengthInProfileFacadeImpl(UnselectLengthInProfileModelContext unselectLengthInProfileModelContext) {
        super(unselectLengthInProfileModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UnselectLengthInProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UnselectLengthInProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<Profile> actualProfileOptional = profileService.findProfileByCode(facadeModelContext.getActualProfileName());
        if (!actualProfileOptional.isPresent()) {
            return new EmptyFacadeResult();
        }

        if (actualProfileOptional.get().getLength() != null &&
                facadeModelContext.getSelectedLengthCode().equals(actualProfileOptional.get().getLength().getCode())) {

            actualProfileOptional.get().setLength(null);
            profileService.updateItem(actualProfileOptional.get());
        }

        return new EmptyFacadeResult();
    }
}
