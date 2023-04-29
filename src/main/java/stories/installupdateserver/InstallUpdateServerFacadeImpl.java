package stories.installupdateserver;

import entities.EpicPlatform;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Epic;
import pojos.kf2factory.Kf2Factory;
import pojos.kf2factory.Kf2Steam;
import services.PlatformService;
import services.PlatformServiceImpl;

import java.util.Optional;

public class InstallUpdateServerFacadeImpl
        extends AbstractTransactionalFacade<InstallUpdateServerModelContext, EmptyFacadeResult>
        implements InstallUpdateServerFacade {

    public InstallUpdateServerFacadeImpl(InstallUpdateServerModelContext installUpdateServerModelContext) {
        super(installUpdateServerModelContext, EmptyFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(InstallUpdateServerModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);

        switch (facadeModelContext.getEnumPlatform()) {
            case STEAM:
                Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
                return steamPlatformOptional.isPresent();

            case EPIC:
                Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
                return epicPlatformOptional.isPresent();

            default:
                return false;
        }
    }

    @Override
    protected EmptyFacadeResult internalExecute(InstallUpdateServerModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);

        switch (facadeModelContext.getEnumPlatform()) {
            case STEAM:
                Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
                Kf2Common kf2SteamCommon = Kf2Factory.getInstance(steamPlatformOptional.get(), em);
                ((Kf2Steam) kf2SteamCommon).installOrUpdateServer(
                        facadeModelContext.isValidateFiles(),
                        facadeModelContext.isBeta(),
                        facadeModelContext.getBetaBrunch()
                );
                return new EmptyFacadeResult();

            case EPIC:
                Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
                Kf2Common kf2EpicCommon = Kf2Factory.getInstance(epicPlatformOptional.get(), em);
                ((Kf2Epic) kf2EpicCommon).installOrUpdateServer();
                return new EmptyFacadeResult();

            default:
                return new EmptyFacadeResult();
        }
    }
}
