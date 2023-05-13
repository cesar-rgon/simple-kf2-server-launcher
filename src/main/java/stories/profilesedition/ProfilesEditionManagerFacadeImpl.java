package stories.profilesedition;

import dtos.ProfileDto;
import entities.Profile;
import framework.AbstractManagerFacade;
import framework.EmptyFacadeResult;
import framework.EmptyModelContext;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import pojos.ProfileToDisplay;
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
import stories.deleteselectedprofile.DeleteSelectedProfileFacade;
import stories.deleteselectedprofile.DeleteSelectedProfileFacadeImpl;
import stories.deleteselectedprofile.DeleteSelectedProfileModelContext;
import stories.listallprofiles.ListAllProfilesFacade;
import stories.listallprofiles.ListAllProfilesFacadeImpl;
import stories.listallprofiles.ListAllProfilesFacadeResult;
import stories.updateprofilename.UpdateProfileNameFacade;
import stories.updateprofilename.UpdateProfileNameFacadeImpl;
import stories.updateprofilename.UpdateProfileNameFacadeResult;
import stories.updateprofilename.UpdateProfileNameModelContext;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ProfilesEditionManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, ListAllProfilesFacadeResult>
        implements ProfilesEditionManagerFacade {

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
    public String findConfigPropertyValue(String key) throws Exception {
        return null;
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

    }

    @Override
    public List<ProfileToDisplay> selectProfilesToBeExported(String message) throws SQLException {
        return null;
    }

    @Override
    public Optional<ButtonType> questionToImportEntitiesFromFile() throws Exception {
        return Optional.empty();
    }

    @Override
    public Properties importEntitiesFromFile(File file) throws Exception {
        return null;
    }

    @Override
    public List<Profile> questionToImportProfilesFromFile(Properties properties, String message) throws Exception {
        return null;
    }

    @Override
    public ObservableList<ProfileDto> importProfilesFromFile(List<Profile> selectedProfileList, Properties properties, StringBuffer errorMessage) throws Exception {
        return null;
    }

    @Override
    public void createConfigFolder(String profileName) throws SQLException {

    }

    @Override
    public ProfileDto findProfileDtoByName(String name) throws Exception {
        return null;
    }
}
