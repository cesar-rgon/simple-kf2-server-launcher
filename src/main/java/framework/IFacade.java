package framework;

import java.util.List;

public interface IFacade {

    List<FacadePrecondition> getFacadePreconditions();
    void setFacadePreconditions(List<FacadePrecondition> facadeFacadePreconditions);
    boolean assertPreconditions() throws Exception;
    FacadeResult execute() throws Exception;

}
