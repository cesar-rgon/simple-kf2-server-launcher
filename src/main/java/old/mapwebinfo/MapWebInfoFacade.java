package old.mapwebinfo;

import dtos.CustomMapModDto;
import dtos.ProfileDto;
import pojos.PlatformProfile;

import java.sql.SQLException;
import java.util.List;

public interface MapWebInfoFacade {
    boolean isCorrectInstallationFolder(String installationFolder);
    CustomMapModDto createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, String mapName, String strUrlMapImage, List<String> profileNameList, StringBuffer success, StringBuffer errors) throws Exception;
    CustomMapModDto findMapOrModByIdWorkShop(Long idWorkShop) throws SQLException;
    void addProfilesToMap(List<String> platformNameList, String mapName, String strUrlMapImage, List<String> profileNameList, StringBuffer success, StringBuffer errors) throws Exception;
    List<PlatformProfile> getPlatformProfileListWithoutMap(Long idWorkShop) throws SQLException;
    List<ProfileDto> getAllProfileList() throws SQLException;
    int countPlatformsProfilesForMap(String customMapName);
}
