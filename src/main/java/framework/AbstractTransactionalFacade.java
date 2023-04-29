package framework;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import services.PropertyService;
import services.PropertyServiceImpl;

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

    private EntityManager beginTransaction() throws Exception {
        if (emf == null) {
            PropertyService propertyService = new PropertyServiceImpl();
            boolean updateDatabase = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.updateDatabase"));
            Properties properties = new Properties();
            if (updateDatabase) {
                properties.setProperty("hibernate.connection.url", "jdbc:derby:kf2database;create=true");
                properties.setProperty("hibernate.hbm2ddl.auto", "update");
            } else {
                properties.setProperty("hibernate.connection.url", "jdbc:derby:kf2database;create=false");
                properties.setProperty("hibernate.hbm2ddl.auto", "none");
            }
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
        }
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        return em;
    }

    private void commitTransaction(EntityManager em) throws Exception {
        em.getTransaction().commit();
        if (em.getTransaction() != null && em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        em.close();
    }

    private void rollbackTransaction(EntityManager em) throws Exception {
        em.getTransaction().rollback();
        em.close();
    }
}
