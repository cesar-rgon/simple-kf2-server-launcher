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
    MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String installationFolder, boolean downloaded, Boolean isMod) throws Exception;
    MapDto createNewCustomMapFromWorkshop(Long idWorkShop, String mapName, String installationFolder, boolean downloaded, Boolean isMod) throws Exception;
    MapDto deleteSelectedMap(String mapName) throws SQLException;
    boolean isCorrectInstallationFolder(String installationFolder);
    MapDto insertOfficialMap(String mapName) throws SQLException;
    MapDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException;
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    List<MapDto> getMapsFromProfile(String profileName) throws SQLException;
}
