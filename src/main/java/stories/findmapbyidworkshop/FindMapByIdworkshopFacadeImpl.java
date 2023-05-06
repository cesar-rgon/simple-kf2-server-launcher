package stories.findmapbyidworkshop;

import dtos.CustomMapModDto;
import dtos.factories.MapDtoFactory;
import entities.CustomMapMod;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import services.CustomMapModServiceImpl;

import java.util.Optional;

public class FindMapByIdworkshopFacadeImpl
        extends AbstractTransactionalFacade<FindMapByIdworkshopModelContext, FindMapByIdworkshopFacadeResult>
        implements FindMapByIdworkshopFacade {

    public FindMapByIdworkshopFacadeImpl(FindMapByIdworkshopModelContext findMapByIdworkshopModelContext) {
        super(findMapByIdworkshopModelContext, FindMapByIdworkshopFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(FindMapByIdworkshopModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected FindMapByIdworkshopFacadeResult internalExecute(FindMapByIdworkshopModelContext facadeModelContext, EntityManager em) throws Exception {
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);
        MapDtoFactory mapDtoFactory = new MapDtoFactory(em);

        CustomMapModDto customMapModDto = null;
        Optional<CustomMapMod> mapOptional = customMapModService.findByIdWorkShop(facadeModelContext.getIdWorkShop());
        if (mapOptional.isPresent()) {
            customMapModDto = (CustomMapModDto) mapDtoFactory.newDto(mapOptional.get());
        }

        return new FindMapByIdworkshopFacadeResult(
                customMapModDto
        );
    }
}
