package stories.deletemapfromplatformprofile;

import dtos.factories.MapDtoFactory;
import entities.CustomMapMod;
import entities.OfficialMap;
import entities.PlatformProfileMap;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import services.CustomMapModServiceImpl;
import services.OfficialMapServiceImpl;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;

import java.util.Optional;

public class DeleteMapFromPlatformProfileFacadeImpl
        extends AbstractTransactionalFacade<DeleteMapFromPlatformProfileModelContext, DeleteMapFromPlatformProfileFacadeResult>
        implements DeleteMapFromPlatformProfileFacade {

    public DeleteMapFromPlatformProfileFacadeImpl(DeleteMapFromPlatformProfileModelContext deleteMapFromPlatformProfileModelContext) {
        super(deleteMapFromPlatformProfileModelContext, DeleteMapFromPlatformProfileFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(DeleteMapFromPlatformProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);

        Optional<PlatformProfileMap> platformProfileMapOptional = platformProfileMapService.findPlatformProfileMapByNames(
                facadeModelContext.getPlatformName(),
                facadeModelContext.getProfileName(),
                facadeModelContext.getMapName()
        );
        return platformProfileMapOptional.isPresent();
    }

    @Override
    protected DeleteMapFromPlatformProfileFacadeResult internalExecute(DeleteMapFromPlatformProfileModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        OfficialMapServiceImpl officialMapService = new OfficialMapServiceImpl(em);
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);
        MapDtoFactory mapDtoFactory = new MapDtoFactory(em);

        Optional<PlatformProfileMap> platformProfileMapOptional = platformProfileMapService.findPlatformProfileMapByNames(
                facadeModelContext.getPlatformName(),
                facadeModelContext.getProfileName(),
                facadeModelContext.getMapName()
        );

        Optional officialMapOptional = officialMapService.findMapByCode(facadeModelContext.getMapName());
        if (officialMapOptional.isPresent()) {
            OfficialMap officialMap = (OfficialMap) Hibernate.unproxy(platformProfileMapOptional.get().getMap());
            OfficialMap deletedMap = (OfficialMap) officialMapService.deleteMap(platformProfileMapOptional.get().getPlatform(), officialMap, platformProfileMapOptional.get().getProfile());
            return new DeleteMapFromPlatformProfileFacadeResult(
                mapDtoFactory.newDto(deletedMap)
            );
        }

        Optional customMapModOptional = customMapModService.findMapByCode(facadeModelContext.getMapName());
        if (customMapModOptional.isPresent()) {
            CustomMapMod customMapMod = (CustomMapMod) Hibernate.unproxy(platformProfileMapOptional.get().getMap());
            CustomMapMod deletedMap = customMapModService.deleteMap(platformProfileMapOptional.get().getPlatform(), customMapMod, platformProfileMapOptional.get().getProfile());
            return new DeleteMapFromPlatformProfileFacadeResult(
                    mapDtoFactory.newDto(deletedMap)
            );
        }
        return new DeleteMapFromPlatformProfileFacadeResult();
    }
}
