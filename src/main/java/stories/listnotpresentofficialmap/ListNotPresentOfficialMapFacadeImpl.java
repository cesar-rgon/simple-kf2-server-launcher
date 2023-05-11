package stories.listnotpresentofficialmap;

import entities.AbstractPlatform;
import entities.Profile;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import pojos.MapToDisplay;
import services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ListNotPresentOfficialMapFacadeImpl
        extends AbstractTransactionalFacade<ListNotPresentOfficialMapFacadeModelContext, ListNotPresentOfficialMapFacadeResult>
        implements ListNotPresentOfficialMapFacade {

    public ListNotPresentOfficialMapFacadeImpl(ListNotPresentOfficialMapFacadeModelContext listNotPresentOfficialMapFacadeModelContext) {
        super(listNotPresentOfficialMapFacadeModelContext, ListNotPresentOfficialMapFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(ListNotPresentOfficialMapFacadeModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected ListNotPresentOfficialMapFacadeResult internalExecute(ListNotPresentOfficialMapFacadeModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);

        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(facadeModelContext.getPlatformName());
        if (!platformOptional.isPresent()) {
            throw new RuntimeException("No platform was found: " + facadeModelContext.getPlatformName());
        }

        Optional<Profile> profileOptional = profileService.findProfileByCode(facadeModelContext.getProfileName());
        if (!profileOptional.isPresent()) {
            throw new RuntimeException("No profile was found: " + facadeModelContext.getProfileName());
        }

        List<String> mapListInPlatformProfile = platformProfileMapService.listPlatformProfileMaps(platformOptional.get(), profileOptional.get()).stream().
                map(ppm -> ppm.getMap().getCode()).
                collect(Collectors.toList());

        List<String> result = new ArrayList<>(facadeModelContext.getOfficialMapNameList());
        result.removeAll(mapListInPlatformProfile);

        List<MapToDisplay> mapListNotInPlatformProfile = result.stream().
                map(mapName -> {
                    return new MapToDisplay(mapName);
                }).
                collect(Collectors.toList());

        return new ListNotPresentOfficialMapFacadeResult(
                mapListNotInPlatformProfile
        );
    }
}
