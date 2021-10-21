package stories.mapwebinfo;

import dtos.CustomMapModDto;
import pojos.ProfileToDisplay;

import java.sql.SQLException;
import java.util.List;

public interface MapWebInfoFacade {
    String findPropertyValue(String key) throws Exception;
    boolean isCorrectInstallationFolder(String installationFolder);
    CustomMapModDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String strUrlMapImage, String installationFolder, List<String> profileNameList) throws Exception;
    CustomMapModDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException;
    void addProfilesToMap(String mapName, List<String> profileNameList) throws SQLException;
    List<ProfileToDisplay> getProfilesWithoutMap(Long idWorkShop) throws SQLException;
}
