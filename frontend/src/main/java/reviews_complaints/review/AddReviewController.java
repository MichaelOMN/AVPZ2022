package reviews_complaints.review;

import adListForm.AdListController;
import app.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;
import server.REST;

import java.util.prefs.Preferences;

public class AddReviewController {
    public static Stage stage;

    @FXML
    private TextArea reviewText;

    @FXML
    private Slider rateSlider;
    @FXML
    private Label rateLabel;

    private final AdListController.TableData tableData;


    public AddReviewController(AdListController.TableData tableData) {
        this.tableData = tableData;
    }

    @FXML
    public void initialize() {
        rateSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                rateLabel.setText(String.valueOf((int)Math.round(rateSlider.getValue())));
            }
        });
    }

    @FXML
    public void addReview() {
        String text = reviewText.getText();
        String body = "{" + "\"description\": \""+text+"\", " +"\"rate\" : "+ (int)Math.round(rateSlider.getValue())+" ," + "\"adv_id\": "+ tableData.id + "}";

        String token = Preferences.userNodeForPackage(App.class).get("token", "");

        try {
            System.out.println(REST.addComment(token, body));
        } catch (Exception e) {
            e.printStackTrace();
        }

        cancel();
    }

    @FXML
    public void cancel() {
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
}
