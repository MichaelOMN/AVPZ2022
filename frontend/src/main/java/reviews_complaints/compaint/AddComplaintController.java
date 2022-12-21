package reviews_complaints.compaint;

import adListForm.AdListController;
import app.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import server.REST;

import java.util.prefs.Preferences;

public class AddComplaintController {
    public static Stage stage;

    @FXML
    private TextArea complaintText;

    private final AdListController.TableData tableData;


    public AddComplaintController(AdListController.TableData tableData) {
        this.tableData = tableData;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void addComplaint() {
        String text = complaintText.getText();
        String body = "{" + "\"description\": \""+text+"\", " + "\"adv_id\": "+ tableData.id + "}";

        String token = Preferences.userNodeForPackage(App.class).get("token", "");

        try {
            System.out.println(REST.addComplaint(token, body));
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
