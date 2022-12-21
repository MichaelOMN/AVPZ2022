package reviews_complaints.compaint;

import adListForm.AdListController;
import app.App;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import registrationForm.RegistrationController;
import server.REST;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ComplaintsListController  {
    private static AdListController.TableData tableData;
    public static Stage stage;
    private static ObservableList<TableData> items;

    public ComplaintsListController(AdListController.TableData tableData) {
        ComplaintsListController.tableData = tableData;
    }

    public static class TableData {
        public String user;
        public String desc;
        public String date;
    }

    @FXML
    private TableView<TableData> table;

    @FXML
    private TableColumn<TableData, String> userCol;
    @FXML
    private TableColumn<TableData, String> descCol;
    @FXML
    private TableColumn<TableData, String> dateCol;

    private static class Complaint {
        String adv_id, description, id, user_id, when_complained;
    }

    @FXML
    public void initialize() {
        userCol.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().user));
        descCol.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().desc));
        dateCol.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().date));

        String id = tableData.id;

        try {
            String complaintsResult = REST.getComplaintsForAd(id);

            System.out.println(complaintsResult);
            JsonObject json = new Gson().fromJson(complaintsResult, JsonObject.class);
            JsonElement element = json.get("List of all complaints").getAsJsonArray();
            Type listType = new TypeToken<List<Complaint>>() {
            }.getType();
            List<Complaint> complaints = new Gson().fromJson(element, listType);
            items = FXCollections.observableArrayList();

            for (Complaint c : complaints) {
                TableData t = new TableData();
                t.date = c.when_complained;
                t.desc = c.description;
                t.user = c.user_id;

                items.add(t);
            }

            table.setItems(items);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void close() {

        try {
            Parent root = FXMLLoader.load(App.class.getResource("/adList.fxml"));
            stage.setTitle("All Ads");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
            stage.show();
            AdListController.stage = stage;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
