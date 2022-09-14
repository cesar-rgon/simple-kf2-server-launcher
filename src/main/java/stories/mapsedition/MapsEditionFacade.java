package stories.mapsedition;

import dtos.*;
import entities.AbstractMap;
import entities.PlatformProfileMap;
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
    AbstractMapDto addProfilesToMap(String platformName, String mapName, List<String> profileNameList, List<ImportMapResultToDisplay> importMapResultToDisplayList) throws SQLException;
    AbstractMapDto deleteMapFromProfile(String platformName, String mapName, String profileName, String installationFolder) throws Exception;
    void unselectProfileMap(String profileName) throws SQLException;
    List<String> selectProfilesToImport(String defaultSelectedProfileName) throws Exception;
    String runServer(String profileName) throws SQLException;
    List<AbstractMapDto> addCustomMapsToProfile(String platformName, String profileName, String mapNameList, String languageCode, String installationFolder, String actualSelectedProfile, StringBuffer success, StringBuffer errors);
    CustomMapModDto importCustomMapModFromServer(String platformName, String mapNameLabel, Long idWorkShop, String commentary, String installationFolder, List<String> selectedProfileNameList, String actualSelectedProfile, List<ImportMapResultToDisplay> importMapResultToDisplayList);
    OfficialMapDto importOfficialMapFromServer(String platformName, String officialMapName, List<String> selectedProfileNameList, String actualSelectedProfile, String mapNameLabel, List<ImportMapResultToDisplay> importMapResultToDisplayList);
    Optional<AbstractMap> findMapByName(String mapName) throws SQLException;
    Optional<PlatformProfileMapDto> findPlatformProfileMapDtoByNames(String platformName, String profileName, String mapName) throws SQLException;
    Optional<PlatformProfileMap> findPlatformProfileMapByNames(String platformName, String profileName, String mapName) throws SQLException;
    List<PlatformProfileMapDto> listPlatformProfileMaps(String platformName, String profileName) throws SQLException;
}
