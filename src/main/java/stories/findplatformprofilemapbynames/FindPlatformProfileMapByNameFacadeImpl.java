package stories.findplatformprofilemapbynames;

import dtos.PlatformProfileMapDto;
import dtos.factories.PlatformProfileMapDtoFactory;
import entities.PlatformProfileMap;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;

import java.util.Optional;

public class FindPlatformProfileMapByNameFacadeImpl
        extends AbstractTransactionalFacade<FindPlatformProfileMapByNameModelContext, FindPlatformProfileMapByNameFacadeResult>
        implements FindPlatformProfileMapByNameFacade {

    public FindPlatformProfileMapByNameFacadeImpl(FindPlatformProfileMapByNameModelContext findPlatformProfileMapByNameModelContext) {
        super(findPlatformProfileMapByNameModelContext, FindPlatformProfileMapByNameFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(FindPlatformProfileMapByNameModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected FindPlatformProfileMapByNameFacadeResult internalExecute(FindPlatformProfileMapByNameModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        PlatformProfileMapDtoFactory platformProfileMapDtoFactory = new PlatformProfileMapDtoFactory(em);
        Optional<PlatformProfileMapDto> platformProfileMapDtoOptional = Optional.empty();

        Optional<PlatformProfileMap> profileMapOptional = platformProfileMapService.findPlatformProfileMapByNames(
                facadeModelContext.getPlatformName(),
                facadeModelContext.getProfileName(),
                facadeModelContext.getMapName()
        );
        if (profileMapOptional.isPresent()) {
            platformProfileMapDtoOptional = Optional.ofNullable(platformProfileMapDtoFactory.newDto(profileMapOptional.get()));
        }

        return new FindPlatformProfileMapByNameFacadeResult(
                platformProfileMapDtoOptional
        );
    }
}
