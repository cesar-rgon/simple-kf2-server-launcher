package stories.updateitemcode;

import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractExtendedEntity;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import services.AbstractService;

import java.util.Optional;

public class UpdateItemCodeFacadeImpl<E extends AbstractExtendedEntity,
                                      D extends SelectDto,
                                      F extends AbstractDtoFactory<E,D>,
                                      S extends AbstractService<E>>
        extends AbstractTransactionalFacade<UpdateItemCodeModelContext, UpdateItemCodeFacadeResult>
        implements UpdateItemCodeFacade {

    private final Class<S> serviceClass;
    private final Class<F> dtoFactoryClass;

    public UpdateItemCodeFacadeImpl(Class<S> serviceClass, Class<F> dtoFactoryClass, UpdateItemCodeModelContext updateItemCodeModelContext) {
        super(updateItemCodeModelContext, UpdateItemCodeFacadeResult.class);
        this.serviceClass = serviceClass;
        this.dtoFactoryClass = dtoFactoryClass;
    }

    @Override
    protected boolean assertPreconditions(UpdateItemCodeModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected UpdateItemCodeFacadeResult internalExecute(UpdateItemCodeModelContext facadeModelContext, EntityManager em) throws Exception {
        F dtoFactory = dtoFactoryClass.getDeclaredConstructor().newInstance();
        S service = serviceClass.getDeclaredConstructor().newInstance();
        service.setEm(em);

        if (StringUtils.isBlank(facadeModelContext.getNewCode()) || facadeModelContext.getNewCode().equalsIgnoreCase(facadeModelContext.getOldCode())) {
            return new UpdateItemCodeFacadeResult();
        }

        Optional<E> itemOptional = service.findByCode(facadeModelContext.getOldCode());
        if (!itemOptional.isPresent()) {
            return new UpdateItemCodeFacadeResult();
        }

        itemOptional.get().setCode(facadeModelContext.getNewCode());
        if (!service.updateItem(itemOptional.get())) {
            return new UpdateItemCodeFacadeResult();
        }

        return new UpdateItemCodeFacadeResult(
            dtoFactory.newDto(itemOptional.get())
        );
    }
}
