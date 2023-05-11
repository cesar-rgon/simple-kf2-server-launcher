package pojos.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stories.checkdownloadedcustommaps.CheckDownloadedCustomMapsFacade;
import stories.checkdownloadedcustommaps.CheckDownloadedCustomMapsFacadeImpl;

import java.util.TimerTask;

public class TimeListener extends TimerTask {

    private static final Logger logger = LogManager.getLogger(TimeListener.class);
    private final CheckDownloadedCustomMapsFacade checkDownloadedCustomMapsFacade;

    public TimeListener() {
        super();
        checkDownloadedCustomMapsFacade = new CheckDownloadedCustomMapsFacadeImpl();
    }

    @Override
    public void run() {
        logger.info("Starting the process of checking downloaded custom maps and mods.");

        try {
            checkDownloadedCustomMapsFacade.execute();
        } catch (Exception e) {
            String message = "Error checking if the maps/mods have been downloaded. See stacktrace for more details:";
            logger.error(message, e);
        }

        logger.info("The process of checking downloaded custom maps and mods has finished.");
    }
}
