package stories.checkstatusservices;

import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;

public class CheckStatusServicesFacadeImpl
        extends AbstractTransactionalFacade<CheckStatusServicesModelContext, EmptyFacadeResult>
        implements CheckStatusServicesFacade {

    public CheckStatusServicesFacadeImpl(CheckStatusServicesModelContext checkStatusServicesModelContext) {
        super(checkStatusServicesModelContext, EmptyFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(CheckStatusServicesModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }
    @Override
    protected EmptyFacadeResult internalExecute(CheckStatusServicesModelContext facadeModelContext, EntityManager em) throws Exception {
        Kf2Common kf2Common = Kf2Factory.getInstance(em);
        assert kf2Common != null;
        kf2Common.checkStatusServices();
        return new EmptyFacadeResult();
    }
}
