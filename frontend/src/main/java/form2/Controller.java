package form2;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.LinkedList;
import java.util.List;

public class Controller {
    @FXML
    private TableView<String> table;
    @FXML
    private TableColumn<String, String> nameColumn;
    @FXML
    private TableColumn<String, CheckBox> likeColumn;


    @FXML
    public void initialize() {
        List<String> list = new LinkedList<>();
        list.add("Bob Nuture: Number 023944");
        list.add("Svetlana Uher: Number 455433");
        list.add("John Kicu: Number 0949554");
        list.add("Amanda Walker: Number 0462773");


        nameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));
        likeColumn.setCellValueFactory(c->new SimpleObjectProperty<>(new CheckBox()));

        table.setItems(FXCollections.observableList(list));
    }
}
