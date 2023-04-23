package framework;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFacade<M extends ModelContext, R extends FacadeResult> implements IFacade {

    private List<FacadePrecondition> facadePreconditions;
    protected final M facadeModelContext;
    protected final Class<R> facadeResultClass;

    protected AbstractFacade(M facadeModelContext, Class<R> facadeResultClass) {
        super();
        this.facadePreconditions = new ArrayList<FacadePrecondition>();
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

}
