package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class Main extends Application {


    public static JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    public static Jedis jedis;

    public static Stage window;

    /*private static void test() {
        try
        {
            jedis = jedisPool.getResource();
            jedis.set("ff", "Hello World");
            jedis.set("YO", "RAFID");
            String result = jedis.get("YO");
            System.out.println(" MSG : " + result);
            result = jedis.get("ff");
            System.out.println(" MSG : " + result);
            jedisPool.returnResource(jedis);
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
        }finally{
            jedisPool.destroy();
        }
    }*/


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Select.fxml"));
        window = primaryStage;
        window.setOnCloseRequest(e->closeProgram());
        primaryStage.setTitle("Railway");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();
    }

    public void closeProgram() {
        jedisPool.returnResource(jedis);
        jedisPool.destroy();
        window.close();
    }

    public static void main(String[] args) {
        jedis = jedisPool.getResource();
        launch(args);
    }
}
