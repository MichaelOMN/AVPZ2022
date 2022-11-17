package registrationForm;

import com.google.gson.Gson;
import form1.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import loginForm.LoginController;
import server.REST;

public class RegistrationController {
    @FXML
    private TextField telephoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField pibField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private CheckBox removeViewerField;
    @FXML
    private Button registerButton;
    @FXML
    public static Stage stage;


    @FXML
    private void initialize() {

    }

    private static class UserData {
        private String tel;
        private String password;
        private String pib;

        private String description;
        private String location;
        private boolean is_remote_viewer;
    }

    @FXML
    private void registerButtonClick(ActionEvent e) {
        UserData data = new UserData();

        data.tel = telephoneField.getText();
        data.password = passwordField.getText();
        data.pib = pibField.getText();
        data.description = descriptionField.getText();
        data.location = locationField.getText();
        data.is_remote_viewer = removeViewerField.isSelected();

        String request = new Gson().toJson(data);
        String response = REST.register(request);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration successful");
        alert.setContentText(response);
        alert.show();
    }

    @FXML
    private void goToLoginPage(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/loginForm.fxml"));
            stage.setTitle("Login");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            LoginController.stage = stage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
