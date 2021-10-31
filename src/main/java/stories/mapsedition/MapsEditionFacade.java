package stories.mapsedition;

import dtos.AbstractMapDto;
import dtos.CustomMapModDto;
import dtos.OfficialMapDto;
import dtos.ProfileDto;
import entities.AbstractMap;
import entities.CustomMapMod;
import entities.Profile;
import javafx.collections.ObservableList;
import pojos.ImportMapResultToDisplay;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MapsEditionFacade {
    String findConfigPropertyValue(String key) throws Exception;
    void setConfigPropertyValue(String key, String value) throws Exception;
    boolean isCorrectInstallationFolder(String installationFolder);
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    List<AbstractMapDto> getMapsFromProfile(String profileName) throws SQLException;
    AbstractMapDto addProfilesToMap(String mapName, List<String> profileNameList, List<ImportMapResultToDisplay> importMapResultToDisplayList) throws SQLException;
    AbstractMapDto deleteMapFromProfile(String mapName, String profileName, String installationFolder) throws Exception;
    void unselectProfileMap(String profileName) throws SQLException;
    List<String> selectProfilesToImport(String defaultSelectedProfileName) throws Exception;
    String runServer(String profileName) throws SQLException;
    List<AbstractMapDto> addCustomMapsToProfile(String profileName, String mapNameList, String languageCode, String installationFolder, String actualSelectedProfile, StringBuffer success, StringBuffer errors);
    CustomMapModDto importCustomMapModFromServer(String mapNameLabel, Long idWorkShop, String commentary, String installationFolder, List<String> selectedProfileNameList, String actualSelectedProfile, List<ImportMapResultToDisplay> importMapResultToDisplayList);
    OfficialMapDto importOfficialMapFromServer(String officialMapName, List<String> selectedProfileNameList, String actualSelectedProfile, String mapNameLabel, List<ImportMapResultToDisplay> importMapResultToDisplayList);
    Optional<AbstractMapDto> findMapDtoByName(String mapName) throws SQLException;
}
