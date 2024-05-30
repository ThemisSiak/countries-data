import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("JavaFX!");
        Scene scene = new Scene(label, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Example");
        primaryStage.show();
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}
