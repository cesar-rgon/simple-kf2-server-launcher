package stories.installationfolder;

import entities.AbstractPlatform;
import framework.AbstractTransactionalFacade;
import jakarta.persistence.EntityManager;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import services.PlatformService;
import services.PlatformServiceImpl;

import java.util.Optional;

public class InstallationFolderFacadeImpl
        extends AbstractTransactionalFacade<InstallationFolderModelContext, InstallationFolderFacadeResult>
        implements InstallationFolderFacade {

    public InstallationFolderFacadeImpl(InstallationFolderModelContext installationFolderModelContext) {
        super(installationFolderModelContext, InstallationFolderFacadeResult.class);
    }

    @Override
    public boolean assertPreconditions(InstallationFolderModelContext installationFolderModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected InstallationFolderFacadeResult internalExecute(InstallationFolderModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);
        return new InstallationFolderFacadeResult(
            platformService.isValidInstallationFolder(facadeModelContext.getPlatformName())
        );
    }
}
