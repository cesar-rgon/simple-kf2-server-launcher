package framework;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class AbstractTransactionalFacade<M extends ModelContext, R extends FacadeResult> extends AbstractFacade {
    private static final String PERSISTENCE_UNIT = "kf2database";
    private static EntityManagerFactory emf;

    protected AbstractTransactionalFacade(M facadeModelContext, Class<R> facadeResultClass) {
        super(facadeModelContext, facadeResultClass);
    }

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
