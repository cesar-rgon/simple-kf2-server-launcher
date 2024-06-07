package stories.createcustommapfromworkshop;

import dtos.CustomMapModDto;
import dtos.factories.MapDtoFactory;
import entities.CustomMapMod;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.CustomMapModServiceImpl;

public class CreateCustomMapFromWorkshopFacadeImpl
        extends AbstractTransactionalFacade<CreateCustomMapFromWorkshopModelContext, CreateCustomMapFromWorkshopFacadeResult>
        implements CreateCustomMapFromWorkshopFacade {

    private static final Logger logger = LogManager.getLogger(CreateCustomMapFromWorkshopFacadeImpl.class);

    public CreateCustomMapFromWorkshopFacadeImpl(CreateCustomMapFromWorkshopModelContext createCustomMapFromWorkshopModelContext) {
        super(createCustomMapFromWorkshopModelContext, CreateCustomMapFromWorkshopFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(CreateCustomMapFromWorkshopModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected CreateCustomMapFromWorkshopFacadeResult internalExecute(CreateCustomMapFromWorkshopModelContext facadeModelContext, EntityManager em) throws Exception {
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);
        MapDtoFactory mapDtoFactory = new MapDtoFactory(em);
        StringBuffer success = new StringBuffer();
        StringBuffer errors = new StringBuffer();

        CustomMapMod newCustomMapMod = customMapModService.createNewCustomMapFromWorkshop(
                facadeModelContext.getPlatformNameList(),
                facadeModelContext.getIdWorkShop(),
                facadeModelContext.getProfileNameList(),
                facadeModelContext.getMapName(),
                facadeModelContext.getStrUrlMapImage(),
                facadeModelContext.isMap(),
                success,
                errors
        );

        if (newCustomMapMod == null) {
            return new CreateCustomMapFromWorkshopFacadeResult(null, success, errors);
        }

        return new CreateCustomMapFromWorkshopFacadeResult(
                (CustomMapModDto) mapDtoFactory.newDto(newCustomMapMod),
                success,
                errors
        );
    }


}
