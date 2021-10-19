package daos;

import entities.AbstractEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class AbstractExtendedDao<E extends AbstractEntity> extends AbstractDao<E> {

    private Class<E> entityClass;

    public AbstractExtendedDao(Class<E> entityClass) {
        super(entityClass);
    }

    public abstract List<E> listAll() throws SQLException;
    public abstract Optional<E> findByCode(String code) throws SQLException;

}
