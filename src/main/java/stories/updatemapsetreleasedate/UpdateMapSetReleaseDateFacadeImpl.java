package stories.updatemapsetreleasedate;

import entities.PlatformProfileMap;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;

import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

public class UpdateMapSetReleaseDateFacadeImpl
        extends AbstractTransactionalFacade<UpdateMapSetReleaseDateModelContext, EmptyFacadeResult>
        implements UpdateMapSetReleaseDateFacade {

    public UpdateMapSetReleaseDateFacadeImpl(UpdateMapSetReleaseDateModelContext updateMapSetReleaseDateModelContext) {
        super(updateMapSetReleaseDateModelContext, EmptyFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(UpdateMapSetReleaseDateModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateMapSetReleaseDateModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);

        Optional<PlatformProfileMap> ppmOptional = platformProfileMapService.findPlatformProfileMapByNames(
                facadeModelContext.getPlatformName(),
                facadeModelContext.getProfileName(),
                facadeModelContext.getMapName()
        );
        if (!ppmOptional.isPresent()) {
            throw new RuntimeException("The relation between them platform: " + facadeModelContext.getPlatformName() + ", the profile: " + facadeModelContext.getProfileName() + " and the map: " + facadeModelContext.getMapName() + " could not be found");
        }

        ppmOptional.get().setReleaseDate(
                facadeModelContext.getReleaseDate() != null ? Date.from(facadeModelContext.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant()): null
        );
        platformProfileMapService.updateItem(ppmOptional.get());

        return new EmptyFacadeResult();
    }
}
