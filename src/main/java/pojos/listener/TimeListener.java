package pojos.listener;

import daos.CustomMapModDao;
import entities.AbstractMap;
import entities.CustomMapMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PropertyService;
import services.PropertyServiceImpl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class TimeListener extends TimerTask {

    private static final Logger logger = LogManager.getLogger(TimeListener.class);

    public TimeListener() {
        super();
    }

    @Override
    public void run() {
        logger.info("Starting the process of checking downloaded custom maps and mods.");
        try {
            List<CustomMapMod> mapList = CustomMapModDao.getInstance().listAll();
            if (mapList != null && !mapList.isEmpty()) {
                PropertyService propertyService = new PropertyServiceImpl();
                String installationFolder = propertyService.getPropertyValue("properties/config.properties", "prop.config.installationFolder");
                for (CustomMapMod map : mapList) {
                    try {
                        List<Path> kfmFilesPath = Files.walk(Paths.get(installationFolder + "/KFGame/Cache/" + map.getIdWorkShop()))
                                .filter(Files::isRegularFile)
                                .filter(f -> f.getFileName().toString().toUpperCase().startsWith("KF-"))
                                .filter(f -> f.getFileName().toString().toUpperCase().endsWith(".KFM"))
                                .collect(Collectors.toList());

                        if (kfmFilesPath != null && !kfmFilesPath.isEmpty()) {
                            String filenameWithExtension = kfmFilesPath.get(0).getFileName().toString();
                            String[] array = filenameWithExtension.split("\\.");
                            String filenameWithoutExtension = array[0];
                            map.setCode(filenameWithoutExtension);
                            map.setDownloaded(true);
                            CustomMapModDao.getInstance().update(map);
                        } else {
                            File folder = new File(installationFolder + "/KFGame/Cache/" + map.getIdWorkShop());
                            if (folder.exists()) {
                                map.setDownloaded(true);
                                CustomMapModDao.getInstance().update(map);
                            }
                        }
                    } catch (Exception ex) {
                        String message = "The custom map/mod: " + map.getCode() + " with idWorkShop: " + map.getIdWorkShop() + " could not be checked as 'Downloaded'.\nSee stacktrace for more details:";
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
