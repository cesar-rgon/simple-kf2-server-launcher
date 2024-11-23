package stories.template;

import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import stories.listallplatforms.ListAllPlatformsFacade;
import stories.listallplatforms.ListAllPlatformsFacadeImpl;
import stories.listallplatforms.ListAllPlatformsFacadeResult;

public class TemplateManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, ListAllPlatformsFacadeResult>
        implements TemplateManagerFacade {

    public TemplateManagerFacadeImpl() {
        super(new EmptyModelContext(), ListAllPlatformsFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected ListAllPlatformsFacadeResult internalExecute(EmptyModelContext facadeModelContext) throws Exception {
        ListAllPlatformsFacade listAllPlatformsFacade = new ListAllPlatformsFacadeImpl();
        return listAllPlatformsFacade.execute();
    }
}
