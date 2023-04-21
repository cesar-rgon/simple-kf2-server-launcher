package daos;

import entities.Profile;
import jakarta.persistence.EntityManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ProfileDao extends AbstractDao<Profile> {

    public ProfileDao(EntityManager em) {
        super(Profile.class, em);
    }

    public List<Profile> listAll() throws SQLException {
        String query = "select p from entities.Profile p order by p.name asc";
        return list(query, null);
    }

    public Optional<Profile> findByCode(String profileName) throws SQLException {
        String query = "select p from entities.Profile p where p.name = :PROFILENAME";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("PROFILENAME", profileName);
        return find(query, parameters);
    }
}
