package daos;

import entities.Description;
import jakarta.persistence.Column;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumLanguage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class DescriptionDao extends AbstractDao<Description> {

    public DescriptionDao(EntityManager em) {
        super(Description.class, em);
    }

    public List<Description> listAll() throws SQLException {
        String query="select d from entities.Description d";
        return list(query, null);
    }

    public Optional<Description> findByCode(String code) throws SQLException {
        return Optional.empty();
    }

    public Optional<Description> findByValue(String code, EnumLanguage language) throws SQLException {
        String query = StringUtils.EMPTY;
        switch (language) {
            case en:
                query="select d from entities.Description d where d.englishValue=:CODE";
                break;
            case es:
                query="select d from entities.Description d where d.spanishValue=:CODE";
                break;
            case fr:
                query="select d from entities.Description d where d.frenchValue=:CODE";
                break;
            case ru:
                query="select d from entities.Description d where d.russianValue=:CODE";
                break;
        }
        if (StringUtils.isBlank(query)) {
            return Optional.empty();
        }
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
