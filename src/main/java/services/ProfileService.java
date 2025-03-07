package services;

import entities.Language;
import entities.Profile;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public interface ProfileService extends IService<Profile> {

    Optional<Profile> findProfileByCode(String profileName) throws Exception;
    List<Profile> listAllProfiles() throws SQLException;
    public boolean deleteProfile(Profile profile) throws Exception;
    Profile cloneProfile(Profile profileToBeCloned, String newProfileName) throws Exception;
    void exportProfilesToFile(List<Profile> profilesToExport, File file) throws Exception;
    void importGameTypesFromFile(Properties properties, List<Language> languageList) throws Exception;
    void importDifficultiesFromFile(Properties properties, List<Language> languageList) throws Exception;
    void importLengthsFromFile(Properties properties, List<Language> languageList) throws Exception;
    void importMaxPlayersFromFile(Properties properties, List<Language> languageList) throws Exception;
    List<Profile> prepareProfilesFromFile(Properties entitiesProperties) throws Exception;
    List<Profile> importProfilesFromFile(List<Profile> selectedProfileList, Properties properties, StringBuffer errorMessage, boolean hasToSelectMaps);
    List<Profile> getProfileListByNames(List<String> profileNameList, StringBuffer success, StringBuffer errors);
}
