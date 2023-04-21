package daos;

import entities.AbstractPlatform;
import entities.SteamPlatform;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SteamPlatformDao extends AbstractDao<SteamPlatform> {

    public SteamPlatformDao(EntityManager em) {
        super(SteamPlatform.class, em);
    }

    public List<SteamPlatform> listAll() throws SQLException {
        String query="select p from entities.SteamPlatform p";
        return list(query, null);
    }

    public Optional<SteamPlatform> findByCode(String code) throws SQLException {
        String query="select p from entities.SteamPlatform p where p.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }

}
