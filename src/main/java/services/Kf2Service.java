package services;

import entities.AbstractEntity;

public interface Kf2Service<T extends AbstractEntity> {

    public T createItem(T entity) throws Exception;
    public boolean updateItemCode(T entity, String oldCode) throws Exception;
    public void updateItemDescription(T entity) throws Exception;
    public boolean deleteItem(T entity) throws Exception;

}
