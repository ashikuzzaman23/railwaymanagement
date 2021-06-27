package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Created by rafid on 7/7/2017.
 */
public class Select {
    @FXML
    public void adminAction(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Admin.fxml"));
        Main.window.setTitle("Admin");
        Main.window.setScene(new Scene(root, 640, 480));
        Main.window.show();
    }
    @FXML
    public void userAction(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("User.fxml"));
        Main.window.setTitle("User");
        Main.window.setScene(new Scene(root, 640, 480));
        Main.window.show();
    }
}
