package stories.mapwebinfo;

import dtos.CustomMapModDto;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import stories.addplatformprofilestomap.AddPlatformProfilesToMapFacadeResult;
import stories.createcustommapfromworkshop.CreateCustomMapFromWorkshopFacadeResult;
import stories.getplatformprofilelistwithoutmap.GetPlatformProfileListWithoutMapFacadeResult;

import java.util.List;

public interface MapWebInfoManagerFacade {

    GetPlatformProfileListWithoutMapFacadeResult execute() throws Exception;
    boolean isCorrectInstallationFolder(String platformName) throws Exception;
    CustomMapModDto findMapOrModByIdWorkShop(Long idWorkShop) throws Exception;
    int countPlatformsProfilesForMap(String customMapName) throws Exception;
    CreateCustomMapFromWorkshopFacadeResult createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, String mapName, String strUrlMapImage, boolean isMap, List<String> profileNameList) throws Exception;
    AddPlatformProfilesToMapFacadeResult addPlatformProfilesToMap(List<String> platformNameList, String mapName, String strUrlMapImage, List<String> profileNameList) throws Exception;
    List<PlatformProfileToDisplay> getSelectedPlatformProfileList(List<PlatformProfile> platformProfileList) throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;
}
