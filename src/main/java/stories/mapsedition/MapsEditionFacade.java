package stories.mapsedition;

import dtos.MapDto;
import dtos.ProfileDto;
import entities.Map;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MapsEditionFacade {
    String findConfigPropertyValue(String key) throws Exception;
    void setConfigPropertyValue(String key, String value) throws Exception;
    MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String installationFolder, boolean downloaded, Boolean isMod, String profileName) throws Exception;
    MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder, boolean downloaded, Boolean isMod, String profileName) throws Exception;
    boolean isCorrectInstallationFolder(String installationFolder);
    MapDto insertOfficialMap(String mapName, String profileName) throws SQLException;
    MapDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException;
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    List<MapDto> getMapsFromProfile(String profileName) throws SQLException;
    Boolean addProfileToMap(String mapName, String profileName) throws SQLException;
    MapDto deleteMapFromProfile(String mapName, String profileName, String installationFolder) throws Exception;
    MapDto findMapByName(String mapName) throws SQLException;
    void unselectProfileMap(String profileName) throws SQLException;
}
