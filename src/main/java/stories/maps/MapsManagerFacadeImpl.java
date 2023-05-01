package stories.maps;

import dtos.AbstractMapDto;
import dtos.PlatformDto;
import dtos.PlatformProfileMapDto;
import dtos.ProfileDto;
import entities.AbstractMap;
import entities.PlatformProfileMap;
import framework.AbstractManagerFacade;
import pojos.MapToDisplay;
import pojos.PlatformProfileMapToImport;
import pojos.PlatformProfileToDisplay;
import pojos.kf2factory.Kf2Common;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.mapsinitialize.MapsInitializeFacade;
import stories.mapsinitialize.MapsInitializeFacadeImpl;
import stories.mapsinitialize.MapsInitializeFacadeResult;
import stories.mapsinitialize.MapsInitializeModelContext;
import stories.listplatformprofilemap.ListPlatformProfileMapFacade;
import stories.listplatformprofilemap.ListPlatformProfileMapFacadeImpl;
import stories.listplatformprofilemap.ListPlatformProfileMapFacadeResult;
import stories.listplatformprofilemap.ListPlatformProfileMapModelContext;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
    public boolean isCorrectInstallationFolder(String platformName) {
        return false;
    }

    @Override
    public void addPlatformProfileMapList(List<PlatformProfileMap> platformProfileMapListToAdd, StringBuffer success, StringBuffer errors) throws SQLException {

    }

    @Override
    public AbstractMapDto deleteMapFromPlatformProfile(String platformName, String mapName, String profileName) throws Exception {
        return null;
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
    public List<PlatformProfileMapDto> addCustomMapsToProfile(List<String> platformNameList, String profileName, String mapNameList, String languageCode, String actualSelectedProfile, StringBuffer success, StringBuffer errors) {
        return null;
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
    public Optional<PlatformProfileMapDto> findPlatformProfileMapDtoByNames(String platformName, String profileName, String mapName) throws SQLException {
        return Optional.empty();
    }

    @Override
    public Optional<PlatformProfileMap> findPlatformProfileMapByNames(String platformName, String profileName, String mapName) throws SQLException {
        return Optional.empty();
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
    public String getPropertyValue(String propFileRelativePath, String propKey, String profileParam, String platformParam) throws Exception {
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
        return null;
    }

    @Override
    public Kf2Common getKf2Common(String platformName) throws Exception {
        return null;
    }

    @Override
    public ProfileDto findProfileDtoByName(String name) throws Exception {
        return null;
    }
}