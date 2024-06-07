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
import stories.downloadmaplistfromsteamcmd.DownloadMapListFromSteamCmdFacade;
import stories.downloadmaplistfromsteamcmd.DownloadMapListFromSteamCmdFacadeImpl;
import stories.downloadmaplistfromsteamcmd.DownloadMapListFromSteamCmdModelContext;
import stories.findplatformprofilemapbynames.FindPlatformProfileMapByNameFacade;
import stories.findplatformprofilemapbynames.FindPlatformProfileMapByNameFacadeImpl;
import stories.findplatformprofilemapbynames.FindPlatformProfileMapByNameFacadeResult;
import stories.findplatformprofilemapbynames.FindPlatformProfileMapByNameModelContext;
import stories.importcustommapsfromserver.ImportCustomMapsFromServerFacade;
import stories.importcustommapsfromserver.ImportCustomMapsFromServerFacadeImpl;
import stories.importcustommapsfromserver.ImportCustomMapsFromServerFacadeResult;
import stories.importcustommapsfromserver.ImportCustomMapsFromServerModelContext;
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
import stories.runservers.RunServersFacade;
import stories.runservers.RunServersFacadeImpl;
import stories.runservers.RunServersModelContext;
import stories.updatemapscycleflaginmaplist.UpdateMapsCycleFlagInMapListFacade;
import stories.updatemapscycleflaginmaplist.UpdateMapsCycleFlagInMapListFacadeImpl;
import stories.updatemapscycleflaginmaplist.UpdateMapsCycleFlagInMapListModelContext;

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
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty("properties/config.properties", key, value);
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
    public void runServer(String platformName, String actualSelectedProfileName, String actualSelectedLanguage) throws Exception {
        RunServersModelContext runServersModelContext = new RunServersModelContext(
                platformName,
                actualSelectedProfileName,
                actualSelectedLanguage
        );
        RunServersFacade runServersFacade = new RunServersFacadeImpl(runServersModelContext);
        runServersFacade.execute();
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

    @Override
    public List<ImportMapResultToDisplay> importCustomMapsModsFromServer(List<PlatformProfileMapToImport> ppmToImportList, String profileName) throws Exception {
        ImportCustomMapsFromServerModelContext importCustomMapsFromServerModelContext = new ImportCustomMapsFromServerModelContext(
                ppmToImportList,
                profileName
        );
        ImportCustomMapsFromServerFacade importCustomMapsFromServerFacade = new ImportCustomMapsFromServerFacadeImpl(importCustomMapsFromServerModelContext);
        ImportCustomMapsFromServerFacadeResult result = importCustomMapsFromServerFacade.execute();
        return result.getImportMapResultToDisplayList();
    }

    @Override
    public void updateMapsCycleFlagInMapList(String profileName,
                                      List<String> steamOfficialMapNameListToRemoveFromMapsCycle,
                                      List<String> steamCustomMapNameListToRemoveFromMapsCycle,
                                      List<String> epicOfficialMapNameListToRemoveFromMapsCycle,
                                      List<String> epicCustomMapNameListToRemoveFromMapsCycle,
                                      boolean isInMapsCycle) throws Exception {
        UpdateMapsCycleFlagInMapListModelContext updateMapsCycleFlagInMapListModelContext = new UpdateMapsCycleFlagInMapListModelContext(
                profileName,
                steamOfficialMapNameListToRemoveFromMapsCycle,
                steamCustomMapNameListToRemoveFromMapsCycle,
                epicOfficialMapNameListToRemoveFromMapsCycle,
                epicCustomMapNameListToRemoveFromMapsCycle,
                isInMapsCycle
        );
        UpdateMapsCycleFlagInMapListFacade ipdateMapsCycleFlagInMapListFacade = new UpdateMapsCycleFlagInMapListFacadeImpl(updateMapsCycleFlagInMapListModelContext);
        ipdateMapsCycleFlagInMapListFacade.execute();
    }

    @Override
    public void downloadMapListFromSteamCmd(List<String> platformNameList, List<String> mapNameList) throws Exception {
        DownloadMapListFromSteamCmdModelContext downloadMapListFromSteamCmdModelContext = new DownloadMapListFromSteamCmdModelContext(
                platformNameList,
                mapNameList
        );
        DownloadMapListFromSteamCmdFacade downloadMapListFromSteamCmdFacade = new DownloadMapListFromSteamCmdFacadeImpl(downloadMapListFromSteamCmdModelContext);
        downloadMapListFromSteamCmdFacade.execute();
    }
}
