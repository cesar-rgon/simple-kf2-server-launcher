package daos;

import entities.MaxPlayers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MaxPlayersDao extends CommonDao<MaxPlayers> {

    private static MaxPlayersDao instance = null;

    /**
     * Singleton constructor
     */
    private MaxPlayersDao() {
        super(MaxPlayers.class);
    }

    public static MaxPlayersDao getInstance() {
        if (instance == null) {
            instance = new MaxPlayersDao();
        }
        return instance;
    }

    public List<MaxPlayers> listAll() throws SQLException {
        String query="select mp from entities.MaxPlayers mp order by mp.code desc";
        return list(query, null);
    }

    public Optional<MaxPlayers> findByCode(String code) throws SQLException {
        String query="select mp from entities.MaxPlayers mp where mp.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
