package services;

import entities.Map;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    String findPropertyValue(String key) throws SQLException;
    List<Map> listNotDownloadedMaps() throws SQLException;
    boolean updateMap(Map map) throws SQLException;
}
