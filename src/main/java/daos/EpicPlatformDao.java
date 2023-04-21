package daos;

import entities.EpicPlatform;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EpicPlatformDao extends AbstractDao<EpicPlatform> {

    public EpicPlatformDao(EntityManager em) {
        super(EpicPlatform.class, em);
    }

    public List<EpicPlatform> listAll() throws SQLException {
        String query="select p from entities.EpicPlatform p";
        return list(query, null);
    }

    public Optional<EpicPlatform> findByCode(String code) throws SQLException {
        String query="select p from entities.EpicPlatform p where p.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
