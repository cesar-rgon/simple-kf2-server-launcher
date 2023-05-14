package services;

import entities.AbstractEntity;
import jakarta.persistence.EntityManager;

public abstract class AbstractService<T extends AbstractEntity> implements IService<T> {

    protected EntityManager em;

    public AbstractService() {
        super();
    }

    public AbstractService(EntityManager em) {
        super();
        this.em = em;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
