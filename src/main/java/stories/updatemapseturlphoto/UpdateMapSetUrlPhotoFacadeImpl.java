package stories.updatemapseturlphoto;

import entities.PlatformProfileMap;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;

import java.util.Optional;

public class UpdateMapSetUrlPhotoFacadeImpl
        extends AbstractTransactionalFacade<UpdateMapSetUrlPhotoModelContext, EmptyFacadeResult>
        implements UpdateMapSetUrlPhotoFacade {

    public UpdateMapSetUrlPhotoFacadeImpl(UpdateMapSetUrlPhotoModelContext updateMapSetUrlPhotoModelContext) {
        super(updateMapSetUrlPhotoModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateMapSetUrlPhotoModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateMapSetUrlPhotoModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);

        Optional<PlatformProfileMap> ppmOptional = platformProfileMapService.findPlatformProfileMapByNames(
                facadeModelContext.getPlatformName(),
                facadeModelContext.getProfileName(),
                facadeModelContext.getMapName()
        );
        if (!ppmOptional.isPresent()) {
            throw new RuntimeException("The relation between them platform: " + facadeModelContext.getPlatformName() + ", the profile: " + facadeModelContext.getProfileName() + " and the map: " + facadeModelContext.getMapName() + " could not be found");
        }

        ppmOptional.get().setUrlPhoto(facadeModelContext.getMapPreviewUrl());
        platformProfileMapService.updateItem(ppmOptional.get());

        return new EmptyFacadeResult();
    }
}
