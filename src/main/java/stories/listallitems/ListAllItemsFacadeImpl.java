package stories.listallitems;

import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractExtendedEntity;
import framework.AbstractTransactionalFacade;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import services.AbstractService;

import java.util.List;

public class ListAllItemsFacadeImpl<E extends AbstractExtendedEntity,
                                    D extends SelectDto,
                                    F extends AbstractDtoFactory<E,D>,
                                    S extends AbstractService<E>>
        extends AbstractTransactionalFacade<EmptyModelContext, ListAllItemsFacadeResult>
        implements ListAllItemsFacade {

    private final Class<S> serviceClass;
    private final Class<F> dtoFactoryClass;

    public ListAllItemsFacadeImpl(Class<S> serviceClass, Class<F> dtoFactoryClass) {
        super(new EmptyModelContext(), ListAllItemsFacadeResult.class);
        this.serviceClass = serviceClass;
        this.dtoFactoryClass = dtoFactoryClass;
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ListAllItemsFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        F dtoFactory = dtoFactoryClass.getDeclaredConstructor().newInstance();
        S service = serviceClass.getDeclaredConstructor().newInstance();
        service.setEm(em);

        List<E> itemList = service.listAll();
        return new ListAllItemsFacadeResult(
                dtoFactory.newDtos(itemList)
        );
    }
}
