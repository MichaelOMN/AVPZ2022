package adListForm;

import app.App;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import compare.CompareController;
import form1.Form1Controller;
import form3.EditController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import reviews_complaints.compaint.AddComplaintController;
import reviews_complaints.compaint.ComplaintsListController;
import reviews_complaints.review.AddReviewController;
import reviews_complaints.review.ReviewsListController;
import server.REST;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class AdListController {
    public static Stage stage;

    private ObservableList<TableData> items;
    private final ObservableList<TableData> searchList = FXCollections.observableArrayList();
    private TableData tableData;

    public static class TableData {
        public String description, location, price, roomCount, area, id;

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof TableData)) return false;

            TableData otherData = (TableData) o;
            return otherData.id.equals(id);
        }
    }


    public void initialize() {
        ObservableList<String> comboItems = FXCollections.observableArrayList();
        comboItems.add("=");
        comboItems.add("!=");
        comboItems.add("x");
        comboItems.add(">");
        comboItems.add("<");

        descCombo.setItems(comboItems);
        locCombo.setItems(comboItems);
        priceCombo.setItems(comboItems);
        roomsCombo.setItems(comboItems);
        areaCombo.setItems(comboItems);


        descColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().description));
        locColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().location));
        priceColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().price));
        roomColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().roomCount));
        areaColumn.setCellValueFactory(t -> new SimpleStringProperty(t.getValue().area));

        ContextMenu menu = new ContextMenu();

        MenuItem addComplaint = new MenuItem("Add complaint");
        addComplaint.setOnAction((ActionEvent event) -> {
            tableData = table.getSelectionModel().getSelectedItem();
            addComplaint();
        });

        menu.getItems().add(addComplaint);

        MenuItem viewComplaints = new MenuItem("View complaints");
        viewComplaints.setOnAction((ActionEvent event) -> {
            tableData = table.getSelectionModel().getSelectedItem();
            viewComplaints();
        });

        menu.getItems().add(viewComplaints);

        MenuItem addReview = new MenuItem("Add review");
        addReview.setOnAction((ActionEvent event) -> {
            tableData = table.getSelectionModel().getSelectedItem();
            addReview();
        });

        menu.getItems().add(addReview);

        MenuItem viewReviews = new MenuItem("View reviews");
        viewReviews.setOnAction((ActionEvent event) -> {
            tableData = table.getSelectionModel().getSelectedItem();
            viewReviews();
        });

        menu.getItems().add(viewReviews);

        MenuItem addToFav = new MenuItem("Add to favourites");
        addToFav.setOnAction((ActionEvent event) -> {
            tableData = table.getSelectionModel().getSelectedItem();
            addToFav();
        });


        menu.getItems().add(addToFav);

        MenuItem addToComparison = new MenuItem("Add to comparison");
        addToComparison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                CompareController.compareList.add(table.getSelectionModel().getSelectedItem());
            }
        });

        menu.getItems().add(addToComparison);


        table.setContextMenu(menu);


        String allAds = "";
        try {
            allAds = REST.getAllAds();
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObject json = new Gson().fromJson(allAds, JsonObject.class);
        JsonElement element = json.get("List of your ads").getAsJsonArray();
        Type listType = new TypeToken<List<Ad>>() {
        }.getType();
        List<Ad> ads = new Gson().fromJson(element, listType);

        items = FXCollections.observableArrayList();

        for (Ad a : ads) {
            TableData t = new TableData();

            t.area = a.size;
            t.description = a.description;
            t.location = a.location;
            t.roomCount = a.room_count;
            t.price = a.price;
            t.id = a.id;

            items.add(t);
        }

        table.setItems(items);
    }

    private void addToFav() {
        String body = "{\"adv_id\": " + tableData.id + "}";
        System.out.println(body);


        try {
            System.out.println(REST.addFavourite(Preferences.userNodeForPackage(App.class).get("token", ""), body));;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TableView<TableData> table;

    @FXML
    private TableColumn<TableData, String> descColumn;
    @FXML
    private TableColumn<TableData, String> locColumn;
    @FXML
    private TableColumn<TableData, String> priceColumn;
    @FXML
    private TableColumn<TableData, String> roomColumn;
    @FXML
    private TableColumn<TableData, String> areaColumn;


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

    public static class Ad {
        public String description, id, location, photo_file, price, room_count, size, tags;
    }


    private void addComplaint() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/add_complaint.fxml"));
            AddComplaintController controller = new AddComplaintController(tableData);
            AddComplaintController.stage = stage;
            loader.setController(controller);

            Parent root = loader.load();
            stage.setTitle("Add Complaint");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void viewComplaints() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/complaints_list.fxml"));
            ComplaintsListController controller = new ComplaintsListController(tableData);
            ComplaintsListController.stage = stage;
            loader.setController(controller);

            Parent root = loader.load();
            stage.setTitle("Complaints");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addReview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/add_review.fxml"));
            AddReviewController controller = new AddReviewController(tableData);
            AddReviewController.stage = stage;
            loader.setController(controller);

            Parent root = loader.load();
            stage.setTitle("Add Review");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void viewReviews() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/reviews_list.fxml"));
            ReviewsListController controller = new ReviewsListController(tableData);
            ReviewsListController.stage = stage;
            loader.setController(controller);

            Parent root = loader.load();
            stage.setTitle("All Reviews");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private TextField descFilter, locFilter, priceFilter, roomsFilter, areaFilter;

    @FXML
    private ComboBox<String> descCombo, locCombo, priceCombo, roomsCombo, areaCombo;

    @FXML
    public void filter() {
        searchList.clear();

        for (TableData t : items) {
            boolean toAdd = true;

            boolean cond1, cond2, cond3, cond4, cond5;

            int idx1 = descCombo.getSelectionModel().getSelectedIndex();
            int idx2 = locCombo.getSelectionModel().getSelectedIndex();
            int idx3 = priceCombo.getSelectionModel().getSelectedIndex();
            int idx4 = roomsCombo.getSelectionModel().getSelectedIndex();
            int idx5 = areaCombo.getSelectionModel().getSelectedIndex();

            cond1 = cond(idx1, t.description, descFilter.getText());
            cond2 = cond(idx2, t.location, locFilter.getText());
            cond3 = cond(idx3, t.price, priceFilter.getText());
            cond4 = cond(idx4, t.roomCount, roomsFilter.getText());
            cond5 = cond(idx5, t.area, areaFilter.getText());


            if (!descFilter.getText().isEmpty()) {
                if (!cond1) {
                    toAdd = false;
                }
            }

            if (!locFilter.getText().isEmpty()) {
                if (!cond2) {
                    toAdd = false;
                }
            }

            if (!priceFilter.getText().isEmpty()) {
                if (!cond3) {
                    toAdd = false;
                }
            }

            if (!roomsFilter.getText().isEmpty()) {
                if (!cond4) {
                    toAdd = false;
                }
            }

            if (!areaFilter.getText().isEmpty()) {
                if (!cond5) {
                    toAdd = false;
                }
            }

            if (toAdd) {
                searchList.add(t);
            }
        }


        table.setItems(searchList);
    }

    @FXML
    public void reset() {
        table.setItems(items);
        descCombo.getSelectionModel().clearSelection();
        locCombo.getSelectionModel().clearSelection();
        priceCombo.getSelectionModel().clearSelection();
        roomsCombo.getSelectionModel().clearSelection();
        areaCombo.getSelectionModel().clearSelection();

        descFilter.clear();
        locFilter.clear();
        priceFilter.clear();
        roomsFilter.clear();
        areaFilter.clear();
    }

    private boolean cond(int idx, String text, String filter) {
        switch (idx) {
            case 0:
                return text.equals(filter);
            case 1:
                return !text.equals(filter);
            case 2:
                return text.contains(filter);
            case 3:
                return Double.parseDouble(text) >  Double.parseDouble(filter);
            case 4:
                return  Double.parseDouble(text) <  Double.parseDouble(filter);
            default:
              return false;
        }
    }
}
