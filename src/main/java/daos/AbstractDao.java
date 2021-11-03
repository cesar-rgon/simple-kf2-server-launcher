package daos;


import entities.AbstractEntity;
import entities.Profile;
import entities.ProfileMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;


public abstract class AbstractDao<T extends AbstractEntity> {

	private static final Logger logger = LogManager.getLogger(AbstractDao.class);
	protected static EntityManagerFactory emf;
	private static final String PERSISTENCE_UNIT = "kf2database";

	private final Class<T> entityClass;

	protected AbstractDao(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		}
	}

	/**
	 * Gets an entity by Id
	 * @param id
	 * @return Entity
	 */
	public T get(Object id) throws SQLException {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			T result = em.find(entityClass, id);
			em.getTransaction().commit();
			return result;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to get the element %s in entity %s", id, entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		} finally {
			endTransaction(em);
		}
	}
	
	/**
	 * Gets a list of entities by a query and parameters
	 * @param query
	 * @param parameters
	 * @return List of entities
	 */
	public List<T> list(String query, Map<String,Object> parameters) throws SQLException {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			Query queryObject = em.createQuery(query);
			if (parameters != null && !parameters.isEmpty()) {
				for (Entry<String,Object> parameter: parameters.entrySet()) {
					queryObject.setParameter(parameter.getKey(), parameter.getValue());
				}
			}
			List<T> result = (List<T>)queryObject.getResultList();
			em.getTransaction().commit();
			return result;
		} catch (Exception e) {
			String errorMessage = String.format("Database error. Error trying to list elements of the entity %s", entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		} finally {
			endTransaction(em);
		}
	}
	
	/**
	 * Gets an entity by a query and parameters
	 * @param query
	 * @param parameters
	 * @return Entity
	 */
	public Optional<T> find(String query, Map<String,Object> parameters) throws SQLException {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			Query queryObject = em.createQuery(query);
			if (parameters != null && !parameters.isEmpty()) {
				for (Entry<String, Object> parameter : parameters.entrySet()) {
					queryObject.setParameter(parameter.getKey(), parameter.getValue());
				}
			}
			T result = (T) queryObject.getSingleResult();
			em.getTransaction().commit();
			return Optional.ofNullable(result);
		} catch (NoResultException e) {
			String message = String.format("No results were found by the query %s in entity %s", query, entityClass.getName());
			logger.info(message, e);
			return Optional.empty();
		} catch (Exception e) {
			String errorMessage = String.format("Database error. Error trying to find an element of the entity %s", entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		} finally {
			endTransaction(em);
		}
	}
	
	/**
	 * Inserts an entity
	 * @param entity
	 * @return True in case of success
	 */
	public T insert(T entity) throws SQLException {
		EntityManager em = null;
		try{
			em = emf.createEntityManager();
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			return entity;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to insert the element %s in entity %s", entity.getId(), entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		} finally {
			endTransaction(em);
		}
	}

	/**
	 * Updates an entity
	 * @param entity
	 * @return True in case of success
	 */
	public boolean update(T entity) throws SQLException {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
			return true;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to update the element %s in entity %s", entity.getId(), entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		} finally {
			endTransaction(em);
		}
	}
	
	/**
	 * Removes an entity
	 * @param entity
	 * @return True in case of success
	 */
	public boolean remove(T entity) throws SQLException {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();
			entity = em.merge(entity);
			em.remove(entity);
			em.getTransaction().commit();
			return true;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to remove the element %s in entity %s", entity.getId(), entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		} finally {
			endTransaction(em);
		}
	}

	/**
	 * Ends a database transaction
	 * @param em
	 */
	private void endTransaction(EntityManager em) {
		if (em != null) {
			if (em.getTransaction() != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			if (em.isOpen()) {
				em.close();
			}
		}
	}

}
