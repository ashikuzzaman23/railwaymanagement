package sample;

/**
 * Created by Toufik on 7/12/2017.
 */
public class Train {
    String start_time;
    String end_time;
    String train_name;
    public Train(String name, String time)
    {
        train_name = name;
        String [] str =time.split("_");
        start_time = str[0];
        end_time = str[1];
    }
    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getTrain_name() {
        return train_name;
    }
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }
}
