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
    boolean isCorrectInstallationFolder(String platformName);
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    public void addPlatformProfileMapList(List<PlatformProfileMap> platformProfileMapListToAdd, StringBuffer success, StringBuffer errors) throws SQLException;
    AbstractMapDto deleteMapFromPlatformProfile(String platformName, String mapName, String profileName) throws Exception;
    void unselectProfileMap(String profileName) throws SQLException;
    List<String> selectProfilesToImport(String defaultSelectedProfileName) throws Exception;
    String runServer(String profileName) throws SQLException;
    List<PlatformProfileMapDto> addCustomMapsToProfile(List<String> platformNameList, String profileName, String mapNameList, String languageCode, String actualSelectedProfile, StringBuffer success, StringBuffer errors);
    CustomMapModDto importCustomMapModFromServer(List<String> platformNameList, String mapNameLabel, Long idWorkShop, String commentary, List<String> selectedProfileNameList, StringBuffer success, StringBuffer errors);
    OfficialMapDto importOfficialMapFromServer(List<String> platformNameList, String officialMapName, List<String> selectedProfileNameList, String actualSelectedProfile, String mapNameLabel, List<ImportMapResultToDisplay> importMapResultToDisplayList);
    Optional<AbstractMap> findMapByName(String mapName) throws SQLException;
    Optional<PlatformProfileMapDto> findPlatformProfileMapDtoByNames(String platformName, String profileName, String mapName) throws SQLException;
    Optional<PlatformProfileMap> findPlatformProfileMapByNames(String platformName, String profileName, String mapName) throws SQLException;
    List<PlatformProfileMapDto> listPlatformProfileMaps(String platformName, String profileName) throws SQLException;
    String[] getMapNameAndUrlImage(Long idWorkShop) throws Exception;
}
