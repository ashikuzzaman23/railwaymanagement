package sample;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;


public class Admin implements Initializable {
    @FXML
    private TextField non_ac_seat;

    @FXML
    private ChoiceBox<String> ampm1;

    @FXML
    private TextField ac_normal_fare;

    @FXML
    private ChoiceBox<String> ampm2;


    @FXML
    private TextField train_name;

    @FXML
    private TextField end_time;

    @FXML
    private TextField ac_cabin_cabin;

    @FXML
    private TextField ac_cabin_fare;

    @FXML
    private TextField non_ac;

    @FXML
    private TextField ac_normal_seat;

    @FXML
    private TextField start_time;

    @FXML
    private TextField non_ac_fare;

    @FXML
    private TextField from;

    @FXML
    private TextField to;

    @FXML
    private TextField ac_cabin;

    @FXML
    private TextField ac_normal;

    @FXML
    private Label msz;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        train_name.setText("");
        start_time.setText("");
        end_time.setText("");
        from.setText("");
        to.setText("");
        ac_normal.setText("");
        ac_normal_seat.setText("");
        ac_normal_fare.setText("");
        ac_cabin.setText("");
        ac_cabin_cabin.setText("");
        ac_cabin_fare.setText("");
        non_ac.setText("");
        non_ac_seat.setText("");
        non_ac_fare.setText("");
        ampm1.setItems(FXCollections.observableArrayList("AM","PM"));
        ampm1.getSelectionModel().selectFirst();
        ampm2.setItems(FXCollections.observableArrayList("AM","PM"));
        ampm2.getSelectionModel().selectFirst();
    }

    @FXML
    public void submitAction(ActionEvent event) throws Exception {
        if(Main.jedis.exists(train_name.getText())) {
            String fromS = from.getText();
            String toS = to.getText();
            if(!fromS.equals("") && !toS.equals("") && !start_time.getText().equals("")
                    && !end_time.getText().equals("")) {
                Main.jedis.sadd(fromS + "_" + toS, train_name.getText());
                String timeS = start_time.getText() + ampm1.getValue();
                String timeE = end_time.getText() + ampm2.getValue();
                String time = timeS + "_" + timeE;
                Main.jedis.sadd("locations", fromS, toS);
                Main.jedis.sadd(train_name.getText() + "_" + fromS + "_" + toS + "_time", time);
                String key = train_name.getText() + "_" + fromS + "_" + toS + "_" + "fare";
                if (!ac_normal_fare.getText().equals("")) {
                    Main.jedis.hset(key, "AC_normal", ac_normal_fare.getText());
                }
                if (!ac_cabin_fare.getText().equals("")) {
                    Main.jedis.hset(key, "AC_cabin", ac_cabin_fare.getText());
                }
                if (!non_ac_fare.getText().equals("")) {
                    Main.jedis.hset(key, "non_AC", non_ac_fare.getText());
                }
            }
            if(!ac_normal.getText().equals("")) {
                Main.jedis.hset(train_name.getText(), "AC_normal_compartment", ac_normal.getText());
            }
            if(!ac_normal_seat.getText().equals("")) {
                Main.jedis.hset(train_name.getText(), "AC_normal_seat", ac_normal_seat.getText());
            }
            if(!ac_cabin.getText().equals("")) {
                Main.jedis.hset(train_name.getText(), "AC_cabin_compartment", ac_cabin.getText());
            }
            if(!ac_cabin_cabin.getText().equals("")) {
                Main.jedis.hset(train_name.getText(), "AC_cabin", ac_cabin_cabin.getText());
            }
            if(!non_ac.getText().equals("")) {
                Main.jedis.hset(train_name.getText(), "non_AC_compartment", non_ac.getText());
            }
            if(!non_ac_seat.getText().equals("")) {
                Main.jedis.hset(train_name.getText(), "non_AC_seat", non_ac_seat.getText());
            }
        }
        else {
            boolean a1 = train_name.getText().equals("");
            boolean a2 = from.getText().equals("") || to.getText().equals("");
            boolean a3 = start_time.getText().equals("") || end_time.getText().equals("");
            boolean a4 = ac_normal.getText().equals("") || ac_normal_seat.getText().equals("")
                    || ac_normal_fare.getText().equals("");
            boolean a5 = ac_cabin.getText().equals("") || ac_cabin_cabin.getText().equals("")
                    || ac_cabin_fare.getText().equals("");
            boolean a6 = non_ac.getText().equals("") || non_ac_seat.getText().equals("")
                    || non_ac_fare.getText().equals("");
            boolean a7 = a1 || a2 || a3 || a4 || a5 || a6;
            if(!a7) {

                //from_to
                String fromS = from.getText();
                String toS = to.getText();
                Main.jedis.sadd("locations", fromS, toS);
                Main.jedis.sadd(fromS + "_" + toS, train_name.getText());

                //train_from_to_time
                String timeS = start_time.getText() + ampm1.getValue();
                String timeE = end_time.getText() + ampm2.getValue();
                String time = timeS + "_" + timeE;
                Main.jedis.sadd(train_name.getText() + "_" + fromS + "_" + toS + "_time", time);


                //train_from_to_fare
                String key = train_name.getText() + "_" + fromS + "_" + toS + "_" + "fare";
                Main.jedis.hset(key, "AC_normal", ac_normal_fare.getText());
                Main.jedis.hset(key, "AC_cabin", ac_cabin_fare.getText());
                Main.jedis.hset(key, "non_AC", non_ac_fare.getText());

                //train
                Main.jedis.hset(train_name.getText(), "AC_normal_compartment", ac_normal.getText());
                Main.jedis.hset(train_name.getText(), "AC_normal_seat", ac_normal_seat.getText());
                Main.jedis.hset(train_name.getText(), "AC_cabin_compartment", ac_cabin.getText());
                Main.jedis.hset(train_name.getText(), "AC_cabin", ac_cabin_cabin.getText());
                Main.jedis.hset(train_name.getText(), "non_AC_compartment", non_ac.getText());
                Main.jedis.hset(train_name.getText(), "non_AC_seat", non_ac_seat.getText());
            }
            else {
                msz.setVisible(true);
            }
        }
        Parent root = FXMLLoader.load(getClass().getResource("Select.fxml"));
        Main.window.setTitle("Railway");
        Main.window.setScene(new Scene(root, 640, 480));
        Main.window.show();
    }

    @FXML
    public void deleteAction(ActionEvent event) {
        boolean a1 = train_name.getText().equals("");
        boolean a2 = from.getText().equals("") || to.getText().equals("");
        boolean a3 = start_time.getText().equals("") || end_time.getText().equals("");
        boolean a4 = ac_normal.getText().equals("") || ac_normal_seat.getText().equals("")
                || ac_normal_fare.getText().equals("");
        boolean a5 = ac_cabin.getText().equals("") || ac_cabin_cabin.getText().equals("")
                || ac_cabin_fare.getText().equals("");
        boolean a6 = non_ac.getText().equals("") || non_ac_seat.getText().equals("")
                || non_ac_fare.getText().equals("");
        if(!a1) {
            if(!a2 && !a3) {
                Main.jedis.srem(train_name.getText() + "_" + from.getText() + "_" + to.getText()
                                + "_time", start_time.getText()+ "_" + end_time.getText());
            }
            else if(!a2) {
                Main.jedis.srem(from.getText() + "_" + to.getText(), train_name.getText());
                Main.jedis.del(train_name.getText() + "_" + from.getText() + "_"
                               + to.getText() + "_time");
                Main.jedis.del(train_name.getText() + "_" + from.getText() + "_"
                               + to.getText() + "_fare");

            }
        }
    }


    @FXML
    public void backAction(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Select.fxml"));
        Main.window.setTitle("Select");
        Main.window.setScene(new Scene(root, 640, 480));
        Main.window.show();
    }
}
