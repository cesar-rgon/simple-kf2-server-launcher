package old.mapsedition;

import dtos.*;
import entities.AbstractMap;
import entities.PlatformProfileMap;
import javafx.collections.ObservableList;
import pojos.MapToDisplay;
import pojos.PlatformProfileMapToImport;
import pojos.PlatformProfileToDisplay;
import pojos.kf2factory.Kf2Common;

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
    void unselectProfileMap(String profileName) throws Exception;
    List<PlatformProfileToDisplay> selectProfilesToImport(String defaultSelectedProfileName) throws Exception;
    String runServer(String platformName, String profileName) throws Exception;
    List<PlatformProfileMapDto> addCustomMapsToProfile(List<String> platformNameList, String profileName, String mapNameList, String languageCode, String actualSelectedProfile, StringBuffer success, StringBuffer errors);
    PlatformProfileMapToImport importCustomMapModFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName) throws Exception;
    PlatformProfileMapToImport importOfficialMapFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName) throws Exception;
    Optional<AbstractMap> findMapByName(String mapName) throws Exception;
    Optional<PlatformProfileMapDto> findPlatformProfileMapDtoByNames(String platformName, String profileName, String mapName) throws SQLException;
    Optional<PlatformProfileMap> findPlatformProfileMapByNames(String platformName, String profileName, String mapName) throws SQLException;
    List<PlatformProfileMapDto> listPlatformProfileMaps(String platformName, String profileName) throws Exception;
    String[] getMapNameAndUrlImage(Long idWorkShop) throws Exception;
    List<PlatformDto> listAllPlatforms() throws SQLException;
    String getPropertyValue(String propFileRelativePath, String propKey, String profileParam, String platformParam) throws Exception;
    AbstractMapDto getMapByIdWorkShop(Long idWorkShop) throws SQLException;
    AbstractMapDto getOfficialMapByName(String mapName) throws Exception ;
    List<MapToDisplay> getNotPresentOfficialMapList(List<String> officialMapNameList, String platformName, String profileName) throws Exception;
    Kf2Common getKf2Common(String platformName) throws Exception;
    ProfileDto findProfileDtoByName(String name) throws Exception;
}
