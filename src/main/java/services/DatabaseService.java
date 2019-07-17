package services;

import entities.Map;
import entities.Profile;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseService {
    String findPropertyValue(String key) throws SQLException;
    List<Map> listDownloadedMaps() throws SQLException;
    List<Map> listNotDownloadedMaps() throws SQLException;
    boolean updateMap(Map map) throws SQLException;
    List<Profile> listAllProfiles() throws SQLException;
    List<Map> listOfficialMaps() throws SQLException;
    Map insertMap(Map map) throws SQLException;
}
