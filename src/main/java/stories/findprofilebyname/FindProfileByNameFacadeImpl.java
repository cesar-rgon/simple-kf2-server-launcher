package stories.findprofilebyname;

import dtos.ProfileDto;
import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.Optional;

public class FindProfileByNameFacadeImpl
        extends AbstractTransactionalFacade<FindProfileByNameModelContext, FindProfileByNameFacadeResult>
        implements FindProfileByNameFacade {

    public FindProfileByNameFacadeImpl(FindProfileByNameModelContext findProfileByNameModelContext) {
        super(findProfileByNameModelContext, FindProfileByNameFacadeResult.class);
    }


    @Override
    public boolean assertPreconditions() throws Exception {
        return true;
    }

    @Override
    protected FindProfileByNameFacadeResult internalExecute(FindProfileByNameModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);

        Optional<Profile> profileOptional = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOptional.isPresent()) {
            return new FindProfileByNameFacadeResult();
        }
        ProfileDto profileDto = profileDtoFactory.newDto(profileOptional.get());
        return new FindProfileByNameFacadeResult(
                profileDto
        );
    }
}
