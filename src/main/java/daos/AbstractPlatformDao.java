package daos;

import entities.AbstractPlatform;
import entities.SteamPlatform;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AbstractPlatformDao extends AbstractExtendedDao<AbstractPlatform> {

    private static AbstractPlatformDao instance = null;

    /**
     * Singleton constructor
     */
    private AbstractPlatformDao() {
        super(AbstractPlatform.class);
    }

    public static AbstractPlatformDao getInstance() {
        if (instance == null) {
            instance = new AbstractPlatformDao();
        }
        return instance;
    }

    @Override
    public List<AbstractPlatform> listAll() throws SQLException {
        String query="select p from entities.AbstractPlatform p";
        return list(query, null);
    }

    @Override
    public Optional<AbstractPlatform> findByCode(String code) throws SQLException {
        String query="select p from entities.AbstractPlatform p where p.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
