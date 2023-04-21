package framework;

import jakarta.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFacadeManager<M extends ModelContext, R extends FacadeResult> implements IFacade {

    private List<FacadePrecondition> facadePreconditions;

    protected final M facadeModelContext;
    protected final Class<R> facadeResultClass;


    protected AbstractFacadeManager(M facadeModelContext, Class<R> facadeResultClass) {
        super();
        facadePreconditions = new ArrayList<FacadePrecondition>();
        this.facadeModelContext = facadeModelContext;
        this.facadeResultClass = facadeResultClass;
    }

    @Override
    public List<FacadePrecondition> getFacadePreconditions() {
        return facadePreconditions;
    }

    @Override
    public void setFacadePreconditions(List<FacadePrecondition> facadeFacadePreconditions) {
        this.facadePreconditions = facadeFacadePreconditions;
    }

    @Override
    public abstract boolean assertPreconditions() throws Exception;

    @Override
    public R execute() throws Exception {
        R result = facadeResultClass.newInstance();
        if (assertPreconditions()) {
            result = internalExecute(facadeModelContext);
        }
        return result;
    }

    protected abstract R internalExecute(M facadeModelContext) throws Exception;

}
