package stories.updateitemdescription;

import daos.DescriptionDao;
import dtos.SelectDto;
import dtos.factories.AbstractDtoFactory;
import entities.AbstractExtendedEntity;
import entities.Description;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumLanguage;
import services.AbstractService;
import stories.updateitemcode.UpdateItemCodeFacade;
import stories.updateitemcode.UpdateItemCodeFacadeResult;
import stories.updateitemcode.UpdateItemCodeModelContext;

import java.util.Optional;

public class UpdateItemDescriptionFacadeImpl<E extends AbstractExtendedEntity,
                                             D extends SelectDto,
                                             F extends AbstractDtoFactory<E,D>,
                                             S extends AbstractService<E>>
        extends AbstractTransactionalFacade<UpdateItemDescriptionModelContext, UpdateItemDescriptionFacadeResult>
        implements UpdateItemDescriptionFacade {

    private final Class<S> serviceClass;
    private final Class<F> dtoFactoryClass;

    public UpdateItemDescriptionFacadeImpl(Class<S> serviceClass, Class<F> dtoFactoryClass, UpdateItemDescriptionModelContext updateItemDescriptionModelContext) {
        super(updateItemDescriptionModelContext, UpdateItemDescriptionFacadeResult.class);
        this.serviceClass = serviceClass;
        this.dtoFactoryClass = dtoFactoryClass;
    }

    @Override
    protected boolean assertPreconditions(UpdateItemDescriptionModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected UpdateItemDescriptionFacadeResult internalExecute(UpdateItemDescriptionModelContext facadeModelContext, EntityManager em) throws Exception {
        F dtoFactory = dtoFactoryClass.getDeclaredConstructor().newInstance();
        S service = serviceClass.getDeclaredConstructor().newInstance();
        service.setEm(em);

        if (StringUtils.isBlank(facadeModelContext.getNewDescription()) || facadeModelContext.getNewDescription().equals(facadeModelContext.getOldDescription())) {
            return new UpdateItemDescriptionFacadeResult();
        }

        Optional<E> itemOptional = service.findByCode(facadeModelContext.getCode());
        if (!itemOptional.isPresent()) {
            return new UpdateItemDescriptionFacadeResult();
        }

        EnumLanguage language = EnumLanguage.valueOf(facadeModelContext.getLanguageCode());
        Description newDescription = new Description();
        switch (language) {
            case en: newDescription.setEnglishValue(facadeModelContext.getNewDescription()); break;
            case es: newDescription.setSpanishValue(facadeModelContext.getNewDescription()); break;
            case fr: newDescription.setFrenchValue(facadeModelContext.getNewDescription()); break;
            case ru: newDescription.setRussianValue(facadeModelContext.getNewDescription()); break;
        }
        new DescriptionDao(em).insert(newDescription);

        itemOptional.get().setDescription(newDescription);
        service.updateItem(itemOptional.get());

        Optional<Description> oldDescriptionOptional = new DescriptionDao(em).findByValue(facadeModelContext.getOldDescription(), language);
        if (oldDescriptionOptional.isPresent()) {
            new DescriptionDao(em).remove(oldDescriptionOptional.get());
        }
        return new UpdateItemDescriptionFacadeResult(
            dtoFactory.newDto(itemOptional.get())
        );

    }
}
