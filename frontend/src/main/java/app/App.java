package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import registrationForm.RegistrationController;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(App.class.getResource("/registrationForm.fxml"));
        stage.setTitle("Register");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        stage.setScene(scene);
        stage.show();
        RegistrationController.stage = stage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
