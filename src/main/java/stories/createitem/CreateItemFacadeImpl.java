package stories.createitem;

import daos.DescriptionDao;
import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractExtendedEntity;
import entities.Description;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import pojos.enums.EnumLanguage;
import services.AbstractService;

public class CreateItemFacadeImpl<E extends AbstractExtendedEntity,
                                  D extends SelectDto,
                                  F extends AbstractDtoFactory<E,D>,
                                  S extends AbstractService<E>>
        extends AbstractTransactionalFacade<CreateItemModelContext, CreateItemFacadeResult>
        implements CreateItemFacade {

    private final Class<E> entityClass;
    private final Class<S> serviceClass;
    private final Class<F> dtoFactoryClass;

    public CreateItemFacadeImpl(Class<E> entityClass, Class<S> serviceClass, Class<F> dtoFactoryClass, CreateItemModelContext createItemModelContext) {
        super(createItemModelContext, CreateItemFacadeResult.class);
        this.entityClass = entityClass;
        this.serviceClass = serviceClass;
        this.dtoFactoryClass = dtoFactoryClass;
    }

    @Override
    protected boolean assertPreconditions(CreateItemModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected CreateItemFacadeResult internalExecute(CreateItemModelContext facadeModelContext, EntityManager em) throws Exception {
        F dtoFactory = dtoFactoryClass.getDeclaredConstructor().newInstance();
        S service = serviceClass.getDeclaredConstructor().newInstance();
        service.setEm(em);

        EnumLanguage language = EnumLanguage.valueOf(facadeModelContext.getLanguageCode());
        Description description = new Description();
        switch (language) {
            case en: description.setEnglishValue(facadeModelContext.getDescription()); break;
            case es: description.setSpanishValue(facadeModelContext.getDescription()); break;
            case fr: description.setFrenchValue(facadeModelContext.getDescription()); break;
            default: description.setRussianValue(facadeModelContext.getDescription()); break;
        }
        new DescriptionDao(em).insert(description);

        E item = entityClass.getDeclaredConstructor().newInstance();
        item.setCode(facadeModelContext.getCode());
        item.setDescription(description);

        return new CreateItemFacadeResult(
            dtoFactory.newDto(service.createItem(item))
        );
    }
}
