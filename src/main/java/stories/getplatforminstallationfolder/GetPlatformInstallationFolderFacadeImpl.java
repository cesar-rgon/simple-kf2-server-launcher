package stories.getplatforminstallationfolder;

import entities.EpicPlatform;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import services.PlatformService;
import services.PlatformServiceImpl;

import java.util.Optional;

public class GetPlatformInstallationFolderFacadeImpl
        extends AbstractTransactionalFacade<GetPlatformInstallationFolderModelContext, GetPlatformInstallationFolderFacadeResult>
        implements GetPlatformInstallationFolderFacade {

    public GetPlatformInstallationFolderFacadeImpl(GetPlatformInstallationFolderModelContext getPlatformInstallationFolderModelContext) {
        super(getPlatformInstallationFolderModelContext, GetPlatformInstallationFolderFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(GetPlatformInstallationFolderModelContext facadeModelContext, EntityManager em) throws Exception {
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
    protected GetPlatformInstallationFolderFacadeResult internalExecute(GetPlatformInstallationFolderModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);

        switch (facadeModelContext.getEnumPlatform()) {
            case STEAM:
                Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
                return new GetPlatformInstallationFolderFacadeResult(
                        steamPlatformOptional.get().getInstallationFolder()
                );

            case EPIC:
                Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
                return new GetPlatformInstallationFolderFacadeResult(
                        epicPlatformOptional.get().getInstallationFolder()
                );

            default:
                return new GetPlatformInstallationFolderFacadeResult();
        }
    }
}
