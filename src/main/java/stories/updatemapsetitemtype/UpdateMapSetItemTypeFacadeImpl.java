package stories.updatemapsetitemtype;

import dtos.factories.PlatformProfileMapDtoFactory;
import entities.AbstractMap;
import entities.CustomMapMod;
import entities.PlatformProfileMap;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import services.CustomMapModServiceImpl;
import services.OfficialMapServiceImpl;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;

import java.util.Optional;

public class UpdateMapSetItemTypeFacadeImpl
        extends AbstractTransactionalFacade<UpdateMapSetItemTypeModelContext, EmptyFacadeResult>
        implements UpdateMapSetItemTypeFacade {

    public UpdateMapSetItemTypeFacadeImpl(UpdateMapSetItemTypeModelContext updateMapSetItemTypeModelContext) {
        super(updateMapSetItemTypeModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateMapSetItemTypeModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateMapSetItemTypeModelContext facadeModelContext, EntityManager em) throws Exception {
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);

        Optional<AbstractMap> mapOptional = customMapModService.findMapByCode(facadeModelContext.getMapName());
        if (!mapOptional.isPresent()) {
            return new EmptyFacadeResult();
        }

        CustomMapMod customMapMod = (CustomMapMod) mapOptional.get();
        customMapMod.setMap(facadeModelContext.isMap());
        customMapModService.updateItem(customMapMod);

        return new EmptyFacadeResult();
    }
}
