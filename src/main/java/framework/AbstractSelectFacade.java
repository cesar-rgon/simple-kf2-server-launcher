package framework;

import pojos.SelectOption;

import java.util.List;

public abstract class AbstractSelectFacade<M extends ModelContext, R extends FacadeResult> extends AbstractFacade {

    private List<SelectOption> options;

    protected AbstractSelectFacade(Class<R> facadeResultClass) throws Exception {
        super(new EmptyModelContext(), facadeResultClass);
    }

    public List<SelectOption> getValues() {
        return options;
    }

    public void setValues(Object values) {
        this.options = (List<SelectOption>) values;
    }

}
