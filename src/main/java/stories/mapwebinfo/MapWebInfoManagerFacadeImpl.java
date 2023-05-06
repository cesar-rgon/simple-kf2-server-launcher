package stories.mapwebinfo;

import dtos.CustomMapModDto;
import dtos.ProfileDto;
import framework.AbstractManagerFacade;
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
import stories.getplatformprofilelistbyidworkshop.GetPlatformProfileListByIdworkshopFacade;
import stories.getplatformprofilelistbyidworkshop.GetPlatformProfileListByIdworkshopFacadeImpl;
import stories.getplatformprofilelistbyidworkshop.GetPlatformProfileListByIdworkshopFacadeResult;
import stories.getplatformprofilelistbyidworkshop.GetPlatformProfileListByIdworkshopModelContext;
import stories.installationfolder.InstallationFolderFacade;
import stories.installationfolder.InstallationFolderFacadeImpl;
import stories.installationfolder.InstallationFolderFacadeResult;
import stories.installationfolder.InstallationFolderModelContext;
import stories.listprofiles.ListProfilesFacade;
import stories.listprofiles.ListProfilesFacadeImpl;
import stories.listprofiles.ListProfilesFacadeResult;

import java.util.List;

public class MapWebInfoManagerFacadeImpl
        extends AbstractManagerFacade<GetPlatformProfileListByIdworkshopModelContext, GetPlatformProfileListByIdworkshopFacadeResult>
        implements MapWebInfoManagerFacade {

    public MapWebInfoManagerFacadeImpl(GetPlatformProfileListByIdworkshopModelContext getPlatformProfileListByIdworkshopModelContext) {
        super(getPlatformProfileListByIdworkshopModelContext, GetPlatformProfileListByIdworkshopFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(GetPlatformProfileListByIdworkshopModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected GetPlatformProfileListByIdworkshopFacadeResult internalExecute(GetPlatformProfileListByIdworkshopModelContext facadeModelContext) throws Exception {
        GetPlatformProfileListByIdworkshopFacade getPlatformProfileListByIdworkshopFacade = new GetPlatformProfileListByIdworkshopFacadeImpl(facadeModelContext);
        return getPlatformProfileListByIdworkshopFacade.execute();
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
    public List<ProfileDto> getAllProfileList() throws Exception {
        ListProfilesFacade ListProfilesFacade = new ListProfilesFacadeImpl();
        ListProfilesFacadeResult result = ListProfilesFacade.execute();
        return result.getProfileDtoList();
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
    public CreateCustomMapFromWorkshopFacadeResult createNewCustomMapFromWorkshop(List<String> platformNameList, Long idWorkShop, String mapName, String strUrlMapImage, List<String> profileNameList) throws Exception {
        CreateCustomMapFromWorkshopModelContext createCustomMapFromWorkshopModelContext = new CreateCustomMapFromWorkshopModelContext(
                platformNameList,
                idWorkShop,
                mapName,
                strUrlMapImage,
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
}
