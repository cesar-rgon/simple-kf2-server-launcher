package stories.addplatformprofilestomap;

import entities.*;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import services.*;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddPlatformProfilesToMapFacadeImpl
        extends AbstractTransactionalFacade<AddPlatformProfilesToMapModelContext, AddPlatformProfilesToMapFacadeResult>
        implements AddPlatformProfilesToMapFacade {


    public AddPlatformProfilesToMapFacadeImpl(AddPlatformProfilesToMapModelContext addPlatformProfilesToMapModelContext) {
        super(addPlatformProfilesToMapModelContext, AddPlatformProfilesToMapFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(AddPlatformProfilesToMapModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected AddPlatformProfilesToMapFacadeResult internalExecute(AddPlatformProfilesToMapModelContext facadeModelContext, EntityManager em) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformService platformService = new PlatformServiceImpl(em);
        AbstractMapService officialMapService = new OfficialMapServiceImpl(em);
        AbstractMapService customMapModService = new CustomMapModServiceImpl(em);
        StringBuffer success = new StringBuffer();
        StringBuffer errors = new StringBuffer();

        List<Profile> profileList = profileService.getProfileListByNames(facadeModelContext.getProfileNameList(), success, errors);
        List<AbstractPlatform> platformList = platformService.getPlatformListByNames(facadeModelContext.getPlatformNameList(), success, errors);

        AbstractMap map = null;
        Optional officialMapOptional = officialMapService.findMapByCode(facadeModelContext.getMapName());
        Optional customMapModOptional = customMapModService.findMapByCode(facadeModelContext.getMapName());
        if (officialMapOptional.isPresent()) {
            map = (AbstractMap) officialMapOptional.get();
        } else {
            if (customMapModOptional.isPresent()) {
                map = (AbstractMap) customMapModOptional.get();
            }
        }

        List<PlatformProfileMap> platformProfileMapListToAdd = new ArrayList<PlatformProfileMap>();
        for (Profile profile: profileList) {
            for (AbstractPlatform platform: platformList) {
                platformProfileMapListToAdd.add(new PlatformProfileMap(
                        platform,
                        profile,
                        map,
                        map.getReleaseDate(),
                        map.getUrlInfo(),
                        map.getUrlPhoto(),
                        officialMapOptional.isPresent() || customMapModService.isDownloadedMap(platform, map),
                        officialMapOptional.isPresent() || (((CustomMapMod) map).getMap() != null && ((CustomMapMod) map).getMap())
                ));
            }
        }

        if (officialMapOptional.isPresent()) {
            officialMapService.addPlatformProfileMapList(
                    platformProfileMapListToAdd,
                    success,
                    errors);
        }

        if (customMapModOptional.isPresent()) {
            customMapModService.addPlatformProfileMapList(
                    platformProfileMapListToAdd,
                    success,
                    errors);
        }

        String customMapLocalFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.mapCustomLocalFolder");

        for (AbstractPlatform platform: platformList) {
            String absoluteTargetFolder = platform.getInstallationFolder() + customMapLocalFolder;
            if (officialMapOptional.isPresent()) {
                Utils.downloadImageFromUrlToFile(facadeModelContext.getStrUrlMapImage(), absoluteTargetFolder, facadeModelContext.getMapName());
            } else {
                assert map != null;
                Utils.downloadImageFromUrlToFile(facadeModelContext.getStrUrlMapImage(), absoluteTargetFolder, Long.toString(((CustomMapMod) map).getIdWorkShop()));
            }
        }

        return new AddPlatformProfilesToMapFacadeResult(
                success,
                errors
        );
    }
}
