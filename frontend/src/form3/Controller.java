package form3;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.LinkedList;
import java.util.List;

public class Controller {
    @FXML
    private TableView<Info> infoTable;
    @FXML
    private TableView<String> facilitiesTable;
    @FXML
    private TableColumn<Info, String> categoryColumn;
    @FXML
    private TableColumn<Info, String> valueColumn;
    @FXML
    private TableColumn<Info, Label> editColumn;
    @FXML
    private TableColumn<String, String> facilitiesColumn;

    private static class Info {
        String category;
        String value;
    }

    @FXML
    private void initialize() {
        List<Info> infoList = new LinkedList<>();
        Info info1 = new Info();
        info1.category = "Location";
        info1.value = "Sumskaya 19";

        Info info2 = new Info();
        info2.category = "Apartment size";
        info2.value = "150 m2";

        Info info3 = new Info();
        info3.category = "Number of rooms";
        info3.value = "4";

        Info info4 = new Info();
        info4.category = "Contacts";
        info4.value = "063857893";

        infoList.add(info1);
        infoList.add(info2);
        infoList.add(info3);
        infoList.add(info4);


        categoryColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().category));
        valueColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().value));

        editColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Info, Label>, ObservableValue<Label>>() {
            @Override
            public ObservableValue<Label> call(TableColumn.CellDataFeatures<Info, Label> param) {
                Label label = new Label();
                label.setText("Edit");
                label.setTextFill(Color.BLUE);
                label.setStyle("-fx-underline: true;");


                return new SimpleObjectProperty<>(label);
            }
        });

        infoTable.setItems(FXCollections.observableList(infoList));


        facilitiesColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));

        List<String> facilities = new LinkedList<>();
        facilities.add("Microwave");
        facilities.add("3 TV's");
        facilities.add("Pets allowed");
        facilities.add("No smoking");
        facilities.add("Near the subway");

        facilitiesTable.setItems(FXCollections.observableList(facilities));
    }
}
