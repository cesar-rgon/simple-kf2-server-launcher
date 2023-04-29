package framework;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFacade<M extends ModelContext, R extends FacadeResult> implements IFacade {

    private final M facadeModelContext;
    private final Class<R> facadeResultClass;

    protected AbstractFacade(M facadeModelContext, Class<R> facadeResultClass) {
        super();
        this.facadeModelContext = facadeModelContext;
        this.facadeResultClass = facadeResultClass;
    }

    protected M getFacadeModelContext() {
        return facadeModelContext;
    }

    protected Class<R> getFacadeResultClass() {
        return facadeResultClass;
    }
}
