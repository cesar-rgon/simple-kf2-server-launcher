package stories.getplatformprofilelistbyidworkshop;

import daos.AbstractDao;
import entities.*;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.PlatformProfile;
import pojos.kf2factory.Kf2Factory;
import services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GetPlatformProfileListByIdworkshopFacadeImpl
        extends AbstractTransactionalFacade<GetPlatformProfileListByIdworkshopModelContext, GetPlatformProfileListByIdworkshopFacadeResult>
        implements GetPlatformProfileListByIdworkshopFacade {

    private static final Logger logger = LogManager.getLogger(GetPlatformProfileListByIdworkshopFacadeImpl.class);

    public GetPlatformProfileListByIdworkshopFacadeImpl(GetPlatformProfileListByIdworkshopModelContext getPlatformProfileListByIdworkshopModelContext) {
        super(getPlatformProfileListByIdworkshopModelContext, GetPlatformProfileListByIdworkshopFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(GetPlatformProfileListByIdworkshopModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        ProfileService profileService = new ProfileServiceImpl(em);

        Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
        Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
        List<Profile> fullProfileList = profileService.listAllProfiles();

        return (steamPlatformOptional.isPresent() || epicPlatformOptional.isPresent()) && !fullProfileList.isEmpty();
    }

    @Override
    protected GetPlatformProfileListByIdworkshopFacadeResult internalExecute(GetPlatformProfileListByIdworkshopModelContext facadeModelContext, EntityManager em) throws Exception {
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

        return new GetPlatformProfileListByIdworkshopFacadeResult(
                platformProfileListWithoutMap
        );
    }
}
