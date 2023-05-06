package stories.addplatformprofilestomap;

import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;

public class AddPlatformProfilesToMapFacadeImpl
        extends AbstractTransactionalFacade<AddPlatformProfilesToMapModelContext, AddPlatformProfilesToMapFacadeResult>
        implements AddPlatformProfilesToMapFacade {


    public AddPlatformProfilesToMapFacadeImpl(AddPlatformProfilesToMapModelContext addPlatformProfilesToMapModelContext) {
        super(addPlatformProfilesToMapModelContext, AddPlatformProfilesToMapFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(AddPlatformProfilesToMapModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected AddPlatformProfilesToMapFacadeResult internalExecute(AddPlatformProfilesToMapModelContext facadeModelContext, EntityManager em) throws Exception {
        return null;
    }
}
