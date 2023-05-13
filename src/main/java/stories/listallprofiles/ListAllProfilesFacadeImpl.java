package stories.listallprofiles;

import dtos.factories.ProfileDtoFactory;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.List;

public class ListAllProfilesFacadeImpl
        extends AbstractTransactionalFacade<EmptyModelContext, ListAllProfilesFacadeResult>
        implements ListAllProfilesFacade {

    public ListAllProfilesFacadeImpl() {
        super(new EmptyModelContext(), ListAllProfilesFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ListAllProfilesFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        ProfileDtoFactory profileDtoFactory = new ProfileDtoFactory(em);

        List<Profile> profileList = profileService.listAllProfiles();
        return new ListAllProfilesFacadeResult(
                profileDtoFactory.newDtos(profileList)
        );
    }
}
