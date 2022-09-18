package stories.mapwebinfo;

import dtos.CustomMapModDto;
import pojos.ProfileToDisplay;

import java.sql.SQLException;
import java.util.List;

public interface MapWebInfoFacade {
    boolean isCorrectInstallationFolder(String installationFolder);
    CustomMapModDto createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, String mapName, String strUrlMapImage, List<String> profileNameList) throws Exception;
    CustomMapModDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException;
    void addProfilesToMap(List<String> platformNameList, String mapName, List<String> profileNameList) throws SQLException;
    List<ProfileToDisplay> getProfilesWithoutMap(Long idWorkShop) throws SQLException;
}
