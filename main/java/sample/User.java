package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Created by rafid on 7/7/2017.
 */
public class User implements Initializable{
    @FXML
    public ChoiceBox<String> fromChoiceBox;
    @FXML
    public ChoiceBox<String> toChoiceBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    public TableView<Train> table;
    @FXML
    public TableColumn<Train, String> column1;
    @FXML
    public TableColumn<Train, String> column2;
    @FXML
    public TableColumn<Train, String> column3;

    @FXML
    public Label error_msg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> location_list = FXCollections.observableArrayList();
        Set<String> locations = Main.jedis.smembers("locations");
        for(String s : locations) {
            location_list.add(s);
        }
        fromChoiceBox.setItems(location_list);
        toChoiceBox.setItems(location_list);
        fromChoiceBox.getSelectionModel().selectFirst();
        toChoiceBox.getSelectionModel().selectFirst();
        column1.setCellValueFactory(new PropertyValueFactory<Train,String >("train_name"));
        column2.setCellValueFactory(new PropertyValueFactory<Train,String >("start_time"));
        column3.setCellValueFactory(new PropertyValueFactory<Train,String >("end_time"));
    }

    @FXML
    public void searchAction(ActionEvent event) {
        String date = String.valueOf(datePicker.getValue());
        String from = fromChoiceBox.getValue();
        String to = toChoiceBox.getValue();
        if(!fromChoiceBox.getValue().equals("")&&!toChoiceBox.getValue().equals("")&&!date.equals(""))
        {
            table.setVisible(false);
            LocalDate localDate = datePicker.getValue();
            //Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            //Date ldate = Date.from(instant);
            Date sysdate = new Date();
            LocalDate sysLocalDate = sysdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            //System.out.println(localDate.compareTo(sysLocalDate));
            if(localDate.compareTo(sysLocalDate) < 0){
                error_msg.setVisible(true);
            }
            else if(Main.jedis.exists(from+"_"+to)&&localDate.compareTo(sysLocalDate)>=0)
            {
                Set<String> trains = Main.jedis.smembers(from+"_"+to);
                error_msg.setVisible(false);
                ObservableList<Train> train_list = FXCollections.observableArrayList();
                for(String s:trains)
                {
                    Set<String> s1 = Main.jedis.smembers(s+"_"+from+"_"+to+"_time");
                    for(String s2:s1)
                    {
                        train_list.add(new Train(s,s2));
                    }
                }
                table.setVisible(true);
                table.setItems(train_list);
             }
            else
            {
             //No train in this route
            }

        }
        else
        {
            //provide from to and date correctly
        }
    }

    @FXML
    public void backAction(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Select.fxml"));
        Main.window.setTitle("Select");
        Main.window.setScene(new Scene(root, 640, 480));
        Main.window.show();
    }

    @FXML
    void selectTableItem(MouseEvent e) throws Exception
    {

        if ( e.getClickCount()==2 ) {
            Train t = table.getSelectionModel().getSelectedItem();
            String from = fromChoiceBox.getValue();
            String to = toChoiceBox.getValue();
            String train = t.getTrain_name();
            String date = String.valueOf(datePicker.getValue());
            String time = t.start_time+"_"+t.end_time;
            String key = train+"_"+from+"_"+to+"_"+date+"_"+time;
            User1.initializer(train,key,from,to);
            Parent root = FXMLLoader.load(getClass().getResource("User1.fxml"));
            Main.window.setTitle("Select");
            Main.window.setScene(new Scene(root, 640, 480));
            Main.window.show();
        }
    }
}
