package compare;

import adListForm.AdListController;
import app.App;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CompareController {
    public static Stage stage;

    @FXML
    private TableView<Row> table;

    private ObservableList<Row> items;

    public static List<AdListController.TableData> compareList = new ArrayList<>();

    private final ObservableList<Row> rows = FXCollections.observableArrayList();

    private static class Row {
        List<String> data = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        TableColumn<Row, String> zeroColumn = new TableColumn<>();
        zeroColumn.setCellValueFactory(new Callback<>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Row, String> t) {
                return new SimpleStringProperty(t.getValue().data.get(0));
            }
        });

        table.getColumns().add(zeroColumn);


        for (int i = 0; i < compareList.size(); i++) {
            TableColumn<Row, String> c = new TableColumn<>();
            int finalI = i + 1;
            c.setCellValueFactory(new Callback<>() {
                @Override
                public ObservableValue<String> call(TableColumn.CellDataFeatures<Row, String> t) {
                    return new SimpleStringProperty(t.getValue().data.get(finalI));
                }
            });


            table.getColumns().add(c);

            c.setPrefWidth(200);
        }

        Row row1 = new Row();
        row1.data.add("");
        Row row2 = new Row();
        row2.data.add("Description");
        Row row3 = new Row();
        row3.data.add("Location");
        Row row4 = new Row();
        row4.data.add("Price");
        Row row5 = new Row();
        row5.data.add("Rooms");
        Row row6 = new Row();
        row6.data.add("Area");

        items = FXCollections.observableArrayList();


        int i = 0;
        for (AdListController.TableData a : compareList) {
            table.getColumns().get(i + 1).setText("Flat with ID: " + a.id);
            row1.data.add(String.valueOf(i));
            row2.data.add(a.description);
            row3.data.add(a.location);
            row4.data.add(a.price);
            row5.data.add(a.roomCount);
            row6.data.add(a.area);
            i++;
        }

        //   items.add(row1);
        items.add(row2);
        items.add(row3);
        items.add(row4);
        items.add(row5);
        items.add(row6);

        table.setItems(items);

        zeroColumn.setCellFactory(new Callback<TableColumn<Row, String>, TableCell<Row, String>>() {
            @Override
            public TableCell<Row, String> call(TableColumn<Row, String> rowStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(String item, boolean e) {
                        if (e) {
                            setText("");
                        } else {
                            setStyle("-fx-font-weight: bold");
                            setText(item);
                        }
                    }
                };
            }
        });
    }

    @FXML
    public void back() {
        try {
            Parent root = FXMLLoader.load(App.class.getResource("/form1.fxml"));
            stage.setTitle("Compare");
            Scene scene = new Scene(root);
            scene.getStylesheets().add("style.css");
            stage.setScene(scene);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void clear() {
        items.clear();
        table.getColumns().clear();
    }
}
