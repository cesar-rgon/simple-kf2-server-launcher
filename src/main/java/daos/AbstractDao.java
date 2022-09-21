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


public abstract class AbstractDao<T extends AbstractEntity> implements AutoCloseable {

	private static final Logger logger = LogManager.getLogger(AbstractDao.class);
	protected static EntityManagerFactory emf;
	protected EntityManager em;

	private static final String PERSISTENCE_UNIT = "kf2database";

	private final Class<T> entityClass;

	protected AbstractDao(Class<T> entityClass) {
		super();
		this.entityClass = entityClass;
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
		}
		if (emf != null && em == null) {
			em = emf.createEntityManager();
		}
	}

	@Override
	public void close() throws Exception {
		if (em != null) {
			if (em.isOpen()) {
				em.close();
			}
		}
	}

	/**
	 * Gets an entity by Id
	 * @param id
	 * @return Entity
	 */
	public T get(Object id) throws SQLException {
		try {
			em.getTransaction().begin();
			T result = em.find(entityClass, id);
			em.getTransaction().commit();
			return result;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to get the element %s in entity %s", id, entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		} finally {
			if (em != null) {
				if (em.getTransaction() != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			}
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
			if (em != null) {
				if (em.getTransaction() != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			}
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
			if (em != null) {
				if (em.getTransaction() != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			}
		}
	}
	
	/**
	 * Inserts an entity
	 * @param entity
	 * @return True in case of success
	 */
	public T insert(T entity) throws SQLException {
		try{
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			return entity;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to insert the element %s in entity %s", entity.getId(), entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		} finally {
			if (em != null) {
				if (em.getTransaction() != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			}
		}
	}

	/**
	 * Updates an entity
	 * @param entity
	 * @return True in case of success
	 */
	public boolean update(T entity) throws SQLException {
		try {
			em.getTransaction().begin();
			em.merge(entity);
			em.getTransaction().commit();
			return true;
		} catch (Exception e){
			String errorMessage = String.format("Database error. Error trying to update the element %s in entity %s", entity.getId(), entityClass.getName());
			logger.error(errorMessage, e);
			throw new SQLException(errorMessage, e);
		} finally {
			if (em != null) {
				if (em.getTransaction() != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			}
		}
	}
	
	/**
	 * Removes an entity
	 * @param entity
	 * @return True in case of success
	 */
	public boolean remove(T entity) throws SQLException {
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
			if (em != null) {
				if (em.getTransaction() != null && em.getTransaction().isActive()) {
					em.getTransaction().rollback();
				}
			}
		}
	}
}
