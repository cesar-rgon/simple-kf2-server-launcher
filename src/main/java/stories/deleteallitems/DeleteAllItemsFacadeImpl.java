package stories.deleteallitems;

import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractExtendedEntity;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import services.AbstractService;

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
        S service = serviceClass.getDeclaredConstructor().newInstance();
        service.setEm(em);

        List<E> allItemList = service.listAll();
        // TODO: Implementar service.deleteAllItems(allItemList, allProfileList); donde antes de eliminar se pone a null el item seleccionado de todos los perfiles.
        // TODO: Además en el service.deleteItem(item); que ponga a null el item seleccionado para todos los perfiles, si lo contienen.
        // TODO: Eliminar los casos de uso "unselect*inprofile", ya no harán falta.
        for (E item: allItemList) {
            service.deleteItem(item);
        }

        return new EmptyFacadeResult();
    }
}
