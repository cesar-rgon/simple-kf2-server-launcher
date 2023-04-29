package stories.installupdateepicserver;

import framework.AbstractManagerFacade;
import framework.EmptyModelContext;
import org.apache.commons.lang3.StringUtils;
import pojos.enums.EnumPlatform;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.getplatforminstallationfolder.GetPlatformInstallationFolderFacade;
import stories.getplatforminstallationfolder.GetPlatformInstallationFolderFacadeImpl;
import stories.getplatforminstallationfolder.GetPlatformInstallationFolderFacadeResult;
import stories.getplatforminstallationfolder.GetPlatformInstallationFolderModelContext;
import stories.installupdateserver.InstallUpdateServerFacade;
import stories.installupdateserver.InstallUpdateServerFacadeImpl;
import stories.installupdateserver.InstallUpdateServerModelContext;
import stories.updateplatforminstallationfolder.UpdatePlatformInstallationFolderFacade;
import stories.updateplatforminstallationfolder.UpdatePlatformInstallationFolderFacadeImpl;
import stories.updateplatforminstallationfolder.UpdatePlatformInstallationFolderFacadeResult;
import stories.updateplatforminstallationfolder.UpdatePlatformInstallationFolderModelContext;

public class InstallUpdateEpicServerManagerFacadeImpl
        extends AbstractManagerFacade<EmptyModelContext, GetPlatformInstallationFolderFacadeResult>
        implements InstallUpdateEpicServerManagerFacade {

    public InstallUpdateEpicServerManagerFacadeImpl() {
        super(new EmptyModelContext(), GetPlatformInstallationFolderFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext) throws Exception {
        return true;
    }

    @Override
    protected GetPlatformInstallationFolderFacadeResult internalExecute(EmptyModelContext facadeModelContext) throws Exception {
        GetPlatformInstallationFolderModelContext getPlatformInstallationFolderModelContext = new GetPlatformInstallationFolderModelContext(
                EnumPlatform.EPIC
        );
        GetPlatformInstallationFolderFacade getPlatformInstallationFolderFacade = new GetPlatformInstallationFolderFacadeImpl(getPlatformInstallationFolderModelContext);
        return getPlatformInstallationFolderFacade.execute();
    }

    @Override
    public boolean saveOrUpdateProperty(String propertyFilePath, String key, String newValue) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty(propertyFilePath, key, newValue);
        return true;
    }

    @Override
    public String findPropertyValue(String propertyFilePath, String key) throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        return propertyService.getPropertyValue(propertyFilePath, key);
    }

    @Override
    public boolean updatePlatformInstallationFolder(String installationFolder) throws Exception {
        UpdatePlatformInstallationFolderModelContext updatePlatformInstallationFolderModelContext = new UpdatePlatformInstallationFolderModelContext(
                EnumPlatform.EPIC,
                installationFolder
        );
        UpdatePlatformInstallationFolderFacade updatePlatformInstallationFolderFacade = new UpdatePlatformInstallationFolderFacadeImpl(updatePlatformInstallationFolderModelContext);
        UpdatePlatformInstallationFolderFacadeResult result = updatePlatformInstallationFolderFacade.execute();
        return result.isUpdated();
    }

    @Override
    public void installUpdateServer() throws Exception {
        InstallUpdateServerModelContext installUpdateServerModelContext = new InstallUpdateServerModelContext(
                EnumPlatform.EPIC,
                false,
                false,
                StringUtils.EMPTY
        );
        InstallUpdateServerFacade installUpdateServerFacade = new InstallUpdateServerFacadeImpl(installUpdateServerModelContext);
        installUpdateServerFacade.execute();
    }
}
