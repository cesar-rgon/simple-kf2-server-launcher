package stories.getplatformprofilelistwithoutmap;

import entities.*;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.PlatformProfile;
import pojos.PlatformProfileToDisplay;
import pojos.kf2factory.Kf2Factory;
import services.*;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GetPlatformProfileListWithoutMapFacadeImpl
        extends AbstractTransactionalFacade<GetPlatformProfileListWithoutMapModelContext, GetPlatformProfileListWithoutMapFacadeResult>
        implements GetPlatformProfileListWithoutMapFacade {

    private static final Logger logger = LogManager.getLogger(GetPlatformProfileListWithoutMapFacadeImpl.class);

    public GetPlatformProfileListWithoutMapFacadeImpl(GetPlatformProfileListWithoutMapModelContext getPlatformProfileListWithoutMapModelContext) {
        super(getPlatformProfileListWithoutMapModelContext, GetPlatformProfileListWithoutMapFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(GetPlatformProfileListWithoutMapModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        List<Profile> fullProfileList = profileService.listAllProfiles();

        return (steamPlatformOptional.isPresent() || epicPlatformOptional.isPresent()) && !fullProfileList.isEmpty();
    }

    @Override
    protected GetPlatformProfileListWithoutMapFacadeResult internalExecute(GetPlatformProfileListWithoutMapModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);

        List<AbstractPlatform> validPlatformList = new ArrayList<AbstractPlatform>();
        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        if (steamPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(steamPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(steamPlatformOptional.get());
            }
        }
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        if (epicPlatformOptional.isPresent()) {
            if (Kf2Factory.getInstance(epicPlatformOptional.get(), em).isValidInstallationFolder()) {
                validPlatformList.add(epicPlatformOptional.get());
            }
        }

        List<Profile> fullProfileList = profileService.listAllProfiles();
        List<PlatformProfile> platformProfileListWithoutMap = new ArrayList<PlatformProfile>();

        for (Profile profile: fullProfileList) {
            for (AbstractPlatform platform: validPlatformList) {
                Optional<PlatformProfileMap> platformProfileMapOptional = platformProfileMapService.listPlatformProfileMaps(platform, profile).stream().
                        filter(ppm -> {
                            try {
                                Optional<AbstractMap> customMapModOptional = customMapModService.findByCode(ppm.getMap().getCode());
                                if (customMapModOptional.isPresent()) {
                                    CustomMapMod customMap = (CustomMapMod) Hibernate.unproxy(customMapModOptional.get());
                                    return facadeModelContext.getIdWorkShop().equals(customMap.getIdWorkShop());
                                }
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                            return false;
                        }).findFirst();

                if (!platformProfileMapOptional.isPresent()) {
                    platformProfileListWithoutMap.add(new PlatformProfile(platform, profile));
                }
            }
        }

        return new GetPlatformProfileListWithoutMapFacadeResult(
                platformProfileListWithoutMap
        );
    }
}
