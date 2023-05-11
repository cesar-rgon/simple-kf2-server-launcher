package stories.checkdownloadedcustommaps;

import entities.AbstractMap;
import entities.CustomMapMod;
import entities.PlatformProfileMap;
import framework.AbstractTransactionalFacade;
import framework.EmptyFacadeResult;
import framework.EmptyModelContext;
import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.listener.TimeListener;
import services.CustomMapModServiceImpl;
import services.PlatformProfileMapService;
import services.PlatformProfileMapServiceImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class CheckDownloadedCustomMapsFacadeImpl
        extends AbstractTransactionalFacade<EmptyModelContext, EmptyFacadeResult>
        implements CheckDownloadedCustomMapsFacade {

    private static final Logger logger = LogManager.getLogger(CheckDownloadedCustomMapsFacadeImpl.class);

    public CheckDownloadedCustomMapsFacadeImpl() {
        super(new EmptyModelContext(), EmptyFacadeResult.class);
    }


    @Override
    protected boolean assertPreconditions(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        return true;
    }

    @Override
    protected EmptyFacadeResult internalExecute(EmptyModelContext facadeModelContext, EntityManager em) throws Exception {
        CustomMapModServiceImpl customMapModService = new CustomMapModServiceImpl(em);
        PlatformProfileMapService platformProfileMapService = new PlatformProfileMapServiceImpl(em);

        List<Integer> customIdMapList = customMapModService.listAllMaps().stream().map(AbstractMap::getId).collect(Collectors.toList());

        List<PlatformProfileMap> platformProfileCustomMapList = platformProfileMapService.listAll().stream().
                filter(ppm -> customIdMapList.contains(ppm.getMap().getId())).
                collect(Collectors.toList());

        if (platformProfileCustomMapList != null && !platformProfileCustomMapList.isEmpty()) {

            for (PlatformProfileMap ppm : platformProfileCustomMapList) {
                CustomMapMod customMap = (CustomMapMod) Hibernate.unproxy(ppm.getMap());
                try {
                    List<Path> kfmFilesPath = Files.walk(Paths.get(ppm.getPlatform().getInstallationFolder() + "/KFGame/Cache/" + customMap.getIdWorkShop()))
                            .filter(Files::isRegularFile)
                            .filter(f -> f.getFileName().toString().toUpperCase().startsWith("KF-"))
                            .filter(f -> f.getFileName().toString().toUpperCase().endsWith(".KFM"))
                            .collect(Collectors.toList());

                    if (kfmFilesPath != null && !kfmFilesPath.isEmpty()) {
                        String filenameWithExtension = kfmFilesPath.get(0).getFileName().toString();
                        String[] array = filenameWithExtension.split("\\.");
                        String filenameWithoutExtension = array[0];
                        customMap.setCode(filenameWithoutExtension);
                        customMapModService.updateItem(customMap);
                        ppm.setDownloaded(true);
                        platformProfileMapService.updateItem(ppm);
                    } else {
                        File folder = new File(ppm.getPlatform().getInstallationFolder() + "/KFGame/Cache/" + customMap.getIdWorkShop());
                        if (folder.exists()) {
                            ppm.setDownloaded(true);
                            platformProfileMapService.updateItem(ppm);
                        }
                    }
                } catch (Exception ex) {
                    String message = "The custom map/mod: " + customMap.getCode() + " with idWorkShop: " + customMap.getIdWorkShop() + " could not be checked as 'Downloaded'.\nSee stacktrace for more details:";
                    logger.error(message, ex);
                }
            }
        }

        return new EmptyFacadeResult();
    }
}
