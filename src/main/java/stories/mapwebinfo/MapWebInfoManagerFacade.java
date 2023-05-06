package stories.mapwebinfo;

import dtos.CustomMapModDto;
import dtos.ProfileDto;
import stories.addplatformprofilestomap.AddPlatformProfilesToMapFacadeResult;
import stories.createcustommapfromworkshop.CreateCustomMapFromWorkshopFacadeResult;
import stories.getplatformprofilelistbyidworkshop.GetPlatformProfileListByIdworkshopFacadeResult;

import java.util.List;

public interface MapWebInfoManagerFacade {

    GetPlatformProfileListByIdworkshopFacadeResult execute() throws Exception;
    boolean isCorrectInstallationFolder(String platformName) throws Exception;
    List<ProfileDto> getAllProfileList() throws Exception;
    CustomMapModDto findMapOrModByIdWorkShop(Long idWorkShop) throws Exception;
    int countPlatformsProfilesForMap(String customMapName) throws Exception;
    CreateCustomMapFromWorkshopFacadeResult createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, String mapName, String strUrlMapImage, List<String> profileNameList) throws Exception;
    AddPlatformProfilesToMapFacadeResult addPlatformProfilesToMap(List<String> platformNameList, String mapName, String strUrlMapImage, List<String> profileNameList) throws Exception;

}
