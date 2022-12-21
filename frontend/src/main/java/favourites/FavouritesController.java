package favourites;

import adListForm.AdListController;
import app.App;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import form1.Form1Controller;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import server.REST;
import server.Util;

import java.lang.reflect.Type;
import java.util.List;
import java.util.prefs.Preferences;

public class FavouritesController {
    public static Stage stage;
    private ObservableList<AdListController.TableData> items = FXCollections.observableArrayList();


    @FXML
    private TableView<AdListController.TableData> table;
    @FXML
    private TableColumn<AdListController.TableData, String> descColumn;
    @FXML
    private TableColumn<AdListController.TableData, String> locColumn;
    @FXML
    private TableColumn<AdListController.TableData, String> priceColumn;
    @FXML
    private TableColumn<AdListController.TableData, String> roomColumn;
    @FXML
    private TableColumn<AdListController.TableData, String> areaColumn;

    private static class QueryData {
        String adv_id, id, user_id;
    }


    @FXML
    public void initialize() {
        descColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().description));
        locColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().location));
        priceColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().price));
        roomColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().roomCount));
        areaColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().area));

        try {
            String fav = REST.getFavourites(Preferences.userNodeForPackage(App.class).get("token", ""));
            JsonObject json = new Gson().fromJson(fav, JsonObject.class);
            JsonElement element = json.get("List of your favourites").getAsJsonArray();
            Type listType = new TypeToken<List<QueryData>>() {
            }.getType();
            List<QueryData> ads = new Gson().fromJson(element, listType);

            System.out.println(fav);

            for (QueryData a : ads) {
                AdListController.TableData t = new AdListController.TableData();

                String adResult = REST.getAdById(a.adv_id);
                Gson gson = new Gson();
                String val = Util.getValueByKeyJSON(adResult, "Adv info");

                val += "}";
                val = "{id:\""+a.adv_id+"\", " + val;
                AdListController.Ad ad = gson.fromJson(val, AdListController.Ad.class);



                t.id = a.id;
                t.area = ad.size;
                t.description = ad.description;
                t.price = ad.price;
                t.location = ad.location;
                t.roomCount = ad.room_count;

                items.add(t);
            }

            table.setItems(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void back() {
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
    public void deleteFavourite() {
        AdListController.TableData t = table.getSelectionModel().getSelectedItem();
        try {
            System.out.println(REST.deleteFavourite(Preferences.userNodeForPackage(App.class).get("token", ""), Integer.parseInt(t.id)));;
        } catch (Exception e) {
            e.printStackTrace();
        }

        items.remove(t);
    }
}
