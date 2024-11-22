package stories.profilesedition;

import dtos.ProfileDto;
import entities.Profile;
import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.ProfileToDisplay;
import services.GameTypeServiceImpl;
import services.ProfileServiceImpl;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.cloneselectedprofile.CloneSelectedProfileFacade;
import stories.cloneselectedprofile.CloneSelectedProfileFacadeImpl;
import stories.cloneselectedprofile.CloneSelectedProfileFacadeResult;
import stories.cloneselectedprofile.CloneSelectedProfileModelContext;
import stories.createnewprofile.CreateNewProfileFacade;
import stories.createnewprofile.CreateNewProfileFacadeImpl;
import stories.createnewprofile.CreateNewProfileFacadeResult;
import stories.createnewprofile.CreateNewProfileModelContext;
import stories.deleteallitems.DeleteAllItemsFacade;
import stories.deleteallitems.DeleteAllItemsFacadeImpl;
import stories.deleteselectedprofile.DeleteSelectedProfileFacade;
import stories.deleteselectedprofile.DeleteSelectedProfileFacadeImpl;
import stories.deleteselectedprofile.DeleteSelectedProfileModelContext;
import stories.entitydescriptions.EntityDescriptionsFacade;
import stories.entitydescriptions.EntityDescriptionsFacadeImpl;
import stories.entitydescriptions.EntityDescriptionsFacadeResult;
import stories.entitydescriptions.EntityDescriptionsModelContext;
import stories.exportprofilestofile.ExportProfilesToFileFacade;
import stories.exportprofilestofile.ExportProfilesToFileFacadeImpl;
import stories.exportprofilestofile.ExportProfilesToFileModelContext;
import stories.getprofilefromfile.GetProfileFromFileFacade;
import stories.getprofilefromfile.GetProfileFromFileFacadeImpl;
import stories.getprofilefromfile.GetProfileFromFileFacadeResult;
import stories.getprofilefromfile.GetProfileFromFileModelContext;
import stories.importentitiesfromfile.ImportEntitiesFromFileFacade;
import stories.importentitiesfromfile.ImportEntitiesFromFileFacadeImpl;
import stories.importentitiesfromfile.ImportEntitiesFromFileFacadeResult;
import stories.importentitiesfromfile.ImportEntitiesFromFileModelContext;
import stories.importprofilesfromfile.ImportProfilesFromFileFacade;
import stories.importprofilesfromfile.ImportProfilesFromFileFacadeImpl;
import stories.importprofilesfromfile.ImportProfilesFromFileFacadeResult;
import stories.importprofilesfromfile.ImportProfilesFromFileModelContext;
import stories.listallprofiles.ListAllProfilesFacade;
import stories.listallprofiles.ListAllProfilesFacadeImpl;
import stories.listallprofiles.ListAllProfilesFacadeResult;
import stories.selectprofilestobeexported.SelectProfilesToBeExportedFacade;
import stories.selectprofilestobeexported.SelectProfilesToBeExportedFacadeImpl;
import stories.selectprofilestobeexported.SelectProfilesToBeExportedFacadeResult;
import stories.updateprofilename.UpdateProfileNameFacade;
import stories.updateprofilename.UpdateProfileNameFacadeImpl;
import stories.updateprofilename.UpdateProfileNameFacadeResult;
import stories.updateprofilename.UpdateProfileNameModelContext;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ProfilesEditionManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, ListAllProfilesFacadeResult>
        implements ProfilesEditionManagerFacade {

    private static final Logger logger = LogManager.getLogger(ProfilesEditionManagerFacadeImpl.class);

    public ProfilesEditionManagerFacadeImpl() {
        super(new EmptyModelContext(), ListAllProfilesFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected ListAllProfilesFacadeResult internalExecute(EmptyModelContext facadeModelContext) throws Exception {
        ListAllProfilesFacade listAllProfilesFacade = new ListAllProfilesFacadeImpl();
        return listAllProfilesFacade.execute();
    }

    @Override
    public String findPropertyValue(String propertyFilePath, String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue(propertyFilePath, key);
    }

    @Override
    public void setConfigPropertyValue(String key, String value) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty("properties/config.properties", key, value);
    }

    @Override
    public void removeConfigProperty(String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.removeProperty("properties/config.properties", key);
    }

    @Override
    public ProfileDto createNewProfile(String profileName) throws Exception {
        CreateNewProfileModelContext createNewProfileModelContext = new CreateNewProfileModelContext(
                profileName
        );
        CreateNewProfileFacade createNewProfileFacade = new CreateNewProfileFacadeImpl(createNewProfileModelContext);
        CreateNewProfileFacadeResult result = createNewProfileFacade.execute();
        return result.getNewProfile();
    }

    @Override
    public void deleteSelectedProfile(String profileName) throws Exception {
        DeleteSelectedProfileModelContext deleteSelectedProfileModelContext = new DeleteSelectedProfileModelContext(
                profileName
        );
        DeleteSelectedProfileFacade deleteSelectedProfileFacade = new DeleteSelectedProfileFacadeImpl(deleteSelectedProfileModelContext);
        deleteSelectedProfileFacade.execute();
    }

    @Override
    public ProfileDto updateChangedProfile(String oldProfileName, String newProfileName) throws Exception {
        UpdateProfileNameModelContext updateProfileNameModelContext = new UpdateProfileNameModelContext(
                oldProfileName,
                newProfileName
        );
        UpdateProfileNameFacade updateProfileNameFacade = new UpdateProfileNameFacadeImpl(updateProfileNameModelContext);
        UpdateProfileNameFacadeResult result = updateProfileNameFacade.execute();
        return result.getUpdatedProfile();
    }

    @Override
    public ProfileDto cloneSelectedProfile(String profileName, String newProfileName) throws Exception {
        CloneSelectedProfileModelContext cloneSelectedProfileModelContext = new CloneSelectedProfileModelContext(
                profileName,
                newProfileName
        );
        CloneSelectedProfileFacade cloneSelectedProfileFacade = new CloneSelectedProfileFacadeImpl(cloneSelectedProfileModelContext);
        CloneSelectedProfileFacadeResult result = cloneSelectedProfileFacade.execute();
        return result.getClonedProfile();
    }

    @Override
    public void exportProfilesToFile(List<ProfileToDisplay> profilesToExportDto, File file) throws Exception {
        ExportProfilesToFileModelContext exportProfilesToFileModelContext = new ExportProfilesToFileModelContext(
                profilesToExportDto,
                file
        );
        ExportProfilesToFileFacade exportProfilesToFileFacade = new ExportProfilesToFileFacadeImpl(exportProfilesToFileModelContext);
        exportProfilesToFileFacade.execute();
    }

    @Override
    public List<ProfileToDisplay> selectProfilesToBeExported() throws Exception {
        SelectProfilesToBeExportedFacade selectProfilesToBeExportedFacade = new SelectProfilesToBeExportedFacadeImpl();
        SelectProfilesToBeExportedFacadeResult result = selectProfilesToBeExportedFacade.execute();
        return result.getProfileToDisplayList();
    }

    @Override
    public Properties importEntitiesFromFile(File file) throws Exception {
        ImportEntitiesFromFileModelContext importEntitiesFromFileModelContext = new ImportEntitiesFromFileModelContext(
                file
        );
        ImportEntitiesFromFileFacade importEntitiesFromFileFacade = new ImportEntitiesFromFileFacadeImpl(importEntitiesFromFileModelContext);
        ImportEntitiesFromFileFacadeResult result = importEntitiesFromFileFacade.execute();
        return result.getProperties();
    }

    @Override
    public List<ProfileToDisplay> questionToImportProfilesFromFile(Properties properties) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();

        int numberOfProfiles = Integer.parseInt(properties.getProperty("exported.profiles.number"));
        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        List<ProfileToDisplay> profileToDisplayList = new ArrayList<ProfileToDisplay>();

        for (int profileIndex = 1; profileIndex <= numberOfProfiles; profileIndex++) {
            String profileName = properties.getProperty("exported.profile" + profileIndex + ".name");
            try {
                String gameTypeCode = properties.getProperty("exported.profile" + profileIndex + ".gameType");
                String difficultyCode = properties.getProperty("exported.profile" + profileIndex + ".difficulty");
                String lengthCode = properties.getProperty("exported.profile" + profileIndex + ".length");

                EntityDescriptionsModelContext entityDescriptionsModelContext = new EntityDescriptionsModelContext(
                        languageCode,
                        gameTypeCode,
                        difficultyCode,
                        lengthCode
                );
                EntityDescriptionsFacade entityDescriptionsFacade = new EntityDescriptionsFacadeImpl(entityDescriptionsModelContext);
                EntityDescriptionsFacadeResult result = entityDescriptionsFacade.execute();
                String mapName = properties.getProperty("exported.profile" + profileIndex + ".map");

                ProfileToDisplay profileToDisplay = new ProfileToDisplay(profileIndex, profileName, result.getGametypeDescription(), mapName, result.getDifficultyDescription(), result.getLengthDescription());
                profileToDisplay.setSelected(true);
                profileToDisplayList.add(profileToDisplay);

            } catch (Exception e) {
                logger.error("Error reading the profile " + profileName + " from exported file", e);
            }
        }

        return profileToDisplayList;
    }

    @Override
    public Profile getProfileFromFile(int profileIndex, Properties properties) throws Exception {
        GetProfileFromFileModelContext getProfileFromFileModelContext = new GetProfileFromFileModelContext(
                profileIndex,
                properties
        );
        GetProfileFromFileFacade getProfileFromFileFacade = new GetProfileFromFileFacadeImpl(getProfileFromFileModelContext);
        GetProfileFromFileFacadeResult result = getProfileFromFileFacade.execute();
        return result.getProfileFromFile();
    }

    @Override
    public ObservableList<ProfileDto> importProfilesFromFile(List<Profile> selectedProfileList, Properties properties, StringBuffer errorMessage) throws Exception {
        ImportProfilesFromFileModelContext importProfilesFromFileModelContext = new ImportProfilesFromFileModelContext(
                selectedProfileList,
                properties,
                errorMessage
        );
        ImportProfilesFromFileFacade importProfilesFromFileFacade = new ImportProfilesFromFileFacadeImpl(importProfilesFromFileModelContext);
        ImportProfilesFromFileFacadeResult result = importProfilesFromFileFacade.execute();
        return result.getImportedProfilesFromFile();
    }

    @Override
    public String findConfigPropertyValue(String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue("properties/config.properties", key);
    }

    @Override
    public ProfileDto loadDefaultValues() throws Exception {
        DeleteAllItemsFacade deleteAllItemsFacade = new DeleteAllItemsFacadeImpl<>(ProfileServiceImpl.class);
        deleteAllItemsFacade.execute();
        return createNewProfile("Default");
    }
}
