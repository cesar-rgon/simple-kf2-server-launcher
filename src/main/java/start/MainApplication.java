package start;

import constants.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import pojos.listener.TimeListener;

import java.util.Timer;
import java.util.TimerTask;

public class MainApplication extends Application {

    private static Stage primaryStage;
    private static FXMLLoader template;
    private static Timer timer;

    @Override
    public void start(Stage primaryStage) throws Exception{
        template = new FXMLLoader(getClass().getResource("/views/template.fxml"));
        Scene scene = new Scene(template.load());
        FXMLLoader mainContent = new FXMLLoader(getClass().getResource("/views/mainContent.fxml"));
        mainContent.setRoot(template.getNamespace().get("content"));
        mainContent.load();
        primaryStage.getIcons().add(new Image("file:src/main/resources/images/kf2ico.png"));
        primaryStage.setTitle(Constants.APPLICATION_TITLE);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(1280);
        primaryStage.setMinHeight(800);
        primaryStage.show();
        TimerTask timeListener = new TimeListener();
        timer = new Timer();
        timer.schedule(timeListener, 0, 30000);
        this.primaryStage = primaryStage;
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
