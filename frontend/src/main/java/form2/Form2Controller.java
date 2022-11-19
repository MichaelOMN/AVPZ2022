package form2;

import app.App;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import form1.Form1Controller;
import form3.EditController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import server.REST;

import java.lang.reflect.Type;
import java.util.List;
import java.util.prefs.Preferences;

public class Form2Controller {
    @FXML
    private TableView<RemoterData> table;
    @FXML
    private TableColumn<RemoterData, String> nameColumn;
    @FXML
    private TableColumn<RemoterData, CheckBox> likeColumn;
    @FXML
    private TextField descriptionField;
    @FXML
    private WebView webView;
    @FXML
    private ImageView imageView;

    public static Stage stage;

    private final Form1Controller.TableData data;

    private static class RemoterData {
        private String location;
        private String description;
        private String pib;
        private String tel;
    }

    public Form2Controller(Form1Controller.TableData data) {
        this.data = data;
    }



    @FXML
    public void initialize() {


        Preferences preferences = Preferences.userNodeForPackage(App.class);
        String token = preferences.get("token", "");

        String text = "error";

        try {
            text = REST.advs(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject json = new Gson().fromJson(text, JsonObject.class);
        JsonElement element = json.get("List of your ads").getAsJsonArray();
        Type listType = new TypeToken<List<Form1Controller.TableData>>() {
        }.getType();
        List<Form1Controller.TableData> list = new Gson().fromJson(element, listType);

        String photo_file = "";
        for (Form1Controller.TableData d : list) {
            if (d.id == data.id) {
                photo_file = data.photo_file;
                break;
            }
        }


        Image image = new Image(REST.MAIN_URL + "pic/" + photo_file);
        imageView.setImage(image);






        webView.getEngine().loadContent("<iframe src=\"https://maps.google.com/maps?q=" + data.location + "&amp;t=&amp;z=14&amp;ie=UTF8&amp;iwloc=&amp;output=embed\" width=100% height=100% allowfullscreen></iframe>\n", "text/html");



        descriptionField.setText(data.description);

        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().pib + ": " + c.getValue().tel));

        likeColumn.setCellValueFactory(c->new SimpleObjectProperty<>(new CheckBox()));

         json = new Gson().fromJson(REST.remoters(), JsonObject.class);
         element = json.get("List of remoters available").getAsJsonArray();
         listType = new TypeToken<List<RemoterData>>() {
        }.getType();
        List<RemoterData> list1 = new Gson().fromJson(element, listType);


        table.setItems(FXCollections.observableArrayList(list1));
    }

    @FXML
    private void close() {
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
