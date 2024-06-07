package stories.downloadmaplistfromsteamcmd;

import entities.AbstractMap;
import entities.CustomMapMod;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.CustomMapModServiceImpl;
import stories.createcustommapfromworkshop.CreateCustomMapFromWorkshopFacadeImpl;

import java.util.Optional;

public class DownloadMapListFromSteamCmdFacadeImpl
        extends AbstractTransactionalFacade<DownloadMapListFromSteamCmdModelContext, EmptyFacadeResult>
        implements DownloadMapListFromSteamCmdFacade {

    private static final Logger logger = LogManager.getLogger(DownloadMapListFromSteamCmdFacadeImpl.class);

    public DownloadMapListFromSteamCmdFacadeImpl(DownloadMapListFromSteamCmdModelContext downloadMapListFromSteamCmdModelContext) {
        super(downloadMapListFromSteamCmdModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(DownloadMapListFromSteamCmdModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(DownloadMapListFromSteamCmdModelContext facadeModelContext, EntityManager em) throws Exception {
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);

        for (String mapName: facadeModelContext.getMapNameList()) {
            Optional<AbstractMap> mapOptional = customMapModService.findMapByCode(mapName);
            if (!mapOptional.isPresent()) {
                logger.error("The map: " + mapName + " could not be found in database");
                continue;
            }
            CustomMapMod customMapMod = (CustomMapMod) mapOptional.get();
            customMapModService.downloadMapFromSteamCmd(facadeModelContext.getPlatformNameList(), customMapMod, em);
        }
        return new EmptyFacadeResult();
    }
}
