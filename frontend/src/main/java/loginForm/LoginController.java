package loginForm;

import app.App;
import com.google.gson.Gson;
import form1.Form1Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.REST;
import server.Util;

import java.util.prefs.Preferences;


public class LoginController {
    @FXML
    private TextField telephoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    public static Stage stage;

    @FXML
    private void initialize() {

    }

    private static class LoginData {
        private String tel;
        private String password;
    }

    @FXML
    private void loginButtonClick(ActionEvent e) {
        LoginData data = new LoginData();
        data.tel = telephoneField.getText();
        data.password = telephoneField.getText();

        String request = new Gson().toJson(data);
        String response = REST.login(request);
        String token = Util.getValueByKeyJSON(response, "token");

        Preferences preferences = Preferences.userNodeForPackage(App.class);
        preferences.put("token", token);

        try {
            Parent root = FXMLLoader.load(App.class.getResource("/form1.fxml"));
            stage.setTitle("Form1");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            Form1Controller.stage = stage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
