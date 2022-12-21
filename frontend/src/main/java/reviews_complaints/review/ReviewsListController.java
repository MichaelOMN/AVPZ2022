package reviews_complaints.review;

import adListForm.AdListController;
import app.App;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
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

import java.lang.reflect.Type;
import java.util.List;
import java.util.prefs.Preferences;

public class ReviewsListController {
    public static Stage stage;

    private final AdListController.TableData tableData;

    private ObservableList<TableData> items = FXCollections.observableArrayList();

    private static class TableData {
        String description, rate, date, user, id;

        @Override
        public boolean equals(Object other) {
            if (other == null) return false;

            if (!(other instanceof TableData)) return false;

            TableData otherData = (TableData) other;

            return otherData.id.equals(id);
        }
    }

    public ReviewsListController(AdListController.TableData tableData) {
        this.tableData = tableData;
    }


    @FXML
    private TableView<TableData> table;
    @FXML
    private TableColumn<TableData, String> textColumn;
    @FXML
    private TableColumn<TableData, String> rateColumn;
    @FXML
    private TableColumn<TableData, String> dateColumn;
    @FXML
    private TableColumn<TableData, String> userColumn;

    private static class Comment {
        String adv_id, description, id, rate, user_id, when_posted;
    }

    @FXML
    public void initialize() {
        textColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().description));
        rateColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().rate));
        dateColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().date));
        userColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().user));


        String commentsResult = "";

        try {
            commentsResult = REST.getCommentsOnAd(tableData.id);

            JsonObject json = new Gson().fromJson(commentsResult, JsonObject.class);
            JsonElement element = json.get("Comments on this adv").getAsJsonArray();
            Type listType = new TypeToken<List<Comment>>() {
            }.getType();
            List<Comment> comments = new Gson().fromJson(element, listType);
            System.out.println(commentsResult);

            for (Comment c : comments) {
                TableData t = new TableData();
                t.date = c.when_posted;
                t.description = c.description;
                t.rate = c.rate;
                t.user = c.user_id;
                t.id = c.id;
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
            AdListController.stage = stage;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void deleteReview() {
        String id = table.getSelectionModel().getSelectedItem().id;

        try {
            REST.deleteComment(Preferences.userNodeForPackage(App.class).get("token", ""), Integer.parseInt(id));
        } catch (Exception e) {
            e.printStackTrace();
        }

        items.remove(table.getSelectionModel().getSelectedItem());
    }
}
