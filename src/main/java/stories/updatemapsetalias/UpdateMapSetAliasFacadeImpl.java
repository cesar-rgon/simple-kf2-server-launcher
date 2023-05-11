package stories.updatemapsetalias;

import entities.PlatformProfileMap;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;

import java.util.Optional;

public class UpdateMapSetAliasFacadeImpl
        extends AbstractTransactionalFacade<UpdateMapSetAliasModelContext, EmptyFacadeResult>
        implements UpdateMapSetAliasFacade {

    public UpdateMapSetAliasFacadeImpl(UpdateMapSetAliasModelContext updateMapSetAliasModelContext) {
        super(updateMapSetAliasModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateMapSetAliasModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateMapSetAliasModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);

        Optional<PlatformProfileMap> ppmOptional = platformProfileMapService.findPlatformProfileMapByNames(
                facadeModelContext.getPlatformName(),
                facadeModelContext.getProfileName(),
                facadeModelContext.getMapName()
        );
        if (!ppmOptional.isPresent()) {
            throw new RuntimeException("The relation between them platform: " + facadeModelContext.getPlatformName() + ", the profile: " + facadeModelContext.getProfileName() + " and the map: " + facadeModelContext.getMapName() + " could not be found");
        }

        ppmOptional.get().setAlias(facadeModelContext.getNewAlias());
        platformProfileMapService.updateItem(ppmOptional.get());

        return new EmptyFacadeResult();
    }
}
