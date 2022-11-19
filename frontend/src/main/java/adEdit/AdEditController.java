package adEdit;

import app.App;
import com.google.gson.Gson;
import form1.Form1Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.REST;

import java.util.prefs.Preferences;

public class AdEditController {
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField roomCountField;
    @FXML
    private TextField sizeField;
    @FXML
    private TextField tagsField;

    public static Stage stage;

    private final boolean edit;
    private Form1Controller.TableData tableData;

    public AdEditController(boolean edit, Form1Controller.TableData tableData) {
        this.edit = edit;
        this.tableData = tableData;
    }

    @FXML
    private void initialize() {
        if (edit) {
            descriptionField.setText(tableData.description);
            locationField.setText(tableData.location);
            priceField.setText(String.valueOf(tableData.price));
            roomCountField.setText(String.valueOf(tableData.room_count));
            sizeField.setText(String.valueOf(tableData.size));
            tagsField.setText(tableData.tags);
        }
    }

    @FXML
    private void save() {
        tableData.description = descriptionField.getText();
        tableData.location = locationField.getText();
        tableData.price = Integer.parseInt(descriptionField.getText());
        tableData.room_count = Integer.parseInt(roomCountField.getText());
        tableData.size = Double.parseDouble(sizeField.getText());
        tableData.tags = tagsField.getText();

        String body = new Gson().toJson(tableData);
        Preferences preferences = Preferences.userNodeForPackage(App.class);
        String token = preferences.get("token", "");

        if (edit) {
            try {
                REST.editAdv(token, body, tableData.id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                String response = REST.saveAdv(token, body);
                tableData.id = Integer.parseInt(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
