package form3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("form3.fxml"));
        primaryStage.setTitle("Form3");
        Scene scene = new Scene(root, 1300, 700);
        scene.getStylesheets().add("res/style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
