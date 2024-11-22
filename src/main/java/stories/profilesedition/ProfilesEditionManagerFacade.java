package stories.profilesedition;

import dtos.ProfileDto;
import entities.Profile;
import jakarta.persistence.EntityManager;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import pojos.ProfileToDisplay;
import stories.listallprofiles.ListAllProfilesFacadeResult;
import stories.mapsinitialize.MapsInitializeFacadeResult;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public interface ProfilesEditionManagerFacade {

    ListAllProfilesFacadeResult execute() throws Exception;
    String findPropertyValue(String propertyFilePath, String key) throws Exception;
    void setConfigPropertyValue(String key, String value) throws Exception;
    void removeConfigProperty(String key) throws Exception;
    ProfileDto createNewProfile(String profileName) throws Exception;
    ProfileDto cloneSelectedProfile(String profileName, String newProfileName) throws Exception;
    void deleteSelectedProfile(String profileName) throws Exception;
    ProfileDto updateChangedProfile(String oldProfileName, String newProfileName) throws Exception;
    List<ProfileToDisplay> selectProfilesToBeExported() throws Exception;
    void exportProfilesToFile(List<ProfileToDisplay> profilesToExportDto, File file) throws Exception;
    Properties importEntitiesFromFile(File file) throws Exception;
    List<ProfileToDisplay> questionToImportProfilesFromFile(Properties properties) throws Exception;
    Profile getProfileFromFile(int profileIndex, Properties properties) throws Exception;
    ObservableList<ProfileDto> importProfilesFromFile(List<Profile> selectedProfileList, Properties properties, StringBuffer errorMessage) throws Exception;
    String findConfigPropertyValue(String key) throws Exception;
    ProfileDto loadDefaultValues() throws Exception;
}
