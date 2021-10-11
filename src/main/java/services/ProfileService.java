package services;

import entities.Profile;

import java.io.File;
import java.util.List;

public interface ProfileService extends Kf2Service<Profile> {

    boolean deleteProfile(Profile profile, String installationFolder) throws Exception;
    Profile cloneProfile(Profile profileToBeCloned, String newProfileName) throws Exception;
    void exportProfilesToFile(List<Profile> profilesToExport, File file) throws Exception;
    List<Profile> importProfilesFromFile(File file, String message, StringBuffer errorMessage) throws Exception;

}
