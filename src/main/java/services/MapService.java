package services;

import entities.Map;
import entities.Profile;
import pojos.ProfileToDisplay;

import java.sql.SQLException;
import java.util.List;

public interface MapService extends Kf2Service<Map> {

    Map createMap(Map map) throws Exception;
    boolean addProfilesToMap(Map map, List<Profile> profileList) throws SQLException;
    Map deleteMap(Map map, Profile profile, String installationFolder) throws Exception;
    List<ProfileToDisplay> selectProfilesToImport(List<Profile> allProfiles, String defaultSelectedProfileName) throws Exception;

}
