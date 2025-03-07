package start;

import io.undertow.Undertow;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.session.Session;
import services.ConsoleService;
import services.ConsoleServiceImpl;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.populatedatabase.PopulateDatabaseFacade;
import stories.populatedatabase.PopulateDatabaseFacadeImpl;
import utils.Utils;

import java.io.File;
import java.util.Arrays;

public class MainApplication extends Application {

    private static final Logger logger = LogManager.getLogger(MainApplication.class);
    private static boolean createDatabase;

    private static Stage primaryStage;
    private static FXMLLoader template;
    private static Undertow embeddedWebServer;
    private static File appData;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Font.loadFont(getClass().getClassLoader().getResource("fonts/Nunito-Regular.ttf").toExternalForm(), 13);
        Font.loadFont(getClass().getClassLoader().getResource("fonts/Nunito-Bold.ttf").toExternalForm(), 13);
        PropertyService propertyService = new PropertyServiceImpl();
        MainApplication.primaryStage = primaryStage;
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/logo.png")));
        String applicationTitle = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationTitle");
        String applicationVersion = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationVersion");
        primaryStage.setTitle(applicationTitle + " " + applicationVersion);
        primaryStage.setMinWidth(1024);
        primaryStage.setMinHeight(750);

        if (createDatabase) {
            createDatabase = false;
            Session.getInstance().setWizardMode(true);
            startWizard();
        } else {
            Session.getInstance().setWizardMode(false);
            startLauncher();
        }
    }

    public void startLauncher() throws Exception {
        PropertyService propertyService = new PropertyServiceImpl();
        String[] resolution = propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationResolution").split("x");
        primaryStage.setWidth(Double.parseDouble(resolution[0]));
        primaryStage.setHeight(Double.parseDouble(resolution[1]));
        Boolean applicationMaximized = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", "prop.config.applicationMaximized"));
        primaryStage.setMaximized(applicationMaximized != null? applicationMaximized: false);
        template = new FXMLLoader(getClass().getResource("/views/template.fxml"));
        Scene scene = new Scene(template.load());
        FXMLLoader mainContent = new FXMLLoader(getClass().getResource("/views/mainContent.fxml"));
        mainContent.setRoot(template.getNamespace().get("content"));
        mainContent.load();
        primaryStage.setScene(scene);
        primaryStage.show();

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

    private static void prepareUndertow() {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            String appDataPath = System.getenv("APPDATA");
            appData = new File(appDataPath);
        } else {
            if (os.contains("Linux")) {
                String appDataPath = System.getProperty("user.home");
                appData = new File(appDataPath);
            }
        }
        if (appData != null) {
            File undertowFolder = new File(appData.getAbsolutePath() + "/.undertow");
            if (!undertowFolder.exists()) {
                undertowFolder.mkdir();
            }
        }
    }

    public static void main(String[] args) {
        PropertyService propertyService = new PropertyServiceImpl();
        try {
            if (args != null && args.length > 0 && "--upgrade".equalsIgnoreCase(args[0])) {
                propertyService.setProperty("properties/config.properties", "prop.config.upgradeTemporalFile", args[1].replaceAll("'", ""));
            }
            prepareUndertow();
            PopulateDatabaseFacade populateDatabaseFacade = new PopulateDatabaseFacadeImpl();
            populateDatabaseFacade.execute();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        try {
            if (args == null || args.length == 0 || "--upgrade".equalsIgnoreCase(args[0])) {
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

    private void startWizard() throws Exception {
        template = new FXMLLoader(getClass().getResource("/views/wizard-step1.fxml"));
        Scene scene = new Scene(template.load());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        embeddedWebServer.stop();
        PropertyService propertyService = new PropertyServiceImpl();
        propertyService.setProperty("properties/config.properties", "prop.config.applicationResolution", primaryStage.getWidth() + "x" + primaryStage.getHeight());
        primaryStage.close();
    }

    public static FXMLLoader getTemplate() {
        return template;
    }

    public static void setTemplate(FXMLLoader newTemplate) {
        template = newTemplate;
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

    public static File getAppData() {
        return appData;
    }

    public static void setAppData(File appData) {
        MainApplication.appData = appData;
    }

    public static boolean isCreateDatabase() {
        return createDatabase;
    }

    public static void setCreateDatabase(boolean createDatabase) {
        MainApplication.createDatabase = createDatabase;
    }
}
