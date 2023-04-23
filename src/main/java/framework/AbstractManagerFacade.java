package framework;

public abstract class AbstractManagerFacade<M extends ModelContext, R extends FacadeResult> extends AbstractFacade {

    protected AbstractManagerFacade(M facadeModelContext, Class<R> facadeResultClass) {
        super(facadeModelContext, facadeResultClass);
    }

    @Override
    public R execute() throws Exception {
        R result = (R) facadeResultClass.newInstance();
        if (assertPreconditions()) {
            result = internalExecute((M) facadeModelContext);
        }
        return result;
    }

    protected abstract R internalExecute(M facadeModelContext) throws Exception;

}
