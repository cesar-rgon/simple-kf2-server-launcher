package daos;

import entities.EpicPlatform;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EpicPlatformDao extends AbstractExtendedDao<EpicPlatform> {
    private static EpicPlatformDao instance = null;

    /**
     * Singleton constructor
     */
    private EpicPlatformDao() {
        super(EpicPlatform.class);
    }

    public static EpicPlatformDao getInstance() {
        if (instance == null) {
            instance = new EpicPlatformDao();
        }
        return instance;
    }

    @Override
    public List<EpicPlatform> listAll() throws SQLException {
        String query="select p from entities.EpicPlatform p";
        return list(query, null);
    }

    @Override
    public Optional<EpicPlatform> findByCode(String code) throws SQLException {
        String query="select p from entities.EpicPlatform p where p.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
