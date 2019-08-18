package start;

import constants.Constants;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pojos.listener.TimeListener;
import services.PropertyService;
import services.PropertyServiceImpl;
import stories.mapsedition.MapsEditionController;
import utils.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class MainApplication extends Application {

    private static final Logger logger = LogManager.getLogger(MainApplication.class);

    private static Stage primaryStage;
    private static FXMLLoader template;
    private static Timer timer;

    @Override
    public void start(Stage primaryStage) throws Exception {
        template = new FXMLLoader(getClass().getResource("/views/template.fxml"));
        Scene scene = new Scene(template.load());
        FXMLLoader mainContent = new FXMLLoader(getClass().getResource("/views/mainContent.fxml"));
        mainContent.setRoot(template.getNamespace().get("content"));
        mainContent.load();
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/kf2ico.png")));
        PropertyService propertyService = new PropertyServiceImpl();
        String applicationTitle = propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_APPLICATION_TITLE);
        primaryStage.setTitle(applicationTitle);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(800);
        Boolean applicationMaximized = Boolean.parseBoolean(propertyService.getPropertyValue("properties/config.properties", Constants.CONFIG_APPLICATION_MAXIMIZED));
        primaryStage.setMaximized(applicationMaximized != null? applicationMaximized: false);
        primaryStage.show();
        TimerTask timeListener = new TimeListener();
        timer = new Timer();
        timer.schedule(timeListener, 0, 30000);
        this.primaryStage = primaryStage;

        this.primaryStage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                String isMaximized = String.valueOf(t1.booleanValue());
                try {
                    propertyService.setProperty("properties/config.properties", Constants.CONFIG_APPLICATION_MAXIMIZED, isMaximized);
                } catch (Exception e) {
                    String message = "Error setting maximized value in config.properties file";
                    logger.error(message, e);
                    Utils.errorDialog(message, "See stacktrace for more details", e);
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        timer.cancel();
        timer.purge();
    }

    public static FXMLLoader getTemplate() {
        return template;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

}
