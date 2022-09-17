package daos;

import entities.AbstractPlatform;
import entities.SteamPlatform;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SteamPlatformDao extends AbstractExtendedDao<SteamPlatform> {

    private static SteamPlatformDao instance = null;

    /**
     * Singleton constructor
     */
    private SteamPlatformDao() {
        super(SteamPlatform.class);
    }

    public static SteamPlatformDao getInstance() {
        if (instance == null) {
            instance = new SteamPlatformDao();
        }
        return instance;
    }

    @Override
    public List<SteamPlatform> listAll() throws SQLException {
        String query="select p from entities.SteamPlatform p";
        return list(query, null);
    }

    @Override
    public Optional<SteamPlatform> findByCode(String code) throws SQLException {
        String query="select p from entities.SteamPlatform p where p.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }

}
