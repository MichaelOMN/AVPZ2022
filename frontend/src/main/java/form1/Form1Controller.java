package form1;

import app.App;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import form3.EditController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import server.REST;
import server.Util;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.List;
import java.util.prefs.Preferences;

public class Form1Controller {
    @FXML
    private TableView<TableData> table;
    @FXML
    private TableColumn<TableData, String> addressColumn;
    @FXML
    private TableColumn<TableData, CheckBox> tickColumn;
    public static Stage stage;
    private ObservableList<TableData> items;
    @FXML
    private TextField descriptionField;
    @FXML
    private ImageView profileView;

    public static class TableData {
        public String description;
        public int id;
        public String location;
        public int price;
        public int room_count;
        public double size;
        public String tags;
    }


    @FXML
    public void initialize() {
        addressColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().location + ", " + c.getValue().description));
        tickColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(new CheckBox("")));


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
        Type listType = new TypeToken<List<TableData>>() {
        }.getType();
        List<TableData> list = new Gson().fromJson(element, listType);


        table.setItems(FXCollections.observableArrayList(list));
        items = table.getItems();


        try {
            Preferences prefs = Preferences.userNodeForPackage(App.class);
            String tkn = prefs.get("token", "");
            String info = REST.getUserInfo(tkn);
            String picId = Util.getValueByKeyJSON(info, "photo_file");

            String description = Util.getValueByKeyJSON(info, "description");
            descriptionField.setText(description);

            Image image = new Image(REST.MAIN_URL + "pic/" + picId);//new Image(new ByteArrayInputStream(pic.getBytes(StandardCharsets.UTF_8)));
            profileView.setImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createAd() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/editingForm.fxml"));
            EditController controller = new EditController(false, new TableData());
            EditController.stage = stage;
            loader.setController(controller);

            Parent root = loader.load();
            stage.setTitle("Create Ad");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void editAd() {

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/editingForm.fxml"));
            EditController controller = new EditController(true, table.getSelectionModel().getSelectedItem());
            EditController.stage = stage;
            loader.setController(controller);

            Parent root = loader.load();
            stage.setTitle("Edit Ad");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteAd() {
        TableData tableData = table.getSelectionModel().getSelectedItem();
        int index = table.getSelectionModel().getSelectedIndex();
        items.remove(index);

        int adId = tableData.id;

        Preferences preferences = Preferences.userNodeForPackage(App.class);
        String token = preferences.get("token", "");
        String text = "error";
        try {
            text = REST.deleteAdv(token, adId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void acceptDescription() {
        Preferences preferences = Preferences.userNodeForPackage(App.class);
        String token = preferences.get("token", "");
        try {
            System.out.println(REST.account(token, "{\"description\": \"" + descriptionField.getText() + "\"}"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void selectImage() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(stage);
        Preferences preferences = Preferences.userNodeForPackage(App.class);

        if (file != null) {
            try {
                profileView.setImage(new Image(file.toURI().toURL().toExternalForm()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String path = file.getAbsolutePath();
            try {
                REST.sendPic(preferences.get("token", ""), path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
