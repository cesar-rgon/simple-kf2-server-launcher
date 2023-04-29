package stories.updateplatforminstallationfolder;

import entities.EpicPlatform;
import entities.SteamPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import services.PlatformService;
import services.PlatformServiceImpl;

import java.util.Optional;

public class UpdatePlatformInstallationFolderFacadeImpl
        extends AbstractTransactionalFacade<UpdatePlatformInstallationFolderModelContext, UpdatePlatformInstallationFolderFacadeResult>
        implements UpdatePlatformInstallationFolderFacade {

    public UpdatePlatformInstallationFolderFacadeImpl(UpdatePlatformInstallationFolderModelContext updatePlatformInstallationFolderModelContext) {
        super(updatePlatformInstallationFolderModelContext, UpdatePlatformInstallationFolderFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(UpdatePlatformInstallationFolderModelContext facadeModelContext, EntityManager em) throws Exception {
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
    protected UpdatePlatformInstallationFolderFacadeResult internalExecute(UpdatePlatformInstallationFolderModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);

        switch (facadeModelContext.getEnumPlatform()) {
            case STEAM:
                Optional<SteamPlatform> steamPlatformOptional = platformService.findSteamPlatform();
                steamPlatformOptional.get().setInstallationFolder(facadeModelContext.getInstallationFolder());
                return new UpdatePlatformInstallationFolderFacadeResult(
                        platformService.updateSteamPlatform(steamPlatformOptional.get())
                );

            case EPIC:
                Optional<EpicPlatform> epicPlatformOptional = platformService.findEpicPlatform();
                epicPlatformOptional.get().setInstallationFolder(facadeModelContext.getInstallationFolder());
                return new UpdatePlatformInstallationFolderFacadeResult(
                        platformService.updateEpicPlatform(epicPlatformOptional.get())
                );

            default:
                return new UpdatePlatformInstallationFolderFacadeResult(false);
        }
    }
}
