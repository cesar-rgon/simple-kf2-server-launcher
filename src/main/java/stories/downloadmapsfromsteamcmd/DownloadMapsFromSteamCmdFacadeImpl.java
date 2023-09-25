package stories.downloadmapsfromsteamcmd;

import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;

public class DownloadMapsFromSteamCmdFacadeImpl
        extends AbstractTransactionalFacade<DownloadMapsFromSteamCmdModelContext, EmptyFacadeResult>
        implements DownloadMapsFromSteamCmdFacade {

    public DownloadMapsFromSteamCmdFacadeImpl(DownloadMapsFromSteamCmdModelContext facadeModelContext) {
        super(facadeModelContext, EmptyFacadeResult.class);
    }

    @Override
    protected boolean assertPreconditions(DownloadMapsFromSteamCmdModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(DownloadMapsFromSteamCmdModelContext facadeModelContext, EntityManager em) throws Exception {
        return null;
    }
}
