package stories.mapwebinfo;

import dtos.MapDto;
import entities.Map;

import java.sql.SQLException;

public interface MapWebInfoFacade {
    boolean isMapInProfile(Long idWorkShop, String profileName);
    String findPropertyValue(String key) throws Exception;
    boolean isCorrectInstallationFolder(String installationFolder);
    MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String strUrlMapImage, String installationFolder, String profileName) throws Exception;
    MapDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException;
    boolean addProfileToMap(String mapName, String profileName) throws SQLException;
}
