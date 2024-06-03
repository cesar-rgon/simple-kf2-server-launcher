package stories.updatemapscycleflaginmaplist;

import entities.PlatformProfileMap;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.enums.EnumPlatform;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;

import java.util.Optional;

public class UpdateMapsCycleFlagInMapListFacadeImpl
        extends AbstractTransactionalFacade<UpdateMapsCycleFlagInMapListModelContext, EmptyFacadeResult>
        implements UpdateMapsCycleFlagInMapListFacade {

    private static final Logger logger = LogManager.getLogger(UpdateMapsCycleFlagInMapListFacadeImpl.class);

    public UpdateMapsCycleFlagInMapListFacadeImpl(UpdateMapsCycleFlagInMapListModelContext updateMapsCycleFlagInMapListModelContext) {
        super(updateMapsCycleFlagInMapListModelContext, EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(UpdateMapsCycleFlagInMapListModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(UpdateMapsCycleFlagInMapListModelContext facadeModelContext, EntityManager em) throws Exception {
        PlatformProfileMapService ppmService = new PlatformProfileMapServiceImpl(em);

        for (String mapName: facadeModelContext.getSteamOfficialMapNameListToRemoveFromMapsCycle()) {
            try {
                Optional<PlatformProfileMap> ppmOptional = ppmService.findPlatformProfileMapByNames(EnumPlatform.STEAM.name(), facadeModelContext.getProfileName(), mapName);
                if (ppmOptional.isPresent()) {
                    ppmOptional.get().setInMapsCycle(facadeModelContext.isInMapsCycle());
                    ppmService.updateItem(ppmOptional.get());
                }
            } catch (Exception e) {
                logger.error("Error updating MapsCycle flag in official Steam's map: " + mapName, e);
            }
        }

        for (String mapName: facadeModelContext.getSteamCustomMapNameListToRemoveFromMapsCycle()) {
            try {
                Optional<PlatformProfileMap> ppmOptional = ppmService.findPlatformProfileMapByNames(EnumPlatform.STEAM.name(), facadeModelContext.getProfileName(), mapName);
                if (ppmOptional.isPresent()) {
                    ppmOptional.get().setInMapsCycle(facadeModelContext.isInMapsCycle());
                    ppmService.updateItem(ppmOptional.get());
                }
            } catch (Exception e) {
                logger.error("Error updating MapsCycle flag in custom Steam's map: " + mapName, e);
            }
        }

        for (String mapName: facadeModelContext.getEpicOfficialMapNameListToRemoveFromMapsCycle()) {
            try {
                Optional<PlatformProfileMap> ppmOptional = ppmService.findPlatformProfileMapByNames(EnumPlatform.EPIC.name(), facadeModelContext.getProfileName(), mapName);
                if (ppmOptional.isPresent()) {
                    ppmOptional.get().setInMapsCycle(facadeModelContext.isInMapsCycle());
                    ppmService.updateItem(ppmOptional.get());
                }
            } catch (Exception e) {
                logger.error("Error updating MapsCycle flag in official Epic's map: " + mapName, e);
            }
        }

        for (String mapName: facadeModelContext.getEpicCustomMapNameListToRemoveFromMapsCycle()) {
            try {
                Optional<PlatformProfileMap> ppmOptional = ppmService.findPlatformProfileMapByNames(EnumPlatform.EPIC.name(), facadeModelContext.getProfileName(), mapName);
                if (ppmOptional.isPresent()) {
                    ppmOptional.get().setInMapsCycle(facadeModelContext.isInMapsCycle());
                    ppmService.updateItem(ppmOptional.get());
                }
            } catch (Exception e) {
                logger.error("Error updating MapsCycle flag in custom Epic's map: " + mapName, e);
            }
        }

        return new EmptyFacadeResult();
    }
}
