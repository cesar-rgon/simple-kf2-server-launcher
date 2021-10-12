package stories.mapsedition;

import dtos.AbstractMapDto;
import dtos.CustomMapModDto;
import dtos.OfficialMapDto;
import dtos.ProfileDto;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface MapsEditionFacade {
    String findConfigPropertyValue(String key) throws Exception;
    void setConfigPropertyValue(String key, String value) throws Exception;
    CustomMapModDto createNewCustomMapFromWorkshop(Long idWorkShop, String installationFolder, boolean downloaded, List<String> profileNameList) throws Exception;
    CustomMapModDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder, boolean downloaded, List<String> profileNameList) throws Exception;
    boolean isCorrectInstallationFolder(String installationFolder);
    OfficialMapDto insertOfficialMap(String mapName, List<String> profileNameList) throws Exception;
    CustomMapModDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException;
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    List<AbstractMapDto> getMapsFromProfile(String profileName) throws SQLException;
    Boolean addProfilesToMap(String mapName, List<String> profileNameList) throws SQLException;
    AbstractMapDto deleteMapFromProfile(String mapName, String profileName, String installationFolder) throws Exception;
    AbstractMapDto findMapByName(String mapName) throws SQLException;
    void unselectProfileMap(String profileName) throws SQLException;
    List<String> selectProfilesToImport(String defaultSelectedProfileName) throws Exception;
}
