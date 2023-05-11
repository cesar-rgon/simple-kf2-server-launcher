package stories.maps;

import dtos.AbstractMapDto;
import dtos.PlatformDto;
import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import entities.AbstractMap;
import entities.PlatformProfileMap;
import framework.AbstractManagerFacade;
import pojos.ImportMapResultToDisplay;
import pojos.MapToDisplay;
import pojos.PlatformProfileMapToImport;
import pojos.PlatformProfileToDisplay;
import pojos.kf2factory.Kf2Common;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.addcustommapstoprofile.AddCustomMapsToProfileFacade;
import stories.addcustommapstoprofile.AddCustomMapsToProfileFacadeImpl;
import stories.addcustommapstoprofile.AddCustomMapsToProfileFacadeResult;
import stories.addcustommapstoprofile.AddCustomMapsToProfileModelContext;
import stories.deletemapfromplatformprofile.DeleteMapFromPlatformProfileFacade;
import stories.deletemapfromplatformprofile.DeleteMapFromPlatformProfileFacadeImpl;
import stories.deletemapfromplatformprofile.DeleteMapFromPlatformProfileFacadeResult;
import stories.deletemapfromplatformprofile.DeleteMapFromPlatformProfileModelContext;
import stories.findplatformprofilemapbynames.FindPlatformProfileMapByNameFacade;
import stories.findplatformprofilemapbynames.FindPlatformProfileMapByNameFacadeImpl;
import stories.findplatformprofilemapbynames.FindPlatformProfileMapByNameFacadeResult;
import stories.findplatformprofilemapbynames.FindPlatformProfileMapByNameModelContext;
import stories.importofficialmapsfromserver.ImportOfficialMapsFromServerFacade;
import stories.importofficialmapsfromserver.ImportOfficialMapsFromServerFacadeImpl;
import stories.importofficialmapsfromserver.ImportOfficialMapsFromServerFacadeResult;
import stories.importofficialmapsfromserver.ImportOfficialMapsFromServerModelContext;
import stories.installationfolder.InstallationFolderFacade;
import stories.installationfolder.InstallationFolderFacadeImpl;
import stories.installationfolder.InstallationFolderFacadeResult;
import stories.installationfolder.InstallationFolderModelContext;
import stories.listnotpresentofficialmap.ListNotPresentOfficialMapFacade;
import stories.listnotpresentofficialmap.ListNotPresentOfficialMapFacadeImpl;
import stories.listnotpresentofficialmap.ListNotPresentOfficialMapFacadeModelContext;
import stories.listnotpresentofficialmap.ListNotPresentOfficialMapFacadeResult;
import stories.mapsinitialize.MapsInitializeFacade;
import stories.mapsinitialize.MapsInitializeFacadeImpl;
import stories.mapsinitialize.MapsInitializeFacadeResult;
import stories.mapsinitialize.MapsInitializeModelContext;
import stories.listplatformprofilemap.ListPlatformProfileMapFacade;
import stories.listplatformprofilemap.ListPlatformProfileMapFacadeImpl;
import stories.listplatformprofilemap.ListPlatformProfileMapFacadeResult;
import stories.listplatformprofilemap.ListPlatformProfileMapModelContext;
import stories.prepareimportmapsfromserver.PrepareImportMapsFromServerFacade;
import stories.prepareimportmapsfromserver.PrepareImportMapsFromServerFacadeImpl;
import stories.prepareimportmapsfromserver.PrepareImportMapsFromServerFacadeResult;
import stories.prepareimportmapsfromserver.PrepareImportMapsFromServerModelContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class MapsManagerFacadeImpl
        extends AbstractManagerFacade<MapsInitializeModelContext, MapsInitializeFacadeResult>
        implements MapsManagerFacade {

    public MapsManagerFacadeImpl(MapsInitializeModelContext mapsInitializeModelContext) {
        super(mapsInitializeModelContext, MapsInitializeFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(MapsInitializeModelContext mapsInitializeModelContext) throws Exception {
        return true;
    }

    @Override
    protected MapsInitializeFacadeResult internalExecute(MapsInitializeModelContext mapsInitializeModelContext) throws Exception {
        MapsInitializeFacade mapsInitializeFacade = new MapsInitializeFacadeImpl(mapsInitializeModelContext);
        return mapsInitializeFacade.execute();
    }

    @Override
    public ListPlatformProfileMapFacadeResult getPlatformProfileMapList(String profileName) throws Exception {
        ListPlatformProfileMapModelContext listPlatformProfileMapModelContext = new ListPlatformProfileMapModelContext(
                profileName
        );
        ListPlatformProfileMapFacade listPlatformProfileMapFacade = new ListPlatformProfileMapFacadeImpl(listPlatformProfileMapModelContext);
        return listPlatformProfileMapFacade.execute();
    }

    @Override
    public String findPropertyValue(String propertyFilePath, String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue(propertyFilePath, key);
    }


    @Override
    public void setConfigPropertyValue(String key, String value) throws Exception {

    }

    @Override
    public boolean isCorrectInstallationFolder(String platformName) throws Exception {
        InstallationFolderModelContext installationFolderModelContext = new InstallationFolderModelContext(
                platformName
        );
        InstallationFolderFacade installationFolderFacade = new InstallationFolderFacadeImpl(installationFolderModelContext);
        InstallationFolderFacadeResult result = installationFolderFacade.execute();
        return result.isCorrectInstallationFolder();
    }

    @Override
    public void addPlatformProfileMapList(List<PlatformProfileMap> platformProfileMapListToAdd, StringBuffer success, StringBuffer errors) throws SQLException {

    }

    @Override
    public AbstractMapDto deleteMapFromPlatformProfile(String platformName, String mapName, String profileName) throws Exception {
        DeleteMapFromPlatformProfileModelContext deleteMapFromPlatformProfileModelContext = new DeleteMapFromPlatformProfileModelContext(
                mapName,
                platformName,
                profileName
        );
        DeleteMapFromPlatformProfileFacade deleteMapFromPlatformProfileFacade = new DeleteMapFromPlatformProfileFacadeImpl(deleteMapFromPlatformProfileModelContext);
        DeleteMapFromPlatformProfileFacadeResult result = deleteMapFromPlatformProfileFacade.execute();
        return result.getMapDto();
    }

    @Override
    public void unselectProfileMap(String profileName) throws Exception {

    }

    @Override
    public List<PlatformProfileToDisplay> selectProfilesToImport(String defaultSelectedProfileName) throws Exception {
        return null;
    }

    @Override
    public String runServer(String platformName, String profileName) throws Exception {
        return null;
    }

    @Override
    public AddCustomMapsToProfileFacadeResult addCustomMapsToProfile(List<String> platformNameList, String profileName, String mapNameList, String languageCode, String actualSelectedProfile) throws Exception {
        AddCustomMapsToProfileModelContext addCustomMapsToProfileModelContext = new AddCustomMapsToProfileModelContext(
                platformNameList,
                profileName,
                mapNameList,
                languageCode,
                actualSelectedProfile
        );
        AddCustomMapsToProfileFacade addCustomMapsToProfileFacade = new AddCustomMapsToProfileFacadeImpl(addCustomMapsToProfileModelContext);
        return addCustomMapsToProfileFacade.execute();
    }

    @Override
    public PlatformProfileMapToImport importCustomMapModFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName) throws Exception {
        return null;
    }

    @Override
    public PlatformProfileMapToImport importOfficialMapFromServer(PlatformProfileMapToImport ppmToImport, String selectedProfileName) throws Exception {
        return null;
    }

    @Override
    public Optional<AbstractMap> findMapByName(String mapName) throws Exception {
        return Optional.empty();
    }

    @Override
    public Optional<PlatformProfileMapDto> findPlatformProfileMapDtoByName(String platformName, String profileName, String mapName) throws Exception {
        FindPlatformProfileMapByNameModelContext findPlatformProfileMapByNameModelContext = new FindPlatformProfileMapByNameModelContext(
                platformName,
                profileName,
                mapName
        );
        FindPlatformProfileMapByNameFacade findPlatformProfileMapByNameFacade = new FindPlatformProfileMapByNameFacadeImpl(findPlatformProfileMapByNameModelContext);
        FindPlatformProfileMapByNameFacadeResult result = findPlatformProfileMapByNameFacade.execute();
        return result.getPlatformProfileMapDtoOptional();
    }

    @Override
    public String[] getMapNameAndUrlImage(Long idWorkShop) throws Exception {
        return new String[0];
    }

    @Override
    public List<PlatformDto> listAllPlatforms() throws SQLException {
        return null;
    }

    @Override
    public AbstractMapDto getMapByIdWorkShop(Long idWorkShop) throws SQLException {
        return null;
    }

    @Override
    public AbstractMapDto getOfficialMapByName(String mapName) throws Exception {
        return null;
    }

    @Override
    public List<MapToDisplay> getNotPresentOfficialMapList(List<String> officialMapNameList, String platformName, String profileName) throws Exception {
        ListNotPresentOfficialMapFacadeModelContext listNotPresentOfficialMapFacadeModelContext = new ListNotPresentOfficialMapFacadeModelContext(
                officialMapNameList,
                platformName,
                profileName
        );
        ListNotPresentOfficialMapFacade listNotPresentOfficialMapFacade = new ListNotPresentOfficialMapFacadeImpl(listNotPresentOfficialMapFacadeModelContext);
        ListNotPresentOfficialMapFacadeResult result = listNotPresentOfficialMapFacade.execute();
        return result.getNotPresentOfficialMapList();
    }

    @Override
    public Kf2Common getKf2Common(String platformName) throws Exception {
        return null;
    }

    @Override
    public ProfileDto findProfileDtoByName(String name) throws Exception {
        return null;
    }

    @Override
    public PrepareImportMapsFromServerFacadeResult prepareImportMapsFromServer(String profileName) throws Exception {
        PrepareImportMapsFromServerModelContext prepareImportMapsFromServerModelContext = new PrepareImportMapsFromServerModelContext(
                profileName
        );
        PrepareImportMapsFromServerFacade prepareImportMapsFromServerFacade = new PrepareImportMapsFromServerFacadeImpl(prepareImportMapsFromServerModelContext);
        return prepareImportMapsFromServerFacade.execute();
    }

    @Override
    public String findPropertyValue(String propFileRelativePath, String propKey, String profileParam, String platformParam) throws Exception {
        Properties prop = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("./" + propFileRelativePath);
        } catch (FileNotFoundException e) {
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileRelativePath);
        }
        prop.load(inputStream);
        inputStream.close();

        return MessageFormat.format(prop.getProperty(propKey), profileParam, platformParam);
    }

    @Override
    public List<ImportMapResultToDisplay> importOfficialMapsFromServer(List<PlatformProfileMapToImport> ppmToImportList, String profileName) throws Exception {
        ImportOfficialMapsFromServerModelContext importOfficialMapsFromServerModelContext = new ImportOfficialMapsFromServerModelContext(
                ppmToImportList,
                profileName
        );
        ImportOfficialMapsFromServerFacade importOfficialMapsFromServerFacade = new ImportOfficialMapsFromServerFacadeImpl(importOfficialMapsFromServerModelContext);
        ImportOfficialMapsFromServerFacadeResult result = importOfficialMapsFromServerFacade.execute();
        return result.getImportMapResultToDisplayList();
    }

}
