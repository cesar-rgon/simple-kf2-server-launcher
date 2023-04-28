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
    public boolean assertPreconditions() throws Exception {
        return false;
    }

    @Override
    protected InstallationFolderFacadeResult internalExecute(InstallationFolderModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformService platformService = new PlatformServiceImpl(em);

        Optional<AbstractPlatform> platformOptional = platformService.findPlatformByName(facadeModelContext.getPlatformName());
        if (!platformOptional.isPresent()) {
            return new InstallationFolderFacadeResult(false);
        }
        Kf2Common kf2Common = Kf2Factory.getInstance(platformOptional.get());
        assert kf2Common != null;
        return new InstallationFolderFacadeResult(kf2Common.isValidInstallationFolder());
    }
}
