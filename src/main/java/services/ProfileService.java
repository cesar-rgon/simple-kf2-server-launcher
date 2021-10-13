package services;

import entities.Profile;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProfileService extends Kf2Service<Profile> {

    Optional<Profile> findProfileByCode(String profileName) throws SQLException;
    List<Profile> listAllProfiles() throws SQLException;
    boolean deleteProfile(Profile profile, String installationFolder) throws Exception;
    Profile cloneProfile(Profile profileToBeCloned, String newProfileName) throws Exception;
    void exportProfilesToFile(List<Profile> profilesToExport, File file) throws Exception;
    List<Profile> importProfilesFromFile(File file, String message, StringBuffer errorMessage) throws Exception;

}
