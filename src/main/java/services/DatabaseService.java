package services;

import entities.Map;
import entities.Profile;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    List<Map> listNotOfficialMaps() throws SQLException;
    boolean updateMap(Map map) throws SQLException;
}
