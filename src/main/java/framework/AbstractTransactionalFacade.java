package framework;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import services.PropertyService;
import services.PropertyServiceImpl;
import start.MainApplication;

import java.io.File;
import java.util.Properties;

public abstract class AbstractTransactionalFacade<M extends ModelContext, R extends FacadeResult> extends AbstractFacade<M,R> {
    private static final String PERSISTENCE_UNIT = "kf2database";
    private static EntityManagerFactory emf;

    protected AbstractTransactionalFacade(M facadeModelContext, Class<R> facadeResultClass) {
        super(facadeModelContext, facadeResultClass);
    }

    @Override
    public R execute() throws Exception {
        EntityManager em = beginTransaction();
        if (!assertPreconditions(getFacadeModelContext(), em)) {
            rollbackTransaction(em);
            throw new RuntimeException("The preconditions have not been satisfied for the actual operation");
        }
        R result = internalExecute(getFacadeModelContext(), em);
        commitTransaction(em);
        return result;
    }

    protected abstract boolean assertPreconditions(M facadeModelContext, EntityManager em) throws Exception;

    protected abstract R internalExecute(M facadeModelContext, EntityManager em) throws Exception;

    protected EntityManager beginTransaction() throws Exception {
        if (emf == null) {
            Properties properties = new Properties();

            String databasePath;
            File file = new File(System.getProperty("user.dir") + "/kf2database.mv.db");
            if (file.exists()) {
                databasePath = System.getProperty("user.dir") + "/kf2database.mv.db";
            } else {
                databasePath = "kf2database.mv.db";
            }
            File databaseFile = new File(databasePath);
            MainApplication.setCreateDatabase(!databaseFile.exists());
            if (databaseFile.exists()) {
                properties.setProperty("hibernate.hbm2ddl.auto", "none");
            } else {
                properties.setProperty("hibernate.hbm2ddl.auto", "create");
            }

            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
        }
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em;
    }

    protected void commitTransaction(EntityManager em) throws Exception {
        em.getTransaction().commit();
        if (em.getTransaction() != null && em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }

    protected void rollbackTransaction(EntityManager em) throws Exception {
        em.getTransaction().rollback();
        em.close();
    }
}
