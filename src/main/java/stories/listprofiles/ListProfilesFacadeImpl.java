package stories.listprofiles;

import dtos.factories.ProfileDtoFactory;
import framework.AbstractTransactionalFacade;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

public class ListProfilesFacadeImpl
        extends AbstractTransactionalFacade<EmptyModelContext, ListProfilesFacadeResult>
        implements ListProfilesFacade {

    public ListProfilesFacadeImpl() {
        super(new EmptyModelContext(), ListProfilesFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);

        return !profileService.listAllProfiles().isEmpty();
    }

    @Override
    protected ListProfilesFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);

        return new ListProfilesFacadeResult(
                profileDtoFactory.newDtos(profileService.listAllProfiles())
        );
    }
}
