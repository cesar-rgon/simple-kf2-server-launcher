package stories.maps;

import dtos.AbstractMapDto;
import dtos.PlatformDto;
import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import entities.AbstractMap;
import entities.PlatformProfileMap;
import pojos.MapToDisplay;
import pojos.PlatformProfileMapToImport;
import pojos.PlatformProfileToDisplay;
import pojos.kf2factory.Kf2Common;
import stories.addcustommapstoprofile.AddCustomMapsToProfileFacadeResult;
import stories.listplatformprofilemap.ListPlatformProfileMapFacadeResult;
import stories.mapsinitialize.MapsInitializeFacadeResult;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface MapsManagerFacade {

    MapsInitializeFacadeResult execute() throws Exception;
    ListPlatformProfileMapFacadeResult getPlatformProfileMapList(String profileName) throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;
    AddCustomMapsToProfileFacadeResult addCustomMapsToProfile(List<String> platformNameList, String profileName, String mapNameList, String languageCode, String actualSelectedProfile) throws Exception;
    Optional<PlatformProfileMapDto> findPlatformProfileMapDtoByName(String platformName, String profileName, String mapName) throws Exception;
    boolean isCorrectInstallationFolder(String platformName) throws Exception;
    AbstractMapDto deleteMapFromPlatformProfile(String platformName, String mapName, String profileName) throws Exception;

    // ------------------------------------------------------------------------

    void setConfigPropertyValue(String key, String value) throws Exception;
    public void addPlatformProfileMapList(List<PlatformProfileMap> platformProfileMapListToAdd, StringBuffer success, StringBuffer errors) throws SQLException;
    void unselectProfileMap(String profileName) throws Exception;
    List<PlatformProfileToDisplay> selectProfilesToImport(String defaultSelectedProfileName) throws Exception;
    String runServer(String platformName, String profileName) throws Exception;
    PlatformProfileMapToImport importCustomMapModFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName) throws Exception;
    PlatformProfileMapToImport importOfficialMapFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName) throws Exception;
    Optional<AbstractMap> findMapByName(String mapName) throws Exception;
    String[] getMapNameAndUrlImage(Long idWorkShop) throws Exception;
    List<PlatformDto> listAllPlatforms() throws SQLException;
    String getPropertyValue(String propFileRelativePath, String propKey, String profileParam, String platformParam) throws Exception;
    AbstractMapDto getMapByIdWorkShop(Long idWorkShop) throws SQLException;
    AbstractMapDto getOfficialMapByName(String mapName) throws Exception ;
    List<MapToDisplay> getNotPresentOfficialMapList(List<String> officialMapNameList, String platformName, String profileName) throws Exception;
    Kf2Common getKf2Common(String platformName) throws Exception;
    ProfileDto findProfileDtoByName(String name) throws Exception;
}
