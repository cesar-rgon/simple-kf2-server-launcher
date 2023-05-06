package stories.countplatformsprofilesformap;

import entities.AbstractMap;
import entities.PlatformProfileMap;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import services.CustomMapModServiceImpl;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;

import java.util.List;
import java.util.Optional;

public class CountPlatformsProfilesForMapFacadeImpl
        extends AbstractTransactionalFacade<CountPlatformsProfilesForMapModelContext, CountPlatformsProfilesForMapFacadeResult>
        implements CountPlatformsProfilesForMapFacade {

    public CountPlatformsProfilesForMapFacadeImpl(CountPlatformsProfilesForMapModelContext countPlatformsProfilesForMapModelContext) {
        super(countPlatformsProfilesForMapModelContext, CountPlatformsProfilesForMapFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(CountPlatformsProfilesForMapModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected CountPlatformsProfilesForMapFacadeResult internalExecute(CountPlatformsProfilesForMapModelContext facadeModelContext, EntityManager em) throws Exception {
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);

        Optional<AbstractMap> mapOptional = customMapModService.findMapByCode(facadeModelContext.getCustomMapName());
        if (!mapOptional.isPresent()) {
            return new CountPlatformsProfilesForMapFacadeResult(0);
        }

        List<PlatformProfileMap> platformProfileMapList = platformProfileMapService.listPlatformProfileMaps(mapOptional.get());
        if (platformProfileMapList == null || platformProfileMapList.isEmpty()) {
            return new CountPlatformsProfilesForMapFacadeResult(0);
        }

        return new CountPlatformsProfilesForMapFacadeResult(platformProfileMapList.size());
    }
}
