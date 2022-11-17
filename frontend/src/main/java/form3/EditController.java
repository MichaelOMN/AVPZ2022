package form3;

import app.App;
import com.google.gson.Gson;
import form1.Form1Controller;
import form1.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import server.REST;
import server.Util;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.prefs.Preferences;

public class EditController {

    @FXML
    private TextField locationField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField roomCountField;
    @FXML
    private TextField sizeField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TableView<String> facilitiesTable;
    @FXML
    private TableColumn<String, String> facilitiesColumn;

    private Form1Controller.TableData tableData;
    private boolean edit;
    public static Stage stage;

    public EditController(boolean edit, Form1Controller.TableData tableData) {
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

           /* String[] facilities = tableData.tags.split(";");
            List<String> facilitiesList = Arrays.asList(facilities);
            facilitiesTable.setItems(FXCollections.observableArrayList(facilitiesList));*/
        }


    }
    @FXML
    private void save() {
        tableData.description = descriptionField.getText();
        tableData.location = locationField.getText();
        tableData.price = Integer.parseInt(priceField.getText());
        tableData.room_count = Integer.parseInt(roomCountField.getText());
        tableData.size = Double.parseDouble(sizeField.getText());

      /*  ObservableList<String> items = facilitiesTable.getItems();
        StringBuilder tagsResult = new StringBuilder();
        for (String s : items) {
            tagsResult.append(s);
            tagsResult.append(";");
        }

        tagsResult.setLength(tagsResult.length() - 1);

        tableData.tags = tagsResult.toString();*/
        tableData.tags = "";

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
            Parent root = FXMLLoader.load(Main.class.getResource("/form1.fxml"));
            stage.setTitle("Form1");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            Form1Controller.stage = stage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void close() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("/form1.fxml"));
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
