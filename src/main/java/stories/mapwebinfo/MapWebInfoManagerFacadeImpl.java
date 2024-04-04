package stories.mapwebinfo;

import dtos.CustomMapModDto;
import framework.AbstractManagerFacade;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.addplatformprofilestomap.AddPlatformProfilesToMapFacade;
import stories.addplatformprofilestomap.AddPlatformProfilesToMapFacadeImpl;
import stories.addplatformprofilestomap.AddPlatformProfilesToMapFacadeResult;
import stories.addplatformprofilestomap.AddPlatformProfilesToMapModelContext;
import stories.countplatformsprofilesformap.CountPlatformsProfilesForMapFacade;
import stories.countplatformsprofilesformap.CountPlatformsProfilesForMapFacadeImpl;
import stories.countplatformsprofilesformap.CountPlatformsProfilesForMapFacadeResult;
import stories.countplatformsprofilesformap.CountPlatformsProfilesForMapModelContext;
import stories.createcustommapfromworkshop.CreateCustomMapFromWorkshopFacade;
import stories.createcustommapfromworkshop.CreateCustomMapFromWorkshopFacadeImpl;
import stories.createcustommapfromworkshop.CreateCustomMapFromWorkshopFacadeResult;
import stories.createcustommapfromworkshop.CreateCustomMapFromWorkshopModelContext;
import stories.findmapbyidworkshop.FindMapByIdworkshopFacade;
import stories.findmapbyidworkshop.FindMapByIdworkshopFacadeImpl;
import stories.findmapbyidworkshop.FindMapByIdworkshopFacadeResult;
import stories.findmapbyidworkshop.FindMapByIdworkshopModelContext;
import stories.getplatformprofilelistwithoutmap.GetPlatformProfileListWithoutMapFacade;
import stories.getplatformprofilelistwithoutmap.GetPlatformProfileListWithoutMapFacadeImpl;
import stories.getplatformprofilelistwithoutmap.GetPlatformProfileListWithoutMapFacadeResult;
import stories.getplatformprofilelistwithoutmap.GetPlatformProfileListWithoutMapModelContext;
import stories.installationfolder.InstallationFolderFacade;
import stories.installationfolder.InstallationFolderFacadeImpl;
import stories.installationfolder.InstallationFolderFacadeResult;
import stories.installationfolder.InstallationFolderModelContext;
import stories.listselectedplatformsprofiles.ListSelectedPlatformsProfilesFacade;
import stories.listselectedplatformsprofiles.ListSelectedPlatformsProfilesFacadeImpl;
import stories.listselectedplatformsprofiles.ListSelectedPlatformsProfilesFacadeResult;
import stories.listselectedplatformsprofiles.ListSelectedPlatformsProfilesModelContext;

import java.util.List;

public class MapWebInfoManagerFacadeImpl
        extends AbstractManagerFacade<GetPlatformProfileListWithoutMapModelContext, GetPlatformProfileListWithoutMapFacadeResult>
        implements MapWebInfoManagerFacade {

    public MapWebInfoManagerFacadeImpl(GetPlatformProfileListWithoutMapModelContext getPlatformProfileListWithoutMapModelContext) {
        super(getPlatformProfileListWithoutMapModelContext, GetPlatformProfileListWithoutMapFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(GetPlatformProfileListWithoutMapModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected GetPlatformProfileListWithoutMapFacadeResult internalExecute(GetPlatformProfileListWithoutMapModelContext facadeModelContext) throws Exception {
        GetPlatformProfileListWithoutMapFacade getPlatformProfileListWithoutMapFacade = new GetPlatformProfileListWithoutMapFacadeImpl(facadeModelContext);
        return getPlatformProfileListWithoutMapFacade.execute();
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
    public CustomMapModDto findMapOrModByIdWorkShop(Long idWorkShop) throws Exception {
        FindMapByIdworkshopModelContext findMapByIdworkshopModelContext = new FindMapByIdworkshopModelContext(
                idWorkShop
        );
        FindMapByIdworkshopFacade findMapByIdworkshopFacade = new FindMapByIdworkshopFacadeImpl(findMapByIdworkshopModelContext);
        FindMapByIdworkshopFacadeResult result = findMapByIdworkshopFacade.execute();
        return result.getCustomMapDto();
    }

    @Override
    public int countPlatformsProfilesForMap(String customMapName) throws Exception {
        CountPlatformsProfilesForMapModelContext countPlatformsProfilesForMapModelContext = new CountPlatformsProfilesForMapModelContext(
                customMapName
        );
        CountPlatformsProfilesForMapFacade countPlatformsProfilesForMapFacade = new CountPlatformsProfilesForMapFacadeImpl(countPlatformsProfilesForMapModelContext);
        CountPlatformsProfilesForMapFacadeResult result = countPlatformsProfilesForMapFacade.execute();
        return result.getCountPlatformsProfilesForMap();
    }

    @Override
    public CreateCustomMapFromWorkshopFacadeResult createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, String mapName, String strUrlMapImage, boolean isMap, List<String> profileNameList) throws Exception {
        CreateCustomMapFromWorkshopModelContext createCustomMapFromWorkshopModelContext = new CreateCustomMapFromWorkshopModelContext(
                platformNameList,
                idWorkShop,
                mapName,
                strUrlMapImage,
                isMap,
                profileNameList
        );
        CreateCustomMapFromWorkshopFacade createCustomMapFromWorkshopFacade = new CreateCustomMapFromWorkshopFacadeImpl(createCustomMapFromWorkshopModelContext);
        return createCustomMapFromWorkshopFacade.execute();
    }

    @Override
    public AddPlatformProfilesToMapFacadeResult addPlatformProfilesToMap(List<String> platformNameList, String mapName, String strUrlMapImage, List<String> profileNameList) throws Exception {
        AddPlatformProfilesToMapModelContext addPlatformProfilesToMapModelContext = new AddPlatformProfilesToMapModelContext(
                platformNameList,
                mapName,
                strUrlMapImage,
                profileNameList
        );
        AddPlatformProfilesToMapFacade addPlatformProfilesToMapFacade = new AddPlatformProfilesToMapFacadeImpl(addPlatformProfilesToMapModelContext);
        return addPlatformProfilesToMapFacade.execute();
    }

    @Override
    public List<PlatformProfileToDisplay> getSelectedPlatformProfileList(List<PlatformProfile> platformProfileList) throws Exception {
        ListSelectedPlatformsProfilesModelContext listSelectedPlatformsProfilesModelContext = new ListSelectedPlatformsProfilesModelContext(
                platformProfileList
        );
        ListSelectedPlatformsProfilesFacade listSelectedPlatformsProfilesFacade = new ListSelectedPlatformsProfilesFacadeImpl(listSelectedPlatformsProfilesModelContext);
        ListSelectedPlatformsProfilesFacadeResult result = listSelectedPlatformsProfilesFacade.execute();
        return result.getSelectedPlatformProfiles();
    }

    @Override
    public String findPropertyValue(String propertyFilePath, String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue(propertyFilePath, key);
    }
}
