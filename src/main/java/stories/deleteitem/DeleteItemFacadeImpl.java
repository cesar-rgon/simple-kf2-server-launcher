package stories.deleteitem;

import daos.DescriptionDao;
import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractExtendedEntity;
import entities.Description;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.AbstractService;
import services.ProfileService;
import services.ProfileServiceImpl;

import java.util.List;
import java.util.Optional;

public class DeleteItemFacadeImpl<E extends AbstractExtendedEntity,
                                  D extends SelectDto,
                                  F extends AbstractDtoFactory<E,D>,
                                  S extends AbstractService<E>>
        extends AbstractTransactionalFacade<DeleteItemModelContext, EmptyFacadeResult>
        implements DeleteItemFacade {

    private final Class<E> entityClass;
    private final Class<S> serviceClass;
    private final Class<F> dtoFactoryClass;

    public DeleteItemFacadeImpl(Class<E> entityClass, Class<S> serviceClass, Class<F> dtoFactoryClass, DeleteItemModelContext deleteItemModelContext) {
        super(deleteItemModelContext, EmptyFacadeResult.class);
        this.entityClass = entityClass;
        this.serviceClass = serviceClass;
        this.dtoFactoryClass = dtoFactoryClass;
    }


    @Override
    protected boolean assertPreconditions(DeleteItemModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(DeleteItemModelContext facadeModelContext, EntityManager em) throws Exception {
        ProfileService profileService = new ProfileServiceImpl(em);
        S service = serviceClass.getDeclaredConstructor().newInstance();
        service.setEm(em);

        List<Profile> allProfileList = profileService.listAllProfiles();
        Optional<E> itemOptional = service.findByCode(facadeModelContext.getCode());
        if (itemOptional.isPresent()) {
            service.deleteItem(itemOptional.get(), allProfileList);
        }

        return new EmptyFacadeResult();
    }
}
