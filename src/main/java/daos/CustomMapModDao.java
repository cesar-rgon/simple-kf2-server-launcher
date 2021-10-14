package daos;

import entities.CustomMapMod;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CustomMapModDao extends AbstractDao<CustomMapMod> {

    private static CustomMapModDao instance = null;

    /**
     * Singleton constructor
     */
    private CustomMapModDao() {
        super(CustomMapMod.class);
    }

    public static CustomMapModDao getInstance() {
        if (instance == null) {
            instance = new CustomMapModDao();
        }
        return instance;
    }

    @Override
    public List<CustomMapMod> listAll() throws SQLException {
        String query="select cmm from entities.CustomMapMod cmm";
        return list(query, null);
    }

    @Override
    public Optional<CustomMapMod> findByCode(String code) throws SQLException {
        String query="select cmm from entities.CustomMapMod cmm where cmm.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }

    public Optional<CustomMapMod> findByIdWorkShop(Long idWorkShop) throws SQLException {
        String query="select cmm from entities.CustomMapMod cmm where cmm.idWorkShop=:IDWORKSHOP";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("IDWORKSHOP", idWorkShop);
        List<CustomMapMod> list = list(query, parameters);
        return (list != null && !list.isEmpty())? Optional.ofNullable(list.get(0)): Optional.empty();
    }
}
