package daos;


import entities.AbstractEntity;
import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;


public abstract class AbstractDao<T extends AbstractEntity> {

	private static final Logger logger = LogManager.getLogger(AbstractDao.class);
	protected EntityManager em;
	private final Class<T> entityClass;

	protected AbstractDao(Class<T> entityClass, EntityManager em) {
		super();
		this.entityClass = entityClass;
		this.em = em;
	}


	/**
	 * Gets an entity by Id
	 * @param id
	 * @return Entity
	 */
	public T get(Object id) throws SQLException {
		try {
			return em.find(entityClass, id);
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to get the element %s in entity %s", id, entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		}
	}
	
	/**
	 * Gets a list of entities by a query and parameters
	 * @param query
	 * @param parameters
	 * @return List of entities
	 */
	public List<T> list(String query, Map<String,Object> parameters) throws SQLException {
		try {
			Query queryObject = em.createQuery(query);
			if (parameters != null && !parameters.isEmpty()) {
				for (Entry<String,Object> parameter: parameters.entrySet()) {
					queryObject.setParameter(parameter.getKey(), parameter.getValue());
				}
			}
			return (List<T>)queryObject.getResultList();
		} catch (Exception e) {
			String errorMessage = String.format("Database error. Error trying to list elements of the entity %s", entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		}
	}
	
	/**
	 * Gets an entity by a query and parameters
	 * @param query
	 * @param parameters
	 * @return Entity
	 */
	public Optional<T> find(String query, Map<String,Object> parameters) throws SQLException {
		try {
			Query queryObject = em.createQuery(query);
			if (parameters != null && !parameters.isEmpty()) {
				for (Entry<String, Object> parameter : parameters.entrySet()) {
					queryObject.setParameter(parameter.getKey(), parameter.getValue());
				}
			}
			return Optional.ofNullable((T) queryObject.getSingleResult());
		} catch (NoResultException e) {
			String message = String.format("No results were found by the query %s in entity %s", query, entityClass.getName());
			logger.info(message, e);
			return Optional.empty();
		} catch (Exception e) {
			String errorMessage = String.format("Database error. Error trying to find an element of the entity %s", entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		}
	}
	
	/**
	 * Inserts an entity
	 * @param entity
	 * @return True in case of success
	 */
	public T insert(T entity) throws SQLException {
		try{
			em.persist(entity);
			return entity;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to insert the element %s in entity %s", entity.getId(), entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		}
	}

	/**
	 * Updates an entity
	 * @param entity
	 * @return True in case of success
	 */
	public boolean update(T entity) throws SQLException {
		try {
			em.merge(entity);
			return true;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to update the element %s in entity %s", entity.getId(), entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		}
	}
	
	/**
	 * Removes an entity
	 * @param entity
	 * @return True in case of success
	 */
	public boolean remove(T entity) throws SQLException {
		try {
			entity = em.merge(entity);
			em.remove(entity);
			return true;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to remove the element %s in entity %s", entity.getId(), entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		}
	}
}
