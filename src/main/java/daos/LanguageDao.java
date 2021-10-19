package daos;

import entities.Language;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class LanguageDao extends AbstractExtendedDao<Language> {

    private static LanguageDao instance = null;

    /**
     * Singleton constructor
     */
    private LanguageDao() {
        super(Language.class);
    }

    public static LanguageDao getInstance() {
        if (instance == null) {
            instance = new LanguageDao();
        }
        return instance;
    }

    @Override
    public List<Language> listAll() throws SQLException {
        String query="select l from entities.Language l order by l.id asc";
        return list(query, null);
    }

    @Override
    public Optional<Language> findByCode(String code) throws SQLException {
        String query="select l from entities.Language l where l.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
