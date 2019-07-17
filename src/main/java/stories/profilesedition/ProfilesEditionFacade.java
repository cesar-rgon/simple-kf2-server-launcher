package stories.profilesedition;

import dtos.ProfileDto;
import entities.Profile;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface ProfilesEditionFacade {
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    ProfileDto createNewProfile(String profileName) throws Exception;
    boolean deleteSelectedProfile(String profileName) throws SQLException;
    ProfileDto updateChangedProfile(String oldProfileName, String newProfileName) throws SQLException;
    String findPropertyValue(String key) throws SQLException;
}
