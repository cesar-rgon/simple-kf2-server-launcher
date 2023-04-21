package framework;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFacade<M extends ModelContext, R extends FacadeResult> implements IFacade {
    private static final String PERSISTENCE_UNIT = "kf2database";
    private static EntityManagerFactory emf;
    private List<FacadePrecondition> facadePreconditions;
    protected final M facadeModelContext;
    protected final Class<R> facadeResultClass;


    protected AbstractFacade(M facadeModelContext, Class<R> facadeResultClass) {
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
        R result = (R) facadeResultClass.newInstance();
        if (assertPreconditions()) {
            EntityManager em = beginTransaction();
            result = internalExecute((M) facadeModelContext, em);
            closeTransaction(em);
        }
        return result;
    }

    protected abstract R internalExecute(M facadeModelContext, EntityManager em) throws Exception;

    private EntityManager beginTransaction() throws Exception {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em;
    }

    private void closeTransaction(EntityManager em) throws Exception {
        em.getTransaction().commit();
        if (em.getTransaction() != null && em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }
}
