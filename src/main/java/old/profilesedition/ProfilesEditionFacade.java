package old.profilesedition;

import dtos.ProfileDto;
import entities.Profile;
import javafx.scene.control.ButtonType;
import javafx.collections.ObservableList;
import pojos.ProfileToDisplay;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public interface ProfilesEditionFacade {
    ObservableList<ProfileDto> listAllProfiles() throws SQLException;
    ProfileDto createNewProfile(String profileName) throws Exception;
    boolean deleteSelectedProfile(String profileName) throws Exception;
    ProfileDto updateChangedProfile(String oldProfileName, String newProfileName) throws Exception;
    String findConfigPropertyValue(String key) throws Exception;
    ProfileDto cloneSelectedProfile(String profileName, String newProfileName) throws Exception;
    void exportProfilesToFile(List<ProfileToDisplay> profilesToExportDto, File file) throws Exception;
    List<ProfileToDisplay> selectProfilesToBeExported(String message) throws SQLException;
    Optional<ButtonType> questionToImportEntitiesFromFile() throws Exception;
    Properties importEntitiesFromFile(File file) throws Exception;
    List<Profile> questionToImportProfilesFromFile(Properties properties, String message) throws Exception;
    ObservableList<ProfileDto> importProfilesFromFile(List<Profile> selectedProfileList, Properties properties, StringBuffer errorMessage) throws Exception;
    void createConfigFolder(String profileName) throws SQLException;
    ProfileDto findProfileDtoByName(String name) throws Exception;
}
