package stories.listallplatforms;

import dtos.factories.PlatformDtoFactory;
import entities.AbstractPlatform;
import framework.AbstractTransactionalFacade;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import services.PlatformService;
import services.PlatformServiceImpl;

import java.util.List;

public class ListAllPlatformsFacadeImpl
        extends AbstractTransactionalFacade<EmptyModelContext, ListAllPlatformsFacadeResult>
        implements ListAllPlatformsFacade {

    public ListAllPlatformsFacadeImpl() {
        super(new EmptyModelContext(), ListAllPlatformsFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ListAllPlatformsFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        PlatformDtoFactory platformDtoFactory = new PlatformDtoFactory();

        List<AbstractPlatform> fullPlatformList = platformService.listAllPlatforms();
        return new ListAllPlatformsFacadeResult(
                platformDtoFactory.newDtos(fullPlatformList)
        );
    }
}
