package start;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.util.Headers;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.listener.TimeListener;
import services.*;
import stories.populatedatabase.PopulateDatabaseFacade;
import stories.populatedatabase.PopulateDatabaseFacadeImpl;
import utils.Utils;

import java.nio.ByteBuffer;
import java.util.*;

public class MainApplication extends Application {

    private static final Logger logger = LogManager.getLogger(MainApplication.class);

    private static Stage primaryStage;
    private static FXMLLoader template;
    private static Timer timer;
    private static Undertow embeddedWebServer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Fix a problem with newest JDK and JavaFX WebView. http2 support need to be disabled
        Properties systemProperties = System.getProperties();
        systemProperties.setProperty("com.sun.webkit.useHTTP2Loader", "false");

        PropertyService propertyService = new PropertyServiceImpl();
        Font.loadFont(getClass().getClassLoader().getResource("fonts/Ubuntu-R.ttf").toExternalForm(), 13);
        Font.loadFont(getClass().getClassLoader().getResource("fonts/Ubuntu-B.ttf").toExternalForm(), 13);
        template = new FXMLLoader(getClass().getResource("/views/template.fxml"));
        Scene scene = new Scene(template.load());
        FXMLLoader mainContent = new FXMLLoader(getClass().getResource("/views/mainContent.fxml"));
        mainContent.setRoot(template.getNamespace().get("content"));
        mainContent.load();
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/logo.png")));
        String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
        String applicationVersion = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationVersion");
        primaryStage.setTitle(applicationTitle + " " + applicationVersion);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(700);
        String[] resolution = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationResolution").split("x");
        primaryStage.setWidth(Double.parseDouble(resolution[0]));
        primaryStage.setHeight(Double.parseDouble(resolution[1]));
        Boolean applicationMaximized = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationMaximized"));
        primaryStage.setMaximized(applicationMaximized != null? applicationMaximized: false);
        primaryStage.show();
        TimerTask timeListener = new TimeListener();
        timer = new Timer();
        String checkDownloadedMapsEveryMilliseconds = propertyService.getPropertyValue("properties/config.properties", "prop.config.checkDownloadedMapsEveryMilliseconds");
        timer.schedule(timeListener, 0, Long.parseLong(checkDownloadedMapsEveryMilliseconds));
        this.primaryStage = primaryStage;

        String languageCode = propertyService.getPropertyValue("properties/config.properties", "prop.config.selectedLanguageCode");
        Boolean checkForUpgrades = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.checkForUpgrades"));
        if (checkForUpgrades != null && checkForUpgrades) {
            Utils.checkApplicationUpgrade(languageCode, true);
        }

        Utils.showTipsOnStasrtup();

        this.primaryStage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                String isMaximized = String.valueOf(t1.booleanValue());
                try {
                    propertyService.setProperty("properties/config.properties", "prop.config.applicationMaximized", isMaximized);
                } catch (Exception e) {
                    String message = "Error setting maximized value in config.properties file";
                    logger.error(message, e);
                    Utils.errorDialog(message, e);
                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            PopulateDatabaseFacade populateDatabaseFacade = new PopulateDatabaseFacadeImpl();
            populateDatabaseFacade.execute();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            if (args == null || args.length == 0) {
                PropertyService propertyService = new PropertyServiceImpl();
                String applicationVersion = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationVersion");
                logger.info("----- Starting application Simple Killing Floor 2 Server Launcher v" + applicationVersion + " -----");
                launch(args);
                logger.info("----- Ending application Simple Killing Floor 2 Server Launcher v" + applicationVersion + " -----");
            } else {
                if ("--pp".equalsIgnoreCase(args[0])) {
                    if (args.length > 1) {
                        ConsoleService consoleService = new ConsoleServiceImpl(null);
                        String[] parameters = Arrays.copyOfRange(args, 1, args.length);
                        consoleService.runServersByConsole(Arrays.asList(parameters));
                    } else {
                        String errorMessage = "\nInvalid parameters.\nUse --pp platformName/profileName [ platformName2/profileName2 ... ].\nValid platform names are: Steam, Epic.\nCase sensitive letters must be used in Profile Name.\n";
                        System.out.println(errorMessage);
                    }
                } else {
                    String errorMessage = "\nInvalid parameters.\nUse --pp platformName/profileName [ platformName2/profileName2 ... ].\nValid platform names are: Steam, Epic.\nCase sensitive letters must be used in Profile Name.\n";
                    System.out.println(errorMessage);
                }
                System.exit(0);
            }
        } catch (Exception e) {
             logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void stop() throws Exception {
        timer.cancel();
        timer.purge();
        embeddedWebServer.stop();
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty("properties/config.properties", "prop.config.applicationResolution", primaryStage.getWidth() + "x" + primaryStage.getHeight());
    }

    public static FXMLLoader getTemplate() {
        return template;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static Undertow getEmbeddedWebServer() {
        return embeddedWebServer;
    }

    public static void setEmbeddedWebServer(Undertow embeddedWebServer) {
        MainApplication.embeddedWebServer = embeddedWebServer;
    }
}
