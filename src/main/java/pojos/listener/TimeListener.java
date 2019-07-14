package pojos.listener;

import constants.Constants;
import entities.Map;
import pojos.kf2factory.Kf2Common;
import pojos.kf2factory.Kf2Factory;
import pojos.session.Session;
import services.DatabaseService;
import services.DatabaseServiceImpl;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class TimeListener extends TimerTask {

    private final DatabaseService databaseService;

    public TimeListener() {
        super();
        databaseService = new DatabaseServiceImpl();
    }

    @Override
    public void run() {
        if (!isRunningProcess()) {
            try {
                List<Map> notDownloadedMapList = databaseService.listNotDownloadedMaps();
                if (notDownloadedMapList != null && !notDownloadedMapList.isEmpty()) {
                    String installationFolder = databaseService.findPropertyValue(Constants.KEY_INSTALLATION_FOLDER);
                    List<String> mapNameListToAdd = new ArrayList<String>();
                    for (Map map : notDownloadedMapList) {
                        List<Path> kfmFilesPath = Files.walk(Paths.get(installationFolder + "/KFGame/Cache/" + map.getIdWorkShop()))
                                .filter(Files::isRegularFile)
                                .filter(f -> f.getFileName().toString().startsWith("KF-"))
                                .filter(f -> f.getFileName().toString().endsWith(".kfm"))
                                .collect(Collectors.toList());

                        if (kfmFilesPath != null && !kfmFilesPath.isEmpty()) {
                            String filenameWithExtension = kfmFilesPath.get(0).getFileName().toString();
                            String[] array = filenameWithExtension.split(".kfm");
                            String filenameWithoutExtension = array[0];
                            map.setCode(filenameWithoutExtension);
                            map.setDownloaded(true);
                            databaseService.updateMap(map);
                            mapNameListToAdd.add(map.getCode());
                        }
                    }
                    if (!mapNameListToAdd.isEmpty()) {
                        Kf2Common kf2Common = Kf2Factory.getInstance();
                        kf2Common.addCustomMapsToKfGameIni(mapNameListToAdd,
                                installationFolder,
                                databaseService.listDownloadedMaps());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isRunningProcess() {
        if (Session.getInstance().getProcessList().isEmpty()) {
            return false;
        }
        for (Process process: Session.getInstance().getProcessList()) {
            if (process.isAlive()) {
                return true;
            }
        }
        return false;
    }
}
