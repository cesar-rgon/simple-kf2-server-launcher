package stories.maps;

import dtos.AbstractMapDto;
import dtos.PlatformDto;
import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import entities.AbstractMap;
import entities.PlatformProfileMap;
import pojos.ImportMapResultToDisplay;
import pojos.MapToDisplay;
import pojos.PlatformProfileMapToImport;
import pojos.PlatformProfileToDisplay;
import pojos.kf2factory.Kf2Common;
import stories.addcustommapstoprofile.AddCustomMapsToProfileFacadeResult;
import stories.listplatformprofilemap.ListPlatformProfileMapFacadeResult;
import stories.mapsinitialize.MapsInitializeFacadeResult;
import stories.prepareimportmapsfromserver.PrepareImportMapsFromServerFacadeResult;

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
    PrepareImportMapsFromServerFacadeResult prepareImportMapsFromServer(String profileName) throws Exception;
    List<MapToDisplay> getNotPresentOfficialMapList(List<String> officialMapNameList, String platformName, String profileName) throws Exception;
    String findPropertyValue(String propFileRelativePath, String propKey, String profileParam, String platformParam) throws Exception;
    List<ImportMapResultToDisplay> importOfficialMapsFromServer(List<PlatformProfileMapToImport> ppmToImportList, String profileName) throws Exception;
    List<ImportMapResultToDisplay> importCustomMapsModsFromServer(List<PlatformProfileMapToImport> ppmToImportList, String profileName) throws Exception;
    void setConfigPropertyValue(String key, String value) throws Exception;
    void runServer(String platformName, String actualSelectedProfileName, String actualSelectedLanguage) throws Exception;

}
