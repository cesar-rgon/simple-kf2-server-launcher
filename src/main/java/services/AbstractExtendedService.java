package services;

import entities.AbstractExtendedEntity;

public interface AbstractExtendedService<T extends AbstractExtendedEntity> extends AbstractService<T> {

    boolean updateItemCode(T entity, String oldCode) throws Exception;
    void updateItemDescription(T entity) throws Exception;

}
