package services;

import entities.AbstractEntity;

public interface AbstractService<T extends AbstractEntity> {

    T createItem(T entity) throws Exception;
    boolean deleteItem(T entity) throws Exception;

}
