package stories.profilesedition;

import dtos.ProfileDto;
import entities.Profile;
import javafx.collections.ObservableList;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public interface ProfilesEditionFacade {
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    ProfileDto createNewProfile(String profileName) throws Exception;
    boolean deleteSelectedProfile(String profileName, String installationFolder) throws Exception;
    ProfileDto updateChangedProfile(String oldProfileName, String newProfileName) throws SQLException;
    String findPropertyValue(String key) throws Exception;
    ProfileDto cloneSelectedProfile(String profileName, String newProfileName) throws SQLException;
    void exportProfilesToFile(List<ProfileDto> profiles, File file) throws Exception;
    ObservableList<ProfileDto> addProfilesToBeImportedFromFile(File file, String message, StringBuffer errorMessage, String installationFolder) throws Exception;
    List<ProfileDto> selectProfiles(String message) throws SQLException;
}
