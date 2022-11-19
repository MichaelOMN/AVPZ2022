package form3;

import app.App;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import form1.Form1Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import server.REST;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
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
    private ImageView adPhoto;
    @FXML
    private Button leftButton, rightButton;
    @FXML
    private WebView webView;
    @FXML
    private TextArea facilitiesField;

    private final Form1Controller.TableData tableData;
    private final boolean edit;
    public static Stage stage;

    public EditController(boolean edit, Form1Controller.TableData tableData) {
        this.edit = edit;
        this.tableData = tableData;
    }


    @FXML
    private void initialize() {

        rightButton.setVisible(false);
        leftButton.setVisible(false);

        if (edit) {
            facilitiesField.setText(tableData.tags);
            webView.getEngine().loadContent("<iframe src=\"https://maps.google.com/maps?q=" + tableData.location + "&amp;t=&amp;z=14&amp;ie=UTF8&amp;iwloc=&amp;output=embed\" width=100% height=100% allowfullscreen></iframe>\n", "text/html");
            descriptionField.setText(tableData.description);
            locationField.setText(tableData.location);
            priceField.setText(String.valueOf(tableData.price));
            roomCountField.setText(String.valueOf(tableData.room_count));
            sizeField.setText(String.valueOf(tableData.size));


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
            for (Form1Controller.TableData data : list) {
                if (data.id == tableData.id) {
                    photo_file = tableData.photo_file;
                    break;
                }
            }


            Image image = new Image(REST.MAIN_URL + "pic/" + photo_file);//new Image(new ByteArrayInputStream(pic.getBytes(StandardCharsets.UTF_8)));
            adPhoto.setImage(image);
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


        Preferences preferences = Preferences.userNodeForPackage(App.class);
        String token = preferences.get("token", "");

        String text = "error";



      /*  ObservableList<String> items = facilitiesTable.getItems();
        StringBuilder tagsResult = new StringBuilder();
        for (String s : items) {
            tagsResult.append(s);
            tagsResult.append(";");
        }

        tagsResult.setLength(tagsResult.length() - 1);

        tableData.tags = tagsResult.toString();*/
        tableData.tags = facilitiesField.getText();

        String body = new Gson().toJson(tableData);

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

    @FXML
    private void selectAdImage() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(stage);
        Preferences preferences = Preferences.userNodeForPackage(App.class);

        if (file != null) {
            try {
                adPhoto.setImage(new Image(file.toURI().toURL().toExternalForm()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String path = file.getAbsolutePath();


            try {
                System.out.println(REST.sendPicOfAd(preferences.get("token", ""), path, tableData.id));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
