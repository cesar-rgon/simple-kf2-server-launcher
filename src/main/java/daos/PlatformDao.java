package daos;

import entities.Platform;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PlatformDao extends AbstractExtendedDao<Platform>{

    private static PlatformDao instance = null;

    /**
     * Singleton constructor
     */
    private PlatformDao() {
        super(Platform.class);
    }

    public static PlatformDao getInstance() {
        if (instance == null) {
            instance = new PlatformDao();
        }
        return instance;
    }

    @Override
    public List<Platform> listAll() throws SQLException {
        String query="select p from entities.Platform p";
        return list(query, null);
    }

    @Override
    public Optional<Platform> findByCode(String code) throws SQLException {
        String query="select p from entities.Platform p where p.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }

}
