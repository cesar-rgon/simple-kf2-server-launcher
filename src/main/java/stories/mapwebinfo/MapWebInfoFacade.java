package stories.mapwebinfo;

import dtos.MapDto;
import entities.Map;
import pojos.ProfileToDisplay;

import java.sql.SQLException;
import java.util.List;

public interface MapWebInfoFacade {
    String findPropertyValue(String key) throws Exception;
    boolean isCorrectInstallationFolder(String installationFolder);
    MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String strUrlMapImage, String installationFolder, List<String> profileNameList) throws Exception;
    MapDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException;
    boolean addProfilesToMap(String mapName, List<String> profileNameList) throws SQLException;
    List<ProfileToDisplay> getProfilesWithoutMap(Long idWorkShop) throws SQLException;
}
