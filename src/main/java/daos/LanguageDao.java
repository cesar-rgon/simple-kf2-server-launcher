package daos;

import entities.Language;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LanguageDao extends AbstractDao<Language> {

    public LanguageDao(EntityManager em) {
        super(Language.class, em);
    }

    public List<Language> listAll() throws SQLException {
        String query="select l from entities.Language l order by l.id asc";
        return list(query, null);
    }

    public Optional<Language> findByCode(String code) throws SQLException {
        String query="select l from entities.Language l where l.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
