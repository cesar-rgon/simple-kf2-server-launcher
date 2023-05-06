package stories.createcustommapfromworkshop;

import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;

public class CreateCustomMapFromWorkshopFacadeImpl
        extends AbstractTransactionalFacade<CreateCustomMapFromWorkshopModelContext, CreateCustomMapFromWorkshopFacadeResult>
        implements CreateCustomMapFromWorkshopFacade {

    public CreateCustomMapFromWorkshopFacadeImpl(CreateCustomMapFromWorkshopModelContext createCustomMapFromWorkshopModelContext) {
        super(createCustomMapFromWorkshopModelContext, CreateCustomMapFromWorkshopFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(CreateCustomMapFromWorkshopModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected CreateCustomMapFromWorkshopFacadeResult internalExecute(CreateCustomMapFromWorkshopModelContext facadeModelContext, EntityManager em) throws Exception {
        return null;
    }
}
