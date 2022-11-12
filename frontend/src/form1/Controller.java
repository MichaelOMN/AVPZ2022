package form1;

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
    private TableColumn<String, String> addressColumn;
    @FXML
    private TableColumn<String, CheckBox> tickColumn;

    @FXML
    public void initialize() {
        addressColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));
        tickColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(new CheckBox("")));

        List<String> list = new LinkedList<>();
        list.add("10 Street, Big flat");
        list.add("21 Svobody, Small flat");
        list.add("10 Sumskaya, Medium flat");
        list.add("21 Svobody, Two floors flat");

        table.setItems(FXCollections.observableList(list));
    }
}
