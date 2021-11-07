package services;

import entities.Language;
import entities.Profile;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public interface ProfileService extends AbstractExtendedService<Profile> {

    Optional<Profile> findProfileByCode(String profileName) throws SQLException;
    List<Profile> listAllProfiles() throws SQLException;
    boolean deleteProfile(Profile profile, String installationFolder) throws Exception;
    Profile cloneProfile(Profile profileToBeCloned, String newProfileName) throws Exception;
    void exportProfilesToFile(List<Profile> profilesToExport, File file) throws Exception;
    void importGameTypesFromFile(Properties properties, List<Language> languageList) throws SQLException;
    void importDifficultiesFromFile(Properties properties, List<Language> languageList) throws SQLException;
    void importLengthsFromFile(Properties properties, List<Language> languageList) throws SQLException;
    void importMaxPlayersFromFile(Properties properties, List<Language> languageList) throws SQLException;
    List<Profile> selectProfilesToBeImported(Properties properties, String message) throws Exception;
    List<Profile> importProfilesFromFile(List<Profile> selectedProfileList, Properties properties, StringBuffer errorMessage);
}
