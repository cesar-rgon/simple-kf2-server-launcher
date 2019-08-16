package services;

import entities.Map;
import entities.Profile;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    List<Map> listDownloadedMaps() throws SQLException;
    List<Map> listNotDownloadedMapsAndMods() throws SQLException;
    boolean updateMap(Map map) throws SQLException;
    List<Map> listCustomMapsAndMods() throws SQLException;
    List<Map> listCustomMaps() throws SQLException;
    Map findMapByName(String mapName) throws SQLException;
}
