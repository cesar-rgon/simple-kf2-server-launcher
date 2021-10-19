package daos;

import entities.MaxPlayers;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MaxPlayersDao extends AbstractExtendedDao<MaxPlayers> {

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

    @Override
    public List<MaxPlayers> listAll() throws SQLException {
        String query="select mp from entities.MaxPlayers mp";
        List<MaxPlayers> playerList = list(query, null);

        List<MaxPlayers> sortedPlayerList = playerList.stream().sorted((o1, o2) -> {
            String code1;
            if (o1.getCode().length() == 1) {
                code1 = "0" + o1.getCode();
            } else {
                code1 = o1.getCode();
            }
            String code2;
            if (o2.getCode().length() == 1) {
                code2 = "0" + o2.getCode();
            } else {
                code2 = o2.getCode();
            }
            return code2.compareTo(code1);
        }).collect(Collectors.toList());

        return sortedPlayerList;
    }

    @Override
    public Optional<MaxPlayers> findByCode(String code) throws SQLException {
        String query="select mp from entities.MaxPlayers mp where mp.code = :CODE";
        java.util.Map<String,Object> parameters = new HashMap<String,Object>();
        parameters.put("CODE", code);
        return find(query, parameters);
    }
}
