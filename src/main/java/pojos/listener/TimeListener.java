package pojos.listener;

import daos.CustomMapModDao;
import daos.OfficialMapDao;
import daos.PlatformProfileMapDao;
import entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import pojos.enums.EnumPlatform;
import pojos.session.Session;
import services.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class TimeListener extends TimerTask {

    private static final Logger logger = LogManager.getLogger(TimeListener.class);
    private final AbstractMapService customMapModService;
    private final PlatformProfileMapService platformProfileMapService;
    public TimeListener() {
        super();
        this.customMapModService = new CustomMapModServiceImpl();
        this.platformProfileMapService = new PlatformProfileMapServiceImpl();
    }

    @Override
    public void run() {
        logger.info("Starting the process of checking downloaded custom maps and mods.");
        try {
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
            logger.info("The process of checking downloaded custom maps and mods has finished.");

        } catch (Exception e) {
            String message = "Error checking if the maps/mods have been downloaded. See stacktrace for more details:";
            logger.error(message, e);
        }
    }
}
