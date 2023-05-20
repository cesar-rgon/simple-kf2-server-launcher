package stories.deleteallitems;

import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractExtendedEntity;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import services.AbstractService;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.List;

public class DeleteAllItemsFacadeImpl<E extends AbstractExtendedEntity,
                                      D extends SelectDto,
                                      F extends AbstractDtoFactory<E,D>,
                                      S extends AbstractService<E>>
        extends AbstractTransactionalFacade<EmptyModelContext, EmptyFacadeResult>
        implements DeleteAllItemsFacade {


    private final Class<S> serviceClass;

    public DeleteAllItemsFacadeImpl(Class<S> serviceClass) {
        super(new EmptyModelContext(), EmptyFacadeResult.class);
        this.serviceClass = serviceClass;
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        S service = serviceClass.getDeclaredConstructor().newInstance();
        service.setEm(em);

        List<Profile> allProfileList = profileService.listAllProfiles();
        List<E> allItemList = service.listAll();
        service.deleteAllItems(allItemList, allProfileList);

        return new EmptyFacadeResult();
    }
}
