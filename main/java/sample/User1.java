package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by rafid on 7/7/2017.
 */
public class User1 implements Initializable{
    static String key;
    static int non_ac_seat;
    static int ac_normal_seat;
    static int ac_cabin_seat;
    static String from;
    static String to;
    static String train;
    static String fareKey;
    @FXML
    private TextField ACSeats;

    @FXML
    public TextField ACCabinFare;

    @FXML
    private TextField nonACSeats;

    @FXML
    public TextField nonACFare;

    @FXML
    private TextField ACCabins;

    @FXML
    public TextField ACNormalFare;
    @FXML
    public Label err1;
    @FXML
    public Label err2;
    @FXML
    public Label err3;

    @FXML
    public Label msg1;
    @FXML
    public Label msg2;
    @FXML
    public Label msg3;

    @FXML
    public void bookAction(ActionEvent event) {
        err1.setVisible(false);
        err2.setVisible(false);
        err3.setVisible(false);
        msg1.setVisible(false);
        msg2.setVisible(false);
        msg3.setVisible(false);
        if(!Main.jedis.exists(key))
        {
            Main.jedis.hset(key,"non_AC_available", String.valueOf(non_ac_seat));
            Main.jedis.hset(key,"AC_normal_available", String.valueOf(ac_normal_seat));
            Main.jedis.hset(key,"AC_cabin_available", String.valueOf(ac_cabin_seat));
        }
        if(!nonACSeats.getText().equals(""))
        {
            int seats = Integer.parseInt(nonACSeats.getText());
            int available = Integer.parseInt(Main.jedis.hget(key,"non_AC_available"));
            System.out.println(available);
            if(seats<=available)
            {
                Main.jedis.hset(key,"non_AC_available", String.valueOf(available-seats));
                System.out.println(Main.jedis.hget(key,"non_AC_available"));
                msg1.setVisible(true);
            }
            else
            {
                err1.setVisible(true);
            }
        }
        if(!ACSeats.getText().equals(""))
        {
            int seats = Integer.parseInt(ACSeats.getText());
            int available = Integer.parseInt(Main.jedis.hget(key,"AC_normal_available"));
            System.out.println(available);
            if(seats<=available)
            {
                Main.jedis.hset(key,"AC_normal_available", String.valueOf(available-seats));
                System.out.println(Main.jedis.hget(key,"AC_normal_available"));
                msg2.setVisible(true);

            }
            else
            {
                err2.setVisible(true);
            }
        }
        if(!ACCabins.getText().equals(""))
        {
            int seats = Integer.parseInt(ACCabins.getText());
            int available = Integer.parseInt(Main.jedis.hget(key,"AC_cabin_available"));
            System.out.println(available);
            if(seats<=available)
            {
                Main.jedis.hset(key,"AC_cabin_available", String.valueOf(available-seats));
                System.out.println(Main.jedis.hget(key,"AC_cabin_available"));
                msg3.setVisible(true);
            }
            else
            {
                err3.setVisible(true);
            }
        }
    }
    @FXML
    public void backAction(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("User.fxml"));
        Main.window.setTitle("User");
        Main.window.setScene(new Scene(root, 640, 480));
        Main.window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        err1.setVisible(false);
        err2.setVisible(false);
        err3.setVisible(false);
        nonACFare.setText(Main.jedis.hget(fareKey,"non_AC"));
        ACCabinFare.setText(Main.jedis.hget(fareKey,"AC_cabin"));
        ACNormalFare.setText(Main.jedis.hget(fareKey,"AC_normal"));
    }

    public static void initializer(String Train,String KEY,String From,String To)
    {
        key = KEY;
        train=Train;
        from=From;
        to=To;
        fareKey = train+"_"+from+"_"+to+"_fare";
        non_ac_seat = Integer.parseInt(Main.jedis.hget(train,"non_AC_compartment"))
                    *Integer.parseInt(Main.jedis.hget(train,"non_AC_seat"));
        //System.out.println(non_ac_seat);
        ac_normal_seat = Integer.parseInt(Main.jedis.hget(train,"AC_normal_compartment"))
                *Integer.parseInt(Main.jedis.hget(train,"AC_normal_seat"));
        //System.out.println(ac_normal_seat);
        ac_cabin_seat = Integer.parseInt(Main.jedis.hget(train,"AC_cabin_compartment"))
                *Integer.parseInt(Main.jedis.hget(train,"AC_cabin"));
        //System.out.println(ac_cabin_seat);
        //System.out.println(key);
    }
}
