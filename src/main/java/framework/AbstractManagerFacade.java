package framework;

public abstract class AbstractManagerFacade<M extends ModelContext, R extends FacadeResult> extends AbstractFacade<M,R> {

    protected AbstractManagerFacade(M facadeModelContext, Class<R> facadeResultClass) {
        super(facadeModelContext, facadeResultClass);
    }

    @Override
    public R execute() throws Exception {
        if (!assertPreconditions(getFacadeModelContext())) {
            throw new RuntimeException("The preconditions have not been satisfied for the operation " + getFacadeResultClass().getName());
        }
        return internalExecute(getFacadeModelContext());
    }

    protected abstract boolean assertPreconditions(M facadeModelContext) throws Exception;

    protected abstract R internalExecute(M facadeModelContext) throws Exception;

}
