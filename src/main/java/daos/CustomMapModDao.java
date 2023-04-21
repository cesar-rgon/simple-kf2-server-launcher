package daos;

import entities.CustomMapMod;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class CustomMapModDao extends AbstractDao<CustomMapMod> {

    public CustomMapModDao(EntityManager em) {
        super(CustomMapMod.class, em);
    }

    public List<CustomMapMod> listAll() throws SQLException {
        String query="select cmm from entities.CustomMapMod cmm";
        return list(query, null);
    }

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
        return find(query, parameters);
    }
}
