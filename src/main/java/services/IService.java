package services;

import entities.AbstractEntity;
import entities.AbstractExtendedEntity;
import entities.Profile;

import java.util.List;
import java.util.Optional;

public interface IService<T extends AbstractEntity> {

    List<T> listAll() throws Exception;
    Optional<T> findByCode(String code) throws Exception;
    T createItem(T entity) throws Exception;
    boolean updateItem(T entity) throws Exception;
    boolean deleteItem(T entity) throws Exception;
    boolean deleteItem(T entity, List<Profile> profileList) throws Exception;
    boolean deleteAllItems(List<T> entityList, List<Profile> profileList) throws Exception;

}
