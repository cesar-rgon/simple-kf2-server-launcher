package services;

import entities.AbstractEntity;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public interface AbstractService<T extends AbstractEntity> {

    List<T> listAll() throws Exception;
    Optional<T> findByCode(String code) throws Exception;
    T createItem(T entity) throws Exception;
    boolean updateItem(T entity) throws Exception;
    boolean deleteItem(T entity) throws Exception;

}
