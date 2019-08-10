package services;

import entities.Map;
import entities.Profile;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    List<Map> listDownloadedMaps() throws SQLException;
    List<Map> listNotDownloadedMaps() throws SQLException;
    boolean updateMap(Map map) throws SQLException;
    List<Profile> listAllProfiles() throws SQLException;
    List<Map> listOfficialMaps() throws SQLException;
    Map insertMap(Map map) throws SQLException;
    List<Map> listCustomMaps() throws SQLException;
    Map findMapByName(String mapName) throws SQLException;
}
